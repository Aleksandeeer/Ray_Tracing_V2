package org.example.material;

import org.example.math.Ray;
import org.example.objects.HitResult;

import java.awt.*;

public class PhongMaterial implements Material {
    private final Color color;
    private final double shininess;
    private final double kd, ks, ka;

    public PhongMaterial(Color color, double ka, double kd, double ks, double shininess) {
        this.color = color;
        this.ka = ka; // ambient
        this.kd = kd; // diffuse
        this.ks = ks; // specular
        this.shininess = shininess;
    }

    @Override
    public boolean scatter(Ray rayIn, HitResult hit, ScatterResult result) {
        // Не используем отражения, просто освещаем
        return false;
    }

    @Override
    public Color getColor() {
        return color;
    }

    @Override
    public Color getAmbient(HitResult hit) {
        return scaleColor(color, ka);
    }

    @Override
    public Color getDiffuse(HitResult hit) {
        return scaleColor(color, kd);
    }

    @Override
    public Color getSpecular(HitResult hit) {
        return new Color(255, 255, 255); // белый блик
    }

    @Override
    public double getShininess() {
        return shininess;
    }

    private Color scaleColor(Color color, double factor) {
        return new Color(
                Math.min((int)(color.getRed() * factor), 255),
                Math.min((int)(color.getGreen() * factor), 255),
                Math.min((int)(color.getBlue() * factor), 255)
        );
    }
}

