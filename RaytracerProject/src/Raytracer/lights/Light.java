/**
 * [1968] - [2020] Centros Culturales de Mexico A.C / Universidad Panamericana
 * All Rights Reserved.
 */
package Raytracer.lights;

import Raytracer.Intersection;
import Raytracer.Ray;
import Raytracer.objects.Object3D;
import Raytracer.Vector3D;

import java.awt.*;

/**
 *
 * @author Jafet Rodríguez
 */
public abstract class Light extends Object3D {
    private double intensity;

    public Light(Vector3D position, Color color, double intensity){
        super(position, color, 0, 0);
        setIntensity(intensity);
    }

    public double getIntensity() {
        return intensity;
    }
    /*public double getIntensity(Intersection intersection) {
        double falloff = Math.sqrt(Math.pow(getPosition().getX() - intersection.getPosition().getX(),2) + Math.pow(getPosition().getY() - intersection.getPosition().getY(),2) + Math.pow(getPosition().getZ() - intersection.getPosition().getZ(),2));
        intensity /= distance;
        return intensity;
    }*/

    public void setIntensity(double intensity) {
        this.intensity = intensity;
    }

    public abstract float getNDotL(Intersection intersection);

    public abstract float getNdotH(Intersection intersection, Vector3D half);

    public Intersection getIntersection(Ray ray){
        return new Intersection(Vector3D.ZERO(), -1, Vector3D.ZERO(), null);
    }
}
