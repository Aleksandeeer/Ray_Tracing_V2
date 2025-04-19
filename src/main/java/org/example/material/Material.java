package org.example.material;

import org.example.math.Ray;
import org.example.objects.HitResult;

import java.awt.*;

public interface Material {
    boolean scatter(Ray rayIn, HitResult hit, ScatterResult result);
    Color getColor();  // базовый цвет
    Color getAmbient();
    Color getDiffuse();
    Color getSpecular();
    double getShininess();
}

