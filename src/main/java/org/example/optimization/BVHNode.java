package org.example.optimization;

import org.example.objects.HitResult;
import org.example.objects.Hittable;
import org.example.math.Ray;
import org.example.math.Vector3;

import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class BVHNode implements Hittable {
    private final Hittable left;
    private final Hittable right;
    private final AABB box;

    private static final Random rand = new Random();

    public BVHNode(List<Hittable> objects) {
        this(objects, 0, objects.size());
    }

    public BVHNode(List<Hittable> srcObjects, int start, int end) {
        int axis = rand.nextInt(3);
        Comparator<Hittable> comparator = getComparator(axis);

        int objectSpan = end - start;

        if (objectSpan == 1) {
            left = right = srcObjects.get(start);
        } else if (objectSpan == 2) {
            if (comparator.compare(srcObjects.get(start), srcObjects.get(start + 1)) < 0) {
                left = srcObjects.get(start);
                right = srcObjects.get(start + 1);
            } else {
                left = srcObjects.get(start + 1);
                right = srcObjects.get(start);
            }
        } else {
            srcObjects.subList(start, end).sort(comparator);
            int mid = start + objectSpan / 2;
            left = new BVHNode(srcObjects, start, mid);
            right = new BVHNode(srcObjects, mid, end);
        }

        AABB boxLeft = left.getBoundingBox();
        AABB boxRight = right.getBoundingBox();
        box = AABB.surroundingBox(boxLeft, boxRight);
    }

    @Override
    public HitResult hit(Ray ray) {
        return hit(ray, 0.001, Double.POSITIVE_INFINITY);
    }

    public HitResult hit(Ray ray, double tMin, double tMax) {
        if (!box.hit(ray, tMin, tMax)) return null;

        HitResult hitLeft = left.hit(ray);
        HitResult hitRight = right.hit(ray);

        if (hitLeft != null && hitRight != null) {
            return hitLeft.t < hitRight.t ? hitLeft : hitRight;
        }
        return hitLeft != null ? hitLeft : hitRight;
    }

    public AABB getBoundingBox() {
        return box;
    }

    private Comparator<Hittable> getComparator(int axis) {
        return (a, b) -> {
            AABB boxA = a.getBoundingBox();
            AABB boxB = b.getBoundingBox();
            return Double.compare(
                    axis == 0 ? boxA.min.x : axis == 1 ? boxA.min.y : boxA.min.z,
                    axis == 0 ? boxB.min.x : axis == 1 ? boxB.min.y : boxB.min.z
            );
        };
    }
}

