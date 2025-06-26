package org.example.objects.triangles;

import org.example.material.Material;
import org.example.math.Ray;
import org.example.math.Vector3;
import org.example.objects.HitResult;
import org.example.objects.Hittable;
import org.example.optimization.AABB;

public class Triangle implements Hittable {
    private final Vector3 v0, v1, v2;
    private final Vector3 normal;
    private final Material material;

    public Triangle(Vector3 v0, Vector3 v1, Vector3 v2, Material material) {
        this.v0 = v0;
        this.v1 = v1;
        this.v2 = v2;
        this.material = material;
        this.normal = (v2.subtract(v0)).cross(v1.subtract(v0)).normalize();
    }

    @Override
    public HitResult hit(Ray ray) {
        double tMin = 0.001;
        double tMax = Double.MAX_VALUE;

        Vector3 edge1 = v1.subtract(v0);
        Vector3 edge2 = v2.subtract(v0);
        Vector3 h = ray.getDirection().cross(edge2);
        double a = edge1.dot(h);

        if (Math.abs(a) < 1e-6) return null; // луч параллелен

        double f = 1.0 / a;
        Vector3 s = ray.getOrigin().subtract(v0);
        double u = f * s.dot(h);
        if (u < 0.0 || u > 1.0) return null;

        Vector3 q = s.cross(edge1);
        double v = f * ray.getDirection().dot(q);
        if (v < 0.0 || u + v > 1.0) return null;

        double t = f * edge2.dot(q);
        if (t < tMin || t > tMax) return null;

        Vector3 point = ray.pointAt(t);

        // Нормаль ориентируем по направлению попадания
        Vector3 outwardNormal = normal;
        boolean frontFace = ray.getDirection().dot(normal) < 0;
        Vector3 finalNormal = frontFace ? outwardNormal : outwardNormal.negate();
        return new HitResult(t, point, finalNormal, material, ray, u, v);
    }

    @Override
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

    public Vector3 getNormal() {
        return normal;
    }

    public Vector3 getV0() {
        return v0;
    }

    public Vector3 getV1() {
        return v1;
    }

    public Vector3 getV2() {
        return v2;
    }
}

