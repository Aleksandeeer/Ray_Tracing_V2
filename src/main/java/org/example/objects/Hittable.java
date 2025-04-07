package org.example.objects;

import org.example.material.Material;
import org.example.math.Ray;

public interface Hittable {
    HitResult hit(Ray ray);
}

