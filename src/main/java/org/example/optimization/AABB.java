package org.example.optimization;

import org.example.math.Ray;
import org.example.math.Vector3;

// * Axis-Aligned Bounding Box (ограничивающий объём описывающий подмножество объектов)
public class AABB {
    public final Vector3 min;
    public final Vector3 max;

    public AABB(Vector3 min, Vector3 max) {
        this.min = min;
        this.max = max;
    }

    public boolean hit(Ray ray, double tMin, double tMax) {
        for (int i = 0; i < 3; i++) {
            double invD = 1.0 / ray.getDirection().get(i);
            double t0 = (getMin(i) - ray.getOrigin().get(i)) * invD;
            double t1 = (getMax(i) - ray.getOrigin().get(i)) * invD;
            if (invD < 0.0) {
                double tmp = t0; t0 = t1; t1 = tmp;
            }
            tMin = Math.max(t0, tMin);
            tMax = Math.min(t1, tMax);
            if (tMax <= tMin) return false;
        }
        return true;
    }

    public static AABB surroundingBox(AABB a, AABB b) {
        Vector3 small = new Vector3(
                Math.min(a.min.x, b.min.x),
                Math.min(a.min.y, b.min.y),
                Math.min(a.min.z, b.min.z)
        );
        Vector3 big = new Vector3(
                Math.max(a.max.x, b.max.x),
                Math.max(a.max.y, b.max.y),
                Math.max(a.max.z, b.max.z)
        );
        return new AABB(small, big);
    }

    private double getMin(int axis) {
        return axis == 0 ? min.x : axis == 1 ? min.y : min.z;
    }

    private double getMax(int axis) {
        return axis == 0 ? max.x : axis == 1 ? max.y : max.z;
    }
}

