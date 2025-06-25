package org.example.objects;

import org.example.material.Material;
import org.example.math.Ray;
import org.example.math.Vector3;
import org.example.objects.triangles.PyramidTriangle;
import org.example.optimization.AABB;

public class Pyramid implements Hittable {
    private final Vector3 baseCenter;
    private final double size;
    private final double height;
    private final Material material;
    private final Vector3 direction; // направление к вершине (нормализуется)

    private final Vector3 v0, v1, v2, v3;
    private final Vector3 apex;
    private final PyramidTriangle[] sides;

    public Pyramid(Vector3 baseCenter, double size, double height, Vector3 direction, Material material) {
        this.baseCenter = baseCenter;
        this.size = size;
        this.height = height;
        this.direction = direction.normalize();
        this.material = material;

        double halfSize = size / 2.0;
        this.v0 = baseCenter.add(new Vector3(-halfSize, 0, -halfSize));
        this.v1 = baseCenter.add(new Vector3(halfSize, 0, -halfSize));
        this.v2 = baseCenter.add(new Vector3(halfSize, 0, halfSize));
        this.v3 = baseCenter.add(new Vector3(-halfSize, 0, halfSize));

        this.apex = baseCenter.add(direction.normalize().multiply(height));

        this.sides = new PyramidTriangle[] {
                new PyramidTriangle(v1, v0, apex),  // ← порядок важен
                new PyramidTriangle(v2, v1, apex),
                new PyramidTriangle(v3, v2, apex),
                new PyramidTriangle(v0, v3, apex)
        };
    }

    @Override
    public HitResult hit(Ray ray) {
        HitResult closestHit = null;
        double closestT = Double.MAX_VALUE;

        for (PyramidTriangle side : sides) {
            HitResult hit = side.getDetailedHit(ray, 0.001, closestT, material);
            if (hit != null && hit.t < closestT) {
                closestT = hit.t;
                closestHit = hit;
            }
        }

        return closestHit;
    }

    @Override
    public AABB getBoundingBox() {
        AABB combined = sides[0].getBoundingBox();
        for (int i = 1; i < sides.length; i++) {
            combined = AABB.surroundingBox(combined, sides[i].getBoundingBox());
        }
        return combined;
    }
}
