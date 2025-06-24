package org.example.material.textures;

import org.example.material.Material;
import org.example.material.ScatterResult;
import org.example.math.Ray;
import org.example.objects.HitResult;

import java.awt.*;

public class TexturePhongMaterial implements Material {
    private final Texture texture;
    private final Color ambient;
    private final Color specular;
    private final double shininess;

    public TexturePhongMaterial(Texture texture, Color ambient, Color specular, double shininess) {
        this.texture = texture;
        this.ambient = ambient;
        this.specular = specular;
        this.shininess = shininess;
    }

    @Override
    public boolean scatter(Ray rayIn, HitResult hit, ScatterResult result) {
        return false; // не рассеивает свет — используется фонг-освещение
    }

    @Override
    public Color getColor() {
        return Color.WHITE;
    }

    @Override
    public Color getAmbient(HitResult hit) {
        return ambient;
    }

    @Override
    public Color getDiffuse(HitResult hit) {
        return texture.getColor(hit.u, hit.v, hit.point);
    }

    @Override
    public Color getSpecular(HitResult hit) {
        return specular;
    }

    @Override
    public double getShininess() {
        return shininess;
    }
}

