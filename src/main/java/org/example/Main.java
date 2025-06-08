package org.example;

import me.tongfei.progressbar.ProgressBar;
import org.example.material.*;
import org.example.math.Ray;
import org.example.math.Vector3;
import org.example.objects.Cube;
import org.example.objects.Plane;
import org.example.objects.Sphere;
import org.example.scene.Camera;
import org.example.scene.Light;
import org.example.scene.Scene;
import org.example.material.textures.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.util.List;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

// TODO: баги:
// ! поменять стеклянный куб на стеклянную сферу

// TODO: фичи:
// ? Дополнительные материалы (пушистый)

public class Main {
    public static final int SCALE = 4;
    public static final int WIDTH = 800 * SCALE;
    public static final int HEIGHT = 600 * SCALE;
    public static final int MAX_DEPTH = 7;

    public static void main(String[] args) throws IOException {
        BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);

        // Создаем сцену
        Scene scene = new Scene();
        scene.setBackground_color(Color.WHITE); // белый фон

        // Материалы
        // Texture
        Texture earthTexture = new ImageTexture("src/main/resources/earth.jpg");

        SinusoidalTexture sinusoidalTexture = new SinusoidalTexture();
        sinusoidalTexture.setBase(new Color(255, 126, 0));
        sinusoidalTexture.setMultiply(100);
        sinusoidalTexture.setVeined(new Color(0, 119, 255));

        // TextureMaterial
        Material earth = new TextureMaterial(earthTexture);
        Material sinusoidalMaterial = new TextureMaterial(sinusoidalTexture);
        Material wallMaterial = new TextureMaterial(
                new GradientTexture(
                        new Color(170, 0, 255),
                        new Color(255, 255, 255),
                        -1.5, 5.0 // диапазон Y в сцене
                )
        );

        // Diffuse
        Material redPhong = new PhongMaterial(new Color(255, 0, 0), 0.1, 0.7, 0.6, 64);
        Material greenPhong = new PhongMaterial(new Color(0, 255, 0), 0.1, 0.7, 0.6, 64);
        Material bluePhong = new PhongMaterial(new Color(0, 0, 255), 0.1, 0.7, 0.6, 64);

        // Glass
        Material glass = new GlassMaterial(1.5);

        // Metal
        Material metal = new MetalMaterial(new Color(192, 192, 192), 0.1);

        Material wall2Material = new TextureMaterial(
                new GradientTexture(
                        new Color(255, 0, 0),
                        new Color(0, 61, 255),
                        -3, 5.0 // диапазон Y в сцене
                )
        );

        Material backgroundMat = new TextureMaterial(
                new GradientTexture(
                        new Color(239, 239, 239), // bottom — светло-серый
                        new Color(0, 70, 255),  // top — небесный синий
                        -1.5, 3.0                 // диапазон Y сцены
                )
        );


        scene.addObject(new Plane(new Vector3(0, -6, 0), new Vector3(0, 1, 0), wallMaterial));  // потолок

        // Объекты
        scene.addObject(new Sphere(new Vector3(0, 0, -5), 1, metal));
        scene.addObject(new Sphere(new Vector3(-2, -1, -6), 0.5, greenPhong));
        scene.addObject(new Sphere(new Vector3(0, -1, -3), 0.5, glass));
        scene.addObject(new Sphere(new Vector3(-3, 0, -6), 1, redPhong));

        scene.addObject(new Cube(new Vector3(-2, 0.5, -5), 2, glass));
        scene.addObject(new Cube(new Vector3(2, -1, -6), 1, bluePhong));

        // Источники света
        scene.addLight(new Light(new Vector3(0, 5, 0), 2));
        scene.addLight(new Light(new Vector3(-5, 5, -5), 1.5));

        scene.buildBVH();

        // Камера
        Camera camera = new Camera(
                new Vector3(0, 0, 0),
                new Vector3(0, 0, -1),
                60,
                WIDTH,
                HEIGHT
        );

        long startTime = System.nanoTime();

        int threads = Runtime.getRuntime().availableProcessors();
        ExecutorService executor = Executors.newFixedThreadPool(threads);
        AtomicInteger pixelCounter = new AtomicInteger(0);
        int[][] pixelBuffer = new int[HEIGHT][WIDTH];

        // Рендеринг сцены с прогресс-баромMore actions
        try (ProgressBar pb = new ProgressBar("Ray tracing", WIDTH * HEIGHT)) {
            List<Future<?>> tasks = new ArrayList<>();

            for (int y = 0; y < HEIGHT; y++) {
                final int row = y;
                tasks.add(executor.submit(() -> {
                    for (int x = 0; x < WIDTH; x++) {
                        Ray ray = camera.getRay(x, row);
                        Color color = scene.trace(ray, MAX_DEPTH);
                        pixelBuffer[row][x] = color.getRGB();
                        pb.step();
                        pixelCounter.incrementAndGet();
                    }
                }));
            }

            for (Future<?> task : tasks) {
                try {
                    task.get();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        long endTime = System.nanoTime();
        executor.shutdown();

        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                image.setRGB(x, y, pixelBuffer[y][x]);
            }
        }

        // Сохранение изображения
        try (ProgressBar progressBar = new ProgressBar("Saving file", 1)) {
            ImageIO.write(image, "png", new File("output.png"));
            progressBar.step();
        }

        double duration = (endTime - startTime) / 1_000_000_000.0;

        System.out.println("Время рендеринга: " + String.format("%.2f", duration) + "c.");
    }
}

