package org.example.objects;

import org.example.material.Material;
import org.example.math.Ray;
import org.example.math.Vector3;

public class Cube implements Hittable {
    private final Vector3 min;
    private final Vector3 max;
    private final Material material;

    public Cube(Vector3 center, double size, Material material) {
        double half = size / 2;
        this.min = center.subtract(new Vector3(half, half, half));
        this.max = center.add(new Vector3(half, half, half));
        this.material = material;
    }

    @Override
    public HitResult hit(Ray ray) {
        double tMin = (min.x - ray.getOrigin().x) / ray.getDirection().x;
        double tMax = (max.x - ray.getOrigin().x) / ray.getDirection().x;
        if (tMin > tMax) { double tmp = tMin; tMin = tMax; tMax = tmp; }

        double tyMin = (min.y - ray.getOrigin().y) / ray.getDirection().y;
        double tyMax = (max.y - ray.getOrigin().y) / ray.getDirection().y;
        if (tyMin > tyMax) { double tmp = tyMin; tyMin = tyMax; tyMax = tmp; }

        if ((tMin > tyMax) || (tyMin > tMax)) return null;

        if (tyMin > tMin) tMin = tyMin;
        if (tyMax < tMax) tMax = tyMax;

        double tzMin = (min.z - ray.getOrigin().z) / ray.getDirection().z;
        double tzMax = (max.z - ray.getOrigin().z) / ray.getDirection().z;
        if (tzMin > tzMax) { double tmp = tzMin; tzMin = tzMax; tzMax = tmp; }

        if ((tMin > tzMax) || (tzMin > tMax)) return null;

        if (tzMin > tMin) tMin = tzMin;
        if (tzMax < tMax) tMax = tzMax;

        if (tMin < 0.001) return null;

        Vector3 point = ray.getOrigin().add(ray.getDirection().multiply(tMin));
        Vector3 normal = computeNormal(point);
        return new HitResult(tMin, point, normal, material, ray);
    }

    private Vector3 computeNormal(Vector3 point) {
        double epsilon = 1e-4;
        if (Math.abs(point.x - min.x) < epsilon) return new Vector3(-1, 0, 0);
        if (Math.abs(point.x - max.x) < epsilon) return new Vector3(1, 0, 0);
        if (Math.abs(point.y - min.y) < epsilon) return new Vector3(0, -1, 0);
        if (Math.abs(point.y - max.y) < epsilon) return new Vector3(0, 1, 0);
        if (Math.abs(point.z - min.z) < epsilon) return new Vector3(0, 0, -1);
        return new Vector3(0, 0, 1);
    }
}
