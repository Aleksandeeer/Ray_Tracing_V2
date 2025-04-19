package org.example;

import me.tongfei.progressbar.ProgressBar;
import org.example.material.DiffuseMaterial;
import org.example.material.GlassMaterial;
import org.example.material.Material;
import org.example.material.PhongMaterial;
import org.example.math.Ray;
import org.example.math.Vector3;
import org.example.objects.Cube;
import org.example.objects.Sphere;
import org.example.scene.Camera;
import org.example.scene.Light;
import org.example.scene.Scene;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Main {
    public static final int SCALER = 5;
    public static final int WIDTH = 800 * SCALER;
    public static final int HEIGHT = 600 * SCALER;
    public static final int MAX_DEPTH = 5;

    public static void main(String[] args) throws IOException {
        BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);

        // Создаем сцену
        Scene scene = new Scene();
        scene.setBackground_color(Color.WHITE); // белый фон

        // Материалы
        Material redPhong = new PhongMaterial(new Color(255, 0, 0), 0.1, 0.7, 0.6, 64);
        Material greenDiffuse = new DiffuseMaterial(new Color(0, 255, 0));
        Material blueDiffuse = new DiffuseMaterial(new Color(0, 0, 255));
        Material glass = new GlassMaterial(1.5);

        // Объекты
        scene.addObject(new Sphere(new Vector3(0, 0, -5), 1, redPhong));           // красная глянцевая сфера
        scene.addObject(new Cube(new Vector3(-2, 0, -5), 2, greenDiffuse));         // зелёный куб
        scene.addObject(new Cube(new Vector3(2, -1, -6), 1, blueDiffuse));          // синий куб
        scene.addObject(new Sphere(new Vector3(0, -1, -3), 0.5, glass));            // стеклянная сфера

        // Источники света
        scene.addLight(new Light(new Vector3(0, 5, 0), 1.0));
        scene.addLight(new Light(new Vector3(-5, 5, -5), 0.5));

        // Камера
        Camera camera = new Camera(
                new Vector3(0, 0, 0),
                new Vector3(0, 0, -1),
                60,
                WIDTH,
                HEIGHT
        );

        // Рендеринг сцены с прогресс-баром
        try (ProgressBar pb = new ProgressBar("Ray tracing", WIDTH * HEIGHT)) {
            for (int y = 0; y < HEIGHT; y++) {
                for (int x = 0; x < WIDTH; x++) {
                    Ray ray = camera.getRay(x, y);
                    Color color = scene.trace(ray, MAX_DEPTH);
                    image.setRGB(x, y, color.getRGB());
                    pb.step();
                }
            }
        }

        // Сохранение изображения
        try (ProgressBar pb = new ProgressBar("Saving file", 1)) {
            ImageIO.write(image, "png", new File("output.png"));
            pb.step();
        }
    }
}
