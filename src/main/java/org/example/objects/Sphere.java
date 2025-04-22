package org.example.objects;

import org.example.material.Material;
import org.example.math.Ray;
import org.example.math.Vector3;

import static org.example.objects.HitResult.getSphereUV;

public class Sphere implements Hittable {
    private final Vector3 center;
    private final double radius;
    private final Material material;

    public Sphere(Vector3 center, double radius, Material material) {
        this.center = center;
        this.radius = radius;
        this.material = material;
    }

    public Vector3 getCenter() {
        return center;
    }

    public double getRadius() {
        return radius;
    }

    @Override
    public HitResult hit(Ray ray) {
        Vector3 oc = ray.getOrigin().subtract(center);
        double a = ray.getDirection().dot(ray.getDirection());
        double b = 2.0 * oc.dot(ray.getDirection());
        double c = oc.dot(oc) - radius * radius;
        double discriminant = b * b - 4 * a * c;

        if (discriminant > 0) {
            double t = (-b - Math.sqrt(discriminant)) / (2.0 * a);
            if (t > 0.001) {
                Vector3 point = ray.getOrigin().add(ray.getDirection().multiply(t));
                Vector3 outwardNormal = point.subtract(center).normalize();

                // 👉 Получаем координаты текстуры
                Vector3 localPoint = point.subtract(center).normalize(); // точка на сфере в нормализованных координатах
                double[] uv = getSphereUV(localPoint);

                return new HitResult(t, point, outwardNormal, material, ray, uv[0], uv[1]);
            }
        }

        return null;
    }

}
