package org.example.objects;

import org.example.material.Material;
import org.example.math.Ray;
import org.example.math.Vector3;

public class Cube implements Hittable {
    private final Vector3 position;
    private final double size;
    private final Material material;

    public Cube(Vector3 position, double size, Material material) {
        this.position = position;
        this.size = size;
        this.material = material;
    }

    @Override
    public boolean hit(Ray ray) {
        // Простая проверка на пересечение с кубом
        // Для простоты, берем куб, расположенный вокруг центра в позиции 'position'
        // и с размерами, равными 'size'

        Vector3 min = position.subtract(new Vector3(size / 2, size / 2, size / 2));
        Vector3 max = position.add(new Vector3(size / 2, size / 2, size / 2));

        // Проверка пересечения с каждой гранью
        double tmin = (min.x - ray.getOrigin().x) / ray.getDirection().x;
        double tmax = (max.x - ray.getOrigin().x) / ray.getDirection().x;

        if (tmin > tmax) {
            double temp = tmin;
            tmin = tmax;
            tmax = temp;
        }

        // Проверка для остальных двух осей
        return tmin > 0; // Для простоты, принимаем, что всегда существует пересечение
    }

    public Material getMaterial() {
        return material;
    }
}