/**
 * [1968] - [2020] Centros Culturales de Mexico A.C / Universidad Panamericana
 * All Rights Reserved.
 */
package Raytracer.lights;

import Raytracer.Intersection;
import Raytracer.Vector3D;

import java.awt.*;

/**
 *
 * @author Jafet Rodríguez
 */
public class DirectionalLight extends Light {
    private Vector3D direction;

    public DirectionalLight(Vector3D position, Vector3D direction, Color color, double intensity){
        super(position, color, intensity);
        setDirection(Vector3D.normalize(direction));
    }
    @Override
    public float getNdotH(Intersection intersection, Vector3D half){
        return (float) Vector3D.dotProduct(intersection.getNormal(), half);
    }

    public Vector3D getDirection() {
        return direction;
    }

    public void setDirection(Vector3D direction) {
        this.direction = direction;
    }

    @Override
    public float getNDotL(Intersection intersection) {
        return (float)Math.max(Vector3D.dotProduct(intersection.getNormal(), Vector3D.scalarMultiplication(getDirection(), -1.0)), 0.0);
    }
}
