package org.example.material;

import org.example.math.Ray;

public class ScatterResult {
    public Ray scattered;
    public java.awt.Color attenuation;

    public ScatterResult() {
        this.attenuation = new java.awt.Color(255, 255, 255); // Белый по умолчанию
    }
}