package org.example.objects;

import org.example.material.Material;
import org.example.math.Ray;
import org.example.math.Vector3;
import org.example.optimization.AABB;

public class Triangle {
    private final Vector3 v0, v1, v2; // Вершины треугольника
    private final Vector3 normal;

    public Triangle(Vector3 v0, Vector3 v1, Vector3 v2) {
        this.v0 = v0;
        this.v1 = v1;
        this.v2 = v2;
        this.normal = (v1.subtract(v0)).cross(v2.subtract(v0)).normalize(); // Нормаль
    }

    // Возвращает true, если есть пересечение (быстрая проверка)
    public boolean hit(Ray ray, double tMin, double tMax) {
        return intersect(ray, tMin, tMax) > 0;
    }

    // Основной метод: возвращает расстояние t до пересечения или -1 если его нет
    public double intersect(Ray ray, double tMin, double tMax) {
        Vector3 edge1 = v1.subtract(v0);
        Vector3 edge2 = v2.subtract(v0);
        Vector3 h = ray.getDirection().cross(edge2);
        double a = edge1.dot(h);

        if (Math.abs(a) < 1e-6) return -1; // Луч параллелен треугольнику

        double f = 1.0 / a;
        Vector3 s = ray.getOrigin().subtract(v0);
        double u = f * s.dot(h);
        if (u < 0.0 || u > 1.0) return -1;

        Vector3 q = s.cross(edge1);
        double v = f * ray.getDirection().dot(q);
        if (v < 0.0 || u + v > 1.0) return -1;

        double t = f * edge2.dot(q);
        if (t < tMin || t > tMax) return -1;

        return t;
    }

    // Возвращает полную информацию о пересечении
    public HitResult getDetailedHit(Ray ray, double tMin, double tMax, Material material) {
        double t = intersect(ray, tMin, tMax);
        if (t <= 0) return null;

        Vector3 point = ray.pointAt(t);
        return new HitResult(t, point, this.normal, material, ray);
    }

    public Vector3 getNormal() {
        return normal;
    }

    // Bounding box для BVH
    public AABB getBoundingBox() {
        Vector3 min = new Vector3(
                Math.min(v0.x, Math.min(v1.x, v2.x)),
                Math.min(v0.y, Math.min(v1.y, v2.y)),
                Math.min(v0.z, Math.min(v1.z, v2.z))
        );
        Vector3 max = new Vector3(
                Math.max(v0.x, Math.max(v1.x, v2.x)),
                Math.max(v0.y, Math.max(v1.y, v2.y)),
                Math.max(v0.z, Math.max(v1.z, v2.z))
        );
        return new AABB(min, max);
    }
}
