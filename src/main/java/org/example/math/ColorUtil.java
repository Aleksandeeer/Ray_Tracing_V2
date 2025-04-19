package org.example.math;

import java.awt.*;

public class ColorUtil {
    public static Vector3 toVec(Color color) {
        return new Vector3(
                color.getRed() / 255.0,
                color.getGreen() / 255.0,
                color.getBlue() / 255.0
        );
    }

    public static Color fromVec(Vector3 vec) {
        return new Color(
                (int) Math.min(vec.x * 255, 255),
                (int) Math.min(vec.y * 255, 255),
                (int) Math.min(vec.z * 255, 255)
        );
    }

    public static Vector3 multiply(Vector3 a, double factor) {
        return new Vector3(a.x * factor, a.y * factor, a.z * factor);
    }

    public static Vector3 multiply(Vector3 a, Vector3 b) {
        return new Vector3(a.x * b.x, a.y * b.y, a.z * b.z); // компонентное перемножение
    }

    public static Vector3 add(Vector3 a, Vector3 b) {
        return new Vector3(a.x + b.x, a.y + b.y, a.z + b.z);
    }
}

