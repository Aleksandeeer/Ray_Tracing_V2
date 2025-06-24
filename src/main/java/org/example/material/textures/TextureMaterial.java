package org.example.material.textures;

import org.example.material.Material;
import org.example.material.ScatterResult;
import org.example.math.Ray;
import org.example.objects.HitResult;

import java.awt.*;

@Deprecated
public class TextureMaterial implements Material {
    private final Texture texture;

    public TextureMaterial(Texture texture) {
        this.texture = texture;
    }

    @Override
    public boolean scatter(Ray rayIn, HitResult hit, ScatterResult result) {
        return false;
    }

    @Override
    public Color getColor() {
        return Color.WHITE;
    }

    @Override
    public Color getAmbient(HitResult hit) {
        return new Color(10, 10, 10);
    }

    @Override
    public Color getDiffuse(HitResult hit) {
        return texture.getColor(hit.u, hit.v, hit.point); // TODO: добавить u,v в HitResult (done)
    }

    @Override
    public Color getSpecular(HitResult hit) {
        return new Color(0, 0, 0);
    }

    @Override
    public double getShininess() {
        return 1.0;
    }
}

