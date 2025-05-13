package org.example.objects;

import org.example.material.Material;
import org.example.math.Ray;
import org.example.math.Vector3;
import org.example.optimization.AABB;

public class Plane implements Hittable {
    private final Vector3 point;     // любая точка на плоскости
    private final Vector3 normal;    // нормаль плоскости (обязательно нормализована)
    private final Material material;

    public Plane(Vector3 point, Vector3 normal, Material material) {
        this.point = point;
        this.normal = normal.normalize();
        this.material = material;
    }

    @Override
    public HitResult hit(Ray ray) {
        double denom = normal.dot(ray.getDirection());
        if (Math.abs(denom) < 1e-6) return null; // луч параллелен плоскости

        double t = (point.subtract(ray.getOrigin())).dot(normal) / denom;

        if (t < 1e-6) return null;

        Vector3 hitPoint = ray.pointAt(t);
        return new HitResult(t, hitPoint, normal, material, ray);
    }

    @Override
    public AABB getBoundingBox() {
        // Можно вернуть null или гигантский box, если используешь BVH
        return null;
    }
}
