package org.example.objects;

import org.example.material.Material;
import org.example.math.Ray;
import org.example.optimization.AABB;

public interface Hittable {
    HitResult hit(Ray ray);
    AABB getBoundingBox();
}

