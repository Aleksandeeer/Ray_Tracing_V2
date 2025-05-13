package org.example.material.textures;

import org.example.math.Vector3;

import java.awt.*;

public class GradientTexture implements Texture {
    private final Color bottom;
    private final Color top;
    private final double minY;
    private final double maxY;

    public GradientTexture(Color bottom, Color top, double minY, double maxY) {
        this.bottom = bottom;
        this.top = top;
        this.minY = minY;
        this.maxY = maxY;
    }

    @Override
    public Color getColor(double u, double v, Vector3 point) {
        double t = (point.y - minY) / (maxY - minY); // используем только Y
        t = Math.max(0, Math.min(1, t)); // clamp в [0, 1]

        int r = (int)(bottom.getRed() * (1 - t) + top.getRed() * t);
        int g = (int)(bottom.getGreen() * (1 - t) + top.getGreen() * t);
        int b = (int)(bottom.getBlue() * (1 - t) + top.getBlue() * t);
        return new Color(r, g, b);
    }
}
