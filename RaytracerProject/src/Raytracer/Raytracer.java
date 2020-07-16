/**
 * [1968] - [2020] Centros Culturales de Mexico A.C / Universidad Panamericana
 * All Rights Reserved.
 */
package Raytracer;

import Raytracer.lights.Light;
import Raytracer.objects.Camera;
import Raytracer.objects.Object3D;
import Raytracer.lights.PointLight;
import Raytracer.objects.Sphere;
import Raytracer.objects.*;
import Raytracer.tools.OBJReader;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

/**
 * @author Jafet Rodríguez
 */
public class Raytracer {

    public static void main(String[] args) {
        System.out.println(new Date());
        Scene scene01 = new Scene();
        scene01.setCamera(new Camera(new Vector3D(0, 0, -8), 160, 160, 1200, 1200, 8.2f, 50f));
        /*scene01.addLight(new DirectionalLight(Vector3D.ZERO(), new Vector3D(0.0, 0.0, 1.0), Color.WHITE, 0.8));
        scene01.addLight(new DirectionalLight(Vector3D.ZERO(), new Vector3D(0.0, -0.1, 0.1), Color.WHITE, 0.2));
        scene01.addLight(new DirectionalLight(Vector3D.ZERO(), new Vector3D(-0.2, -0.1, 0.0), Color.WHITE, 0.2));*/
        scene01.addLight(new PointLight(new Vector3D(0f, 1f, 0f), Color.WHITE, 2));


        scene01.addObject(OBJReader.GetPolygon("resources/Floor.obj", new Vector3D(0f, -4.0f, 2f), Color.RED, 100, 1.0));
        scene01.addObject(new Sphere(new Vector3D(2f, -3.5F, 4f), 0.5f, Color.PINK, 50, 1.5 ));
        scene01.addObject(OBJReader.GetPolygon("resources/CubeQuad.obj", new Vector3D(2.5f, -3.0f, 7.5f), Color.GREEN, 100, 1.0));
        scene01.addObject(OBJReader.GetPolygon("resources/SmallTeapot.obj", new Vector3D(-3.5f, -4.0f, 7.5f),Color.BLUE, 100.0, 1.0));
        scene01.addObject(OBJReader.GetPolygon("resources/Ring.obj", new Vector3D(-0.0f, -4.0f, 8f), Color.MAGENTA, 200.0 ,2.0));
        //scene01.addObject(new Sphere(new Vector3D(0.2f, -1.7F, 2.7f), .1f, Color.RED, 50, 2.5 ));
        //scene01.addObject(new Sphere(new Vector3D(-.2f, -1.5F, 2.7f), .1f, Color.BLUE, 50, 2.5 ));
        //scene01.addObject(OBJReader.GetPolygon("nmodel_08_43.obj", new Vector3D(-0.0f, -4.0f, 30f), Color.BLUE, 100.0 ,2.0));




        BufferedImage image = raytrace(scene01);
        File outputImage = new File("image.png");
        try {
            ImageIO.write(image, "png", outputImage);
        } catch (IOException ioe) {
            System.out.println("Something failed");
        }
        System.out.println(new Date());
    }

    public static BufferedImage raytrace(Scene scene) {
        Camera mainCamera = scene.getCamera();
        ArrayList<Light> lights = scene.getLights();
        float[] nearFarPlanes = mainCamera.getNearFarPlanes();
        BufferedImage image = new BufferedImage(mainCamera.getResolutionWidth(), mainCamera.getResolutionHeight(), BufferedImage.TYPE_INT_RGB);
        ArrayList<Object3D> objects = scene.getObjects();

        Vector3D[][] positionsToRaytrace = mainCamera.calculatePositionsToRay();
        for (int i = 0; i < positionsToRaytrace.length; i++) {
            for (int j = 0; j < positionsToRaytrace[i].length; j++) {
                double x = positionsToRaytrace[i][j].getX() + mainCamera.getPosition().getX();
                double y = positionsToRaytrace[i][j].getY() + mainCamera.getPosition().getY();
                double z = positionsToRaytrace[i][j].getZ() + mainCamera.getPosition().getZ();


                Ray ray = new Ray(mainCamera.getPosition(), new Vector3D(x, y, z));
                float cameraZ = (float) mainCamera.getPosition().getZ();
                Intersection closestIntersection = raycast(ray, objects, null, new float[]{cameraZ + nearFarPlanes[0], cameraZ + nearFarPlanes[1]});

                //Background color
                Color pixelColor = Color.BLACK;
                if (closestIntersection != null) {
                    pixelColor = Color.BLACK;
                    for (Light light : lights) {
                        Ray shadow = new Ray(closestIntersection.getPosition(),light.getPosition());
                        Intersection shadowCast = raycast(shadow, objects, closestIntersection.getObject(), new float[]{cameraZ + nearFarPlanes[0], cameraZ + nearFarPlanes[1]} );


                        //H = L + V / |L + V|
                        Vector3D LsumV = Vector3D.add(light.getPosition(),mainCamera.getPosition());
                        Vector3D halfVector = Vector3D.scalarMultiplication(LsumV,1/Vector3D.magnitude(LsumV));
                        float NdotH = light.getNdotH(closestIntersection, halfVector);
                        float specularCO = (float) Math.pow(NdotH , closestIntersection.getObject().getShininess() );

                       Vector3D reflectionAngle = Vector3D.substract(ray.getDirection(),Vector3D.scalarMultiplication(Vector3D.scalarMultiplication(closestIntersection.getNormal(),2),Vector3D.dotProduct(ray.getDirection(),closestIntersection.getNormal())));
                        Ray reflectionRay = new Ray(closestIntersection.getPosition(), reflectionAngle);
                        Intersection reflect = raycast(reflectionRay, objects, closestIntersection.getObject(), new float[]{cameraZ + nearFarPlanes[0], cameraZ + nearFarPlanes[1]});


                        float nDotL = light.getNDotL(closestIntersection);
                        float intensity = (float) (light.getIntensity() * nDotL);
                        double falloff = intensity / Math.sqrt(Math.pow(light.getPosition().getX() - closestIntersection.getPosition().getX(),2) + Math.pow(light.getPosition().getY() - closestIntersection.getPosition().getY(),2) + Math.pow(light.getPosition().getZ() - closestIntersection.getPosition().getZ(),2));
                        Color lightColor = light.getColor();
                        Color objColor = closestIntersection.getObject().getColor();
                        float[] lightColors = new float[]{lightColor.getRed()  / 255.0f, lightColor.getGreen() / 255.0f, lightColor.getBlue() / 255.0f};
                        float[] objColors = new float[]{objColor.getRed() * (float)closestIntersection.getObject().getDiffuseCO() / 255.0f, objColor.getGreen() * (float)closestIntersection.getObject().getDiffuseCO()/ 255.0f, objColor.getBlue() * (float)closestIntersection.getObject().getDiffuseCO()/ 255.0f};
                        for (int colorIndex = 0; colorIndex < objColors.length; colorIndex++) {
                            objColors[colorIndex] *= intensity * lightColors[colorIndex] * falloff;
                           if(reflect != null){
                                Color casterColor = closestIntersection.getObject().getColor();
                                float[] reflectionColors = new float[]{casterColor.getRed()/255.0f, casterColor.getGreen()/255.0f,casterColor.getBlue()/255.0f};
                                objColors[colorIndex] *= intensity * lightColors[colorIndex] * falloff + reflectionColors[colorIndex];
                            }
                        }

                        Color diffuse = new Color(clamp(objColors[0], 0, 1),clamp(objColors[1], 0, 1),clamp(objColors[2], 0, 1));
                        Color specular = new Color(clamp(specularCO , 0, 1),clamp(specularCO , 0, 1),clamp(specularCO , 0, 1));
                        Color blinnPhong = addColor(diffuse, specular);
                        if (shadowCast != null){
                          blinnPhong = Color.BLACK;
                        }
                        pixelColor = addColor(pixelColor, blinnPhong);
                    }
                }
                image.setRGB(i, j, pixelColor.getRGB());
            }
        }

        return image;
    }

    public static float clamp(float value, float min, float max) {
        if (value < min) {
            return min;
        }
        if (value > max) {
            return max;
        }
        return value;
    }

    public static Color addColor(Color original, Color otherColor){
        float red = clamp((original.getRed() / 255.0f) + (otherColor.getRed() / 255.0f), 0, 1);
        float green = clamp((original.getGreen() / 255.0f) + (otherColor.getGreen() / 255.0f), 0, 1);
        float blue = clamp((original.getBlue() / 255.0f) + (otherColor.getBlue() / 255.0f), 0, 1);
        return new Color(red, green, blue);
    }

    public static Intersection raycast(Ray ray, ArrayList<Object3D> objects, Object3D caster, float[] clippingPlanes) {
        Intersection closestIntersection = null;

        for (int k = 0; k < objects.size(); k++) {
            Object3D currentObj = objects.get(k);
            if (caster == null || !currentObj.equals(caster)) {
                Intersection intersection = currentObj.getIntersection(ray);
                if (intersection != null) {
                    double distance = intersection.getDistance();
                    if (distance >= 0 &&
                            (closestIntersection == null || distance < closestIntersection.getDistance()) &&
                            (clippingPlanes == null || (intersection.getPosition().getZ() >= clippingPlanes[0] &&
                            intersection.getPosition().getZ() <= clippingPlanes[1]))) {

                        closestIntersection = intersection;
                    }
                }
            }
        }

        return closestIntersection;
    }
}
