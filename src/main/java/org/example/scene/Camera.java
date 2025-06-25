package org.example.scene;

import org.example.Constant;
import org.example.math.Ray;
import org.example.math.Vector3;

public class Camera {
    private final Vector3 origin;
    private final Vector3 lowerLeftCorner;
    private final Vector3 horizontal;
    private final Vector3 vertical;
    private final double viewportHeight;
    private final double viewportWidth;

    public Camera(Vector3 origin, Vector3 lookAt, double fov, int imageWidth, int imageHeight) {
        this.origin = origin;

        // Корректная установка направления для камеры
        Vector3 w = origin.subtract(lookAt).normalize();  // Направление камеры
        Vector3 u = new Vector3(0, 1, 0).cross(w).normalize();  // Вектор для X-оси
        Vector3 v = w.cross(u);  // Вектор для Y-оси

        viewportHeight = Math.tan(Math.toRadians(fov / 2)) * 2;
        viewportWidth = viewportHeight * imageWidth / imageHeight;

        lowerLeftCorner = origin.subtract(u.multiply(viewportWidth / 2))
                .subtract(v.multiply(viewportHeight / 2))
                .subtract(w);

        horizontal = u.multiply(viewportWidth);
        vertical = v.multiply(viewportHeight);
    }

    public Ray getRay(int x, int y) {
        double u = (x + 0.5) / (double) Constant.Scene_parameters.WIDTH;  // 800 - ширина изображения
        double v = (y + 0.5) / (double) Constant.Scene_parameters.HEIGHT;  // 600 - высота изображения

        Vector3 direction = lowerLeftCorner.add(horizontal.multiply(u)).add(vertical.multiply(v)).subtract(origin);
        return new Ray(origin, direction);
    }
}
