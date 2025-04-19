package org.example.material;

import org.example.math.Ray;
import org.example.math.Vector3;
import org.example.objects.HitResult;

import java.awt.*;

public class DiffuseMaterial implements Material {
    private final Color baseColor;

    public DiffuseMaterial(Color baseColor) {
        this.baseColor = baseColor;
    }

    @Override
    public boolean scatter(Ray rayIn, HitResult hit, ScatterResult result) {
        // ! Это материал без отражений, используется только в модели Фонга
        return false;
    }

    @Override
    public Color getColor() {
        return baseColor;
    }

    @Override
    public Color getAmbient() {
        return scaleColor(baseColor, 0.1); // слабое общее освещение
    }

    @Override
    public Color getDiffuse() {
        return baseColor;
    }

    @Override
    public Color getSpecular() {
        return new Color(0, 0, 0); // нет блика
    }

    @Override
    public double getShininess() {
        return 1.0;
    }

    private Color scaleColor(Color c, double factor) {
        return new Color(
                (int) Math.min(c.getRed() * factor, 255),
                (int) Math.min(c.getGreen() * factor, 255),
                (int) Math.min(c.getBlue() * factor, 255)
        );
    }
}

