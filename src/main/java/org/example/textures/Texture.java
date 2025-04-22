package org.example.textures;

import org.example.math.Vector3;

import java.awt.*;

public interface Texture {
    Color getColor(double u, double v, Vector3 point);
}

