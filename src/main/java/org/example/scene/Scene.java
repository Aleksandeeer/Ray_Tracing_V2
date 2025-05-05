package org.example.scene;

import org.example.material.GlassMaterial;
import org.example.material.Material;
import org.example.material.ScatterResult;
import org.example.math.ColorUtil;
import org.example.math.Ray;
import org.example.math.Vector3;
import org.example.objects.HitResult;
import org.example.objects.Hittable;
import org.example.objects.Sphere;
import org.example.optimization.BVHNode;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Scene {
    private final List<Hittable> objects = new ArrayList<>();
    private final List<Light> lights = new ArrayList<>();
    private Hittable rootBVH; // корень BVH
    private Color background_color;

    public void addObject(Hittable object) {
        objects.add(object);
    }

    public void addLight(Light light) {
        lights.add(light);
    }

    public void setBackground_color(Color color) {
        background_color = color;
    }

    public Color getBackground_color() {
        return background_color;
    }

    public void buildBVH() {
        rootBVH = new BVHNode(objects); // построить дерево после добавления всех объектов
    }

    public Color trace(Ray ray, int depth) {
        if (depth <= 0) return Color.BLACK;

        HitResult closestHit = rootBVH.hit(ray);
        if (closestHit == null) {
            return background_color;
        }

        ScatterResult scatter = new ScatterResult();
        if (closestHit.material.scatter(ray, closestHit, scatter)) {
            Color scatteredColor = trace(scatter.scattered, depth - 1);
            return multiplyColor(scatter.attenuation, scatteredColor);
        }

        // Фонг-освещение
        Material mat = closestHit.material;
        Vector3 viewDir = ray.getDirection().negate();

        Vector3 ambient = ColorUtil.toVec(mat.getAmbient(closestHit));
        Vector3 diffuse = new Vector3(0, 0, 0);
        Vector3 specular = new Vector3(0, 0, 0);

        for (Light light : lights) {
            Vector3 lightDir = light.getPosition().subtract(closestHit.point).normalize();
            Ray shadowRay = new Ray(closestHit.point.add(closestHit.normal.multiply(0.01)), lightDir);
            boolean inShadow = false;

            HitResult shadowHit = rootBVH.hit(shadowRay);
            if (shadowHit != null && shadowHit.t > 0.001) {
                if (!(shadowHit.material instanceof GlassMaterial)) {
                    inShadow = true;
                }
            }

            if (!inShadow) {
                double diff = Math.max(0, closestHit.normal.dot(lightDir));
                Vector3 diffuseComponent = ColorUtil.multiply(ColorUtil.toVec(mat.getDiffuse(closestHit)), diff * light.getIntensity());
                diffuse = ColorUtil.add(diffuse, diffuseComponent);

                Vector3 reflectDir = reflect(lightDir.negate(), closestHit.normal);
                double specAngle = Math.max(0, reflectDir.dot(viewDir));
                double spec = Math.pow(specAngle, mat.getShininess());
                Vector3 specularComponent = ColorUtil.multiply(ColorUtil.toVec(mat.getSpecular(closestHit)), spec * light.getIntensity());
                specular = ColorUtil.add(specular, specularComponent);
            }
        }

        Vector3 finalColor = ColorUtil.add(ambient, ColorUtil.add(diffuse, specular));
        return ColorUtil.fromVec(finalColor);
    }

    private Color multiplyColor(Color a, Color b) {
        return new Color(
                (a.getRed() * b.getRed()) / 255,
                (a.getGreen() * b.getGreen()) / 255,
                (a.getBlue() * b.getBlue()) / 255
        );
    }

    private Vector3 reflect(Vector3 v, Vector3 normal) {
        return v.subtract(normal.multiply(2 * v.dot(normal)));
    }
}