/**
 * [1968] - [2020] Centros Culturales de Mexico A.C / Universidad Panamericana
 * All Rights Reserved.
 */
package Raytracer;

import Raytracer.lights.Light;
import Raytracer.objects.Camera;
import Raytracer.objects.Object3D;
import Raytracer.Raytracer;

import java.util.ArrayList;

/**
 *
 * @author Jafet Rodríguez
 */
public class Scene {

    private Camera camera;
    private ArrayList<Object3D> objects;
    private ArrayList<Light> lights;

    public Scene(){
        setObjects(new ArrayList<Object3D>());
        setLights(new ArrayList<Light>());
    }

    public Camera getCamera() {
        return camera;
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    public void addObject(Object3D object){
        getObjects().add(object);
    }

    public ArrayList<Object3D> getObjects() {
        return objects;
    }

    public void setObjects(ArrayList<Object3D> objects) {
        this.objects = objects;
    }

    public ArrayList<Light> getLights() {
        return lights;
    }

    public void setLights(ArrayList<Light> lights) {
        this.lights = lights;
    }

    public void addLight(Light light){
        getLights().add(light);
    }
}
