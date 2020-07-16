/**
 * [1968] - [2020] Centros Culturales de Mexico A.C / Universidad Panamericana
 * All Rights Reserved.
 */
package Raytracer.objects;

import Raytracer.IIntersectable;
import Raytracer.Vector3D;

import java.awt.*;

/**
 *
 * @author Jafet Rodríguez
 */
public abstract class Object3D implements IIntersectable {

    private Vector3D position;
    private Color color;
    private double Shininess;
    private double diffuseCO;

    public Vector3D getPosition() {
        return position;
    }

    public void setPosition(Vector3D position) {
        this.position = position;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Object3D(Vector3D position, Color color, double Shininess, double diffuseCO) {
        setPosition(position);
        setColor(color);
        setShininess(Shininess);
        setDiffuseCO(diffuseCO);
    }

    public double getShininess() {
        return Shininess;
    }

    public void setShininess(double shininess) {
        Shininess = shininess;
    }

    public double getDiffuseCO() {
        return diffuseCO;
    }

    public void setDiffuseCO(double diffuseCO) {
        this.diffuseCO = diffuseCO;
    }
}
