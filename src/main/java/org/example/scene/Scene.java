package org.example.scene;

import org.example.math.Ray;
import org.example.math.Vector3;
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
        // Для каждого объекта сцены проверяем, есть ли пересечение с лучом
        for (Hittable object : objects) {
            if (object.hit(ray)) {
                // Если луч пересек объект, вычисляем точку пересечения
                double t = getIntersection(ray, object);
                Vector3 intersection = ray.getOrigin().add(ray.getDirection().multiply(t));  // Точка пересечения

                // Получаем нормаль в точке пересечения
                Vector3 normal = intersection.subtract(((Sphere) object).getCenter()).normalize(); // Для сферы
                Color color = ((Sphere) object).getMaterial().getColor();

                // Итерация по всем источникам света
                for (Light light : lights) {
                    Vector3 toLight = light.getPosition().subtract(intersection).normalize();  // Направление к источнику света

                    // Проверка, не находится ли объект в тени (простейший способ: луч от объекта к источнику света)
                    Ray shadowRay = new Ray(intersection.add(normal.multiply(0.001)), toLight); // Добавляем небольшой смещение, чтобы избежать самопересечений
                    boolean inShadow = false;

                    // Проверяем, есть ли объект между точкой пересечения и источником света
                    for (Hittable obj : objects) {
                        if (obj != object && obj.hit(shadowRay)) {
                            inShadow = true; // Объект в тени
                            break;
                        }
                    }

                    // Если объект не в тени, то учитываем освещенность
                    if (!inShadow) {
                        // Простейшее диффузное освещение: интенсивность пропорциональна углу между нормалью и светом
                        double brightness = Math.max(0, normal.dot(toLight));  // Косинус угла между нормалью и направлением на источник света

                        // Умножаем цвет объекта на яркость
                        int r = (int) (color.getRed() * brightness);
                        int g = (int) (color.getGreen() * brightness);
                        int b = (int) (color.getBlue() * brightness);

                        // Возвращаем цвет с учетом освещенности
                        return new Color(r, g, b);
                    }
                }

                // Если объект не в тени и светит, возвращаем его цвет
                return color;
            }
        }

        // Черный цвет, если луч не пересекает ни один объект
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