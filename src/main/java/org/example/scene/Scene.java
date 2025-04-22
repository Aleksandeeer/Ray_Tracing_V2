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
        if (depth <= 0) {
            return Color.BLACK; // достигли предела глубины трассировки
        }

        HitResult closestHit = null;
        double closestT = Double.MAX_VALUE;

        for (Hittable object : objects) {
            HitResult hit = object.hit(ray);
            if (hit != null && hit.t < closestT) {
                closestT = hit.t;
                closestHit = hit;
            }
        }

        // Если пересечений нет — возвращаем фон
        if (closestHit == null) {
            return background_color;
        }

        // Если материал рассеивает свет (например, стекло, зеркало и т.д.)
        ScatterResult scatter = new ScatterResult();
        if (closestHit.material.scatter(ray, closestHit, scatter)) {
            Color scatteredColor = trace(scatter.scattered, depth - 1);
            return multiplyColor(scatter.attenuation, scatteredColor);
        }

        // Иначе — применяем модель Фонга (нерассеивающие материалы: ламберт, фонг)
        Material mat = closestHit.material;
        Vector3 viewDir = ray.getDirection().negate();

        Vector3 ambient = ColorUtil.toVec(mat.getAmbient(closestHit));
        Vector3 diffuse = new Vector3(0, 0, 0);
        Vector3 specular = new Vector3(0, 0, 0);

        for (Light light : lights) {
            Vector3 lightDir = light.getPosition().subtract(closestHit.point).normalize();

            // Проверка на тень
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
                // Diffuse
                double diff = Math.max(0, closestHit.normal.dot(lightDir));
                Vector3 diffuseComponent = ColorUtil.multiply(ColorUtil.toVec(mat.getDiffuse(closestHit)), diff * light.getIntensity());
                diffuse = ColorUtil.add(diffuse, diffuseComponent);

                // Specular
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