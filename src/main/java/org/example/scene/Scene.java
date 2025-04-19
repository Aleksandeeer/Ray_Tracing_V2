package org.example.scene;

import org.example.material.Material;
import org.example.material.ScatterResult;
import org.example.math.ColorUtil;
import org.example.math.Ray;
import org.example.math.Vector3;
import org.example.objects.HitResult;
import org.example.objects.Hittable;
import org.example.objects.Sphere;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Scene {
    private final List<Hittable> objects;
    private final List<Light> lights;
    private Color background_color;

    public Scene() {
        objects = new ArrayList<>();
        lights = new ArrayList<>();
    }

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

    public Color trace(Ray ray, int depth) {
        if (depth <= 0) return background_color;

        HitResult closestHit = null;
        double closestT = Double.MAX_VALUE;

        for (Hittable object : objects) {
            HitResult hit = object.hit(ray);
            if (hit != null && hit.t < closestT) {
                closestT = hit.t;
                closestHit = hit;
            }
        }

        if (closestHit != null) {
            Material mat = closestHit.material;
            Vector3 viewDir = ray.getDirection().negate();

            Vector3 ambient = ColorUtil.toVec(mat.getAmbient());
            Vector3 diffuse = new Vector3(0, 0, 0);
            Vector3 specular = new Vector3(0, 0, 0);

            for (Light light : lights) {
                Vector3 lightDir = light.getPosition().subtract(closestHit.point).normalize();

                // Тени
                Ray shadowRay = new Ray(closestHit.point.add(closestHit.normal.multiply(0.001)), lightDir);
                boolean inShadow = false;
                for (Hittable object : objects) {
                    HitResult shadowHit = object.hit(shadowRay);
                    if (shadowHit != null && shadowHit.t > 0.001) {
                        inShadow = true;
                        break;
                    }
                }

                if (!inShadow) {
                    double diffFactor = Math.max(0, closestHit.normal.dot(lightDir));
                    diffuse = ColorUtil.add(diffuse,
                            ColorUtil.multiply(ColorUtil.toVec(mat.getDiffuse()), diffFactor * light.getIntensity()));

                    Vector3 reflectDir = reflect(lightDir.negate(), closestHit.normal);
                    double specFactor = Math.pow(Math.max(0, reflectDir.dot(viewDir)), mat.getShininess());
                    specular = ColorUtil.add(specular,
                            ColorUtil.multiply(ColorUtil.toVec(mat.getSpecular()), specFactor * light.getIntensity()));
                }
            }

            Vector3 finalColor = ColorUtil.add(ambient, ColorUtil.add(diffuse, specular));
            return ColorUtil.fromVec(finalColor);
        }

        return background_color;
    }

    private Color multiplyColor(Color a, Color b) {
        return new Color(
                (a.getRed() * b.getRed()) / 255,
                (a.getGreen() * b.getGreen()) / 255,
                (a.getBlue() * b.getBlue()) / 255
        );
    }


    // Метод для получения точки пересечения с объектом
    private double getIntersection(Ray ray, Hittable object) {
        if (object instanceof Sphere) {
            Sphere sphere = (Sphere) object;
            Vector3 oc = ray.getOrigin().subtract(sphere.getCenter());
            double a = ray.getDirection().dot(ray.getDirection());
            double b = 2.0 * oc.dot(ray.getDirection());
            double c = oc.dot(oc) - sphere.getRadius() * sphere.getRadius();
            double discriminant = b * b - 4 * a * c;
            if (discriminant > 0) {
                double t = (-b - Math.sqrt(discriminant)) / (2.0 * a);
                if (t > 0) {
                    return t;
                }
            }
        }
        return Double.MAX_VALUE;  // Если пересечения нет, возвращаем максимально возможное значение
    }

    private Vector3 reflect(Vector3 v, Vector3 normal) {
        return v.subtract(normal.multiply(2 * v.dot(normal)));
    }

}