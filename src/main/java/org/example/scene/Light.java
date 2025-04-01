package org.example.scene;

import org.example.math.Vector3;

public class Light {
    private final Vector3 position;
    private final double intensity;

    public Light(Vector3 position, double intensity) {
        this.position = position;
        this.intensity = intensity;
    }

    public Vector3 getPosition() {
        return position;
    }

    public double getIntensity() {
        return intensity;
    }
}
