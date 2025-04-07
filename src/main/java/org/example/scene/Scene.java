package org.example.scene;

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

    public Color trace(Ray ray) {
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
            Color color = closestHit.material.getColor();
            double brightness = 0;

            for (Light light : lights) {
                Vector3 toLight = light.getPosition().subtract(closestHit.point).normalize();
                Ray shadowRay = new Ray(closestHit.point.add(closestHit.normal.multiply(0.001)), toLight);

                boolean inShadow = false;
                for (Hittable object : objects) {
                    HitResult shadowHit = object.hit(shadowRay);
                    if (shadowHit != null && shadowHit.t < closestT) {
                        inShadow = true;
                        break;
                    }
                }

                if (!inShadow) {
                    brightness += Math.max(0, closestHit.normal.dot(toLight)) * light.getIntensity();
                }
            }

            brightness = Math.min(brightness, 1.0);
            return new Color(
                    (int) (color.getRed() * brightness),
                    (int) (color.getGreen() * brightness),
                    (int) (color.getBlue() * brightness)
            );
        }

        return background_color;
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

}