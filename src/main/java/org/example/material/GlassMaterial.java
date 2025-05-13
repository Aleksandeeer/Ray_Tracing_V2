package org.example.material;

import org.example.math.Ray;
import org.example.math.RefractionUtil;
import org.example.math.Vector3;
import org.example.objects.HitResult;

import java.awt.*;

public class GlassMaterial implements Material {
    private final double refIdx; // показатель преломления

    public GlassMaterial(double refIdx) {
        this.refIdx = refIdx;
    }

    @Override
    public boolean scatter(Ray rayIn, HitResult hit, ScatterResult result) {
        result.attenuation = new Color(255, 255, 255); // стекло не окрашивает свет

        Vector3 unitDir = rayIn.getDirection().normalize();
        double cosTheta = Math.min(hit.normal.negate().dot(unitDir), 1.0);
        double sinTheta = Math.sqrt(1.0 - cosTheta * cosTheta);

        double etaRatio = hit.frontFace ? (1.0 / refIdx) : refIdx;

        boolean cannotRefract = etaRatio * sinTheta > 1.0;
        Vector3 direction;

        if (cannotRefract || RefractionUtil.schlickReflectance(cosTheta, etaRatio) > Math.random()) {
            direction = RefractionUtil.reflect(unitDir, hit.normal);
        } else {
            direction = RefractionUtil.refract(unitDir, hit.normal, etaRatio);
        }

        result.scattered = new Ray(hit.point.add(direction.multiply(0.02)), direction);
        return true;
    }

    @Override
    public Color getColor() {
        return new Color(255, 255, 255); // стекло = прозрачное
    }

    @Override
    public Color getAmbient(HitResult hit) {
        return new Color(20, 20, 20); // слабый свет вокруг
    }

    @Override
    public Color getDiffuse(HitResult hit) {
        return new Color(0, 0, 0); // стекло почти не имеет диффузной составляющей
    }

    @Override
    public Color getSpecular(HitResult hit) {
        return new Color(255, 255, 255); // белые блики
    }

    @Override
    public double getShininess() {
        return 128; // стекло = очень гладкое
    }
}
