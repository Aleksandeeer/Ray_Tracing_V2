package org.example.material;

import java.awt.*;

public class Material {
    private final Color color;
    private final double reflection;

    public Material(Color color, double reflection) {
        this.color = color;
        this.reflection = reflection;
    }

    public Color getColor() {
        return color;
    }

    public double getReflection() {
        return reflection;
    }
}