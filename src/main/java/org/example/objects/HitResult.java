package org.example.objects;

import org.example.material.Material;
import org.example.math.Vector3;

public class HitResult {
    public final double t;
    public final Vector3 point;
    public final Vector3 normal;
    public final Material material;

    public HitResult(double t, Vector3 point, Vector3 normal, Material material) {
        this.t = t;
        this.point = point;
        this.normal = normal;
        this.material = material;
    }
}

