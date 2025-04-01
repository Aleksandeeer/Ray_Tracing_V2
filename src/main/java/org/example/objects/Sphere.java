package org.example.objects;

import org.example.material.Material;
import org.example.math.Ray;
import org.example.math.Vector3;

public class Sphere implements Hittable {
    private final Vector3 center;
    private final double radius;
    private final Material material;

    public Sphere(Vector3 center, double radius, Material material) {
        this.center = center;
        this.radius = radius;
        this.material = material;
    }

    @Override
    public boolean hit(Ray ray) {
        Vector3 oc = ray.getOrigin().subtract(center);
        double a = ray.getDirection().dot(ray.getDirection());
        double b = 2.0 * oc.dot(ray.getDirection());
        double c = oc.dot(oc) - radius * radius;
        double discriminant = b * b - 4 * a * c;

        return discriminant > 0;
    }
    @Override
    public Material getMaterial() {
        return material;
    }

    // Метод для получения центра сферы
    public Vector3 getCenter() {
        return center;
    }

    public double getRadius() { return radius; }
}
