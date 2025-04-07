package org.example.objects;

import org.example.math.Ray;
import org.example.math.Vector3;

public class Triangle {
    private final Vector3 v0, v1, v2; // Вершины треугольника
    private final Vector3 normal;

    public Triangle(Vector3 v0, Vector3 v1, Vector3 v2) {
        this.v0 = v0;
        this.v1 = v1;
        this.v2 = v2;
        this.normal = (v1.subtract(v0)).cross(v2.subtract(v0)).normalize(); // Нормаль
    }

    public boolean hit(Ray ray, double tMin, double tMax) {
        // Плоское пересечение луча с треугольником
        Vector3 edge1 = v1.subtract(v0);
        Vector3 edge2 = v2.subtract(v0);
        Vector3 h = ray.getDirection().cross(edge2);
        double a = edge1.dot(h);

        if (a > -0.0001 && a < 0.0001) return false; // Луч параллелен плоскости

        double f = 1.0 / a;
        Vector3 s = ray.getOrigin().subtract(v0);
        double u = f * s.dot(h);

        if (u < 0.0 || u > 1.0) return false; // Луч не пересекает треугольник

        Vector3 q = s.cross(edge1);
        double v = f * ray.getDirection().dot(q);

        if (v < 0.0 || u + v > 1.0) return false; // Треугольник не пересекается

        double t = f * edge2.dot(q);
        return t > tMin && t < tMax;
    }

    public Vector3 getNormal() {
        return normal;
    }
}
