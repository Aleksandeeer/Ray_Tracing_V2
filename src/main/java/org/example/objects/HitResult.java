package org.example.objects;

import org.example.material.Material;
import org.example.math.Ray;
import org.example.math.Vector3;

public class HitResult {
    public final double t;
    public final Vector3 point;
    public final Vector3 normal;
    public final Material material;
    public final boolean frontFace;

    public HitResult(double t, Vector3 point, Vector3 outwardNormal, Material material, Ray ray) {
        this.t = t;
        this.point = point;
        this.frontFace = ray.getDirection().dot(outwardNormal) < 0;
        this.normal = frontFace ? outwardNormal : outwardNormal.negate();
        this.material = material;
    }
}

