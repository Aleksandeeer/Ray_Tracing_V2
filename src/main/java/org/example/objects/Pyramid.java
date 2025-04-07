package org.example.objects;

import org.example.material.Material;
import org.example.math.Ray;
import org.example.math.Vector3;

public class Pyramid implements Hittable {
    private final Vector3 baseCenter;
    private final double size; // Размер основания
    private final double height; // Высота пирамиды
    private final Material material;

    // Вершины основания
    private final Vector3 v0, v1, v2, v3;
    // Вершина пирамиды
    private final Vector3 apex;

    private final Triangle[] sides;

    public Pyramid(Vector3 baseCenter, double size, double height, Material material) {
        this.baseCenter = baseCenter;
        this.size = size;
        this.height = height;
        this.material = material;

        // Рассчитываем вершины основания
        double halfSize = size / 2.0;
        this.v0 = baseCenter.add(new Vector3(-halfSize, 0, -halfSize));
        this.v1 = baseCenter.add(new Vector3(halfSize, 0, -halfSize));
        this.v2 = baseCenter.add(new Vector3(halfSize, 0, halfSize));
        this.v3 = baseCenter.add(new Vector3(-halfSize, 0, halfSize));

        // Вершина пирамиды
        this.apex = baseCenter.add(new Vector3(0, height, 0));

        // Создаем треугольники для боковых граней пирамиды
        sides = new Triangle[]{
                new Triangle(v0, v1, apex),
                new Triangle(v1, v2, apex),
                new Triangle(v2, v3, apex),
                new Triangle(v3, v0, apex)
        };
    }

    @Override
    public HitResult hit(Ray ray) {
        HitResult closestHit = null;
        double closestT = Double.MAX_VALUE;

        // Проверка пересечений с боковыми гранями
        for (Triangle side : sides) {
            if (side.hit(ray, 0.001, closestT)) {
                // Если пересечение найдено, сохраняем его
                closestT = 0.001;
                Vector3 point = ray.getOrigin().add(ray.getDirection().multiply(closestT));
                Vector3 normal = side.getNormal();
                closestHit = new HitResult(closestT, point, normal, material);
            }
        }

        return closestHit;
    }
}