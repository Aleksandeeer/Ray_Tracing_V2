package org.example.objects;

import org.example.material.Material;
import org.example.math.Ray;
import org.example.math.Vector3;

public class Pyramid implements Hittable {
    private final Vector3 baseCenter;
    private final double baseSize;
    private final double height;
    private final Material material;

    public Pyramid(Vector3 baseCenter, double baseSize, double height, Material material) {
        this.baseCenter = baseCenter;
        this.baseSize = baseSize;
        this.height = height;
        this.material = material;
    }

    @Override
    public boolean hit(Ray ray) {
        // Для упрощения, предположим, что проверка пересечения с пирамидой
        // может быть выполнена как проверка на пересечение с четырьмя боковыми гранями
        // и основанием. Сложную логику можно добавлять по мере необходимости.
        return false;  // Здесь можно добавить проверку пересечения с пирамидой.
    }

    public Material getMaterial() {
        return material;
    }
}
