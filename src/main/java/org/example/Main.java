package org.example;

import me.tongfei.progressbar.ProgressBar;
import org.example.material.*;
import org.example.math.Ray;
import org.example.math.Vector3;
import org.example.objects.Cube;
import org.example.objects.Sphere;
import org.example.scene.Camera;
import org.example.scene.Light;
import org.example.scene.Scene;
import org.example.textures.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Main {
    public static final int SCALE = 5;
    public static final int WIDTH = 800 * SCALE;
    public static final int HEIGHT = 600 * SCALE;
    public static final int MAX_DEPTH = 5;

    public static void main(String[] args) throws IOException {
        BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);

        // Создаем сцену
        Scene scene = new Scene();
        scene.setBackground_color(Color.WHITE); // белый фон

        // Материалы
        // Texture
        System.out.println("Working Directory = " + System.getProperty("user.dir"));

        Texture earthTexture = new ImageTexture("src/main/resources/earth.jpg");
        Material earth = new TextureMaterial(earthTexture);

        // Diffuse
        Material redPhong = new PhongMaterial(new Color(255, 0, 0), 0.1, 0.7, 0.6, 64);
        Material greenPhong = new PhongMaterial(new Color(0, 255, 0), 0.1, 0.7, 0.6, 64);
        Material bluePhong = new PhongMaterial(new Color(0, 0, 255), 0.1, 0.7, 0.6, 64);

        // Glass
        Material glass = new GlassMaterial(1.5);

        // Metal
        Material metal = new MetalMaterial(new Color(192, 192, 192), 0.1);

        // Объекты
        scene.addObject(new Sphere(new Vector3(0, 0, -5), 1, earth));
        scene.addObject(new Sphere(new Vector3(-2, -1, -6), 0.5, greenPhong));
        scene.addObject(new Sphere(new Vector3(0, -1, -3), 0.5, glass));
        scene.addObject(new Sphere(new Vector3(-3, 0, -6), 1, redPhong));

        scene.addObject(new Cube(new Vector3(-2, 0, -5), 2, glass));
        scene.addObject(new Cube(new Vector3(2, -1, -6), 1, bluePhong));

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
