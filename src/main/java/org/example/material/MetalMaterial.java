package org.example.material;

import org.example.math.Ray;
import org.example.math.RefractionUtil;
import org.example.math.Vector3;
import org.example.objects.HitResult;

import java.awt.*;

public class MetalMaterial implements Material {
    private final Color baseColor;
    private final double fuzz;

    public MetalMaterial(Color baseColor, double fuzz) {
        this.baseColor = baseColor;
        this.fuzz = Math.min(fuzz, 1.0); // ограничим от 0 до 1
    }

    @Override
    public boolean scatter(Ray rayIn, HitResult hit, ScatterResult result) {
        Vector3 reflected = RefractionUtil.reflect(rayIn.getDirection().normalize(), hit.normal);
        Vector3 scatteredDir = reflected.add(Vector3.randomInUnitSphere().multiply(fuzz));

        result.scattered = new Ray(hit.point.add(hit.normal.multiply(0.001)), scatteredDir);
        result.attenuation = baseColor;

        return scatteredDir.dot(hit.normal) > 0;
    }

    @Override
    public Color getColor() {
        return baseColor;
    }

    @Override
    public Color getAmbient(HitResult hit) {
        return scaleColor(baseColor, 0.1);
    }

    @Override
    public Color getDiffuse(HitResult hit) {
        return scaleColor(baseColor, 0.3);
    }

    @Override
    public Color getSpecular(HitResult hit) {
        return new Color(255, 255, 255);
    }

    @Override
    public double getShininess() {
        return 64.0;
    }

    private Color scaleColor(Color color, double factor) {
        return new Color(
                (int) Math.min(color.getRed() * factor, 255),
                (int) Math.min(color.getGreen() * factor, 255),
                (int) Math.min(color.getBlue() * factor, 255)
        );
    }
}
