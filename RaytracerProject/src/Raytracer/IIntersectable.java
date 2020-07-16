/**
 * [1968] - [2020] Centros Culturales de Mexico A.C / Universidad Panamericana
 * All Rights Reserved.
 */
package Raytracer;

/**
 *
 * @author Jafet Rodríguez
 */
public interface IIntersectable {
    public abstract Intersection getIntersection(Ray ray);
}
