package org.example.material.textures;

import org.example.math.Vector3;

import java.awt.*;


public class SinusoidalTexture implements Texture {
    int multiply = 5;
    Color base = new Color(120, 110, 100); // серо-коричневый
    Color veined = new Color(200, 200, 200);

    @Override
    public Color getColor(double u, double v, Vector3 point) {
        // Псевдошум: волны по координатам
        double pattern = Math.sin(multiply * point.x + 5 * Math.sin(multiply * point.z))
                + Math.sin(multiply * point.y + 5 * Math.sin(multiply * point.x));

        pattern = (Math.sin(pattern) + 1) / 2.0; // [0, 1]

        // Интерполяция цвета
        int r = (int)(base.getRed() * (1 - pattern) + veined.getRed() * pattern);
        int g = (int)(base.getGreen() * (1 - pattern) + veined.getGreen() * pattern);
        int b = (int)(base.getBlue() * (1 - pattern) + veined.getBlue() * pattern);

        return new Color(clamp(r), clamp(g), clamp(b));
    }

    private int clamp(int c) {
        return Math.max(0, Math.min(255, c));
    }

    public void setMultiply(int multiply) {
        this.multiply = multiply;
    }

    public void setBase(Color base) {
        this.base = base;
    }

    public void setVeined(Color veined) {
        this.veined = veined;
    }
}