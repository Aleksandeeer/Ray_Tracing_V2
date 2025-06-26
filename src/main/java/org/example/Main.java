package org.example;

import me.tongfei.progressbar.ProgressBar;
import org.example.material.*;
import org.example.material.gradient.GradientTexture;
import org.example.material.image.ImageTexture;
import org.example.material.textures.Texture;
import org.example.material.textures.TexturePhongMaterial;
import org.example.math.Ray;
import org.example.math.Vector3;
import org.example.objects.Plane;
import org.example.objects.triangles.Triangle;
import org.example.parser.ObjParser;
import org.example.scene.Camera;
import org.example.scene.Light;
import org.example.scene.Scene;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedImage image = new BufferedImage(Constant.Scene_parameters.WIDTH, Constant.Scene_parameters.HEIGHT, BufferedImage.TYPE_INT_RGB);

        Scene scene = new Scene();
        scene.setBackground_color(Color.WHITE);

        // * Chess
        Texture chessTexture = new ImageTexture(Constant.TexturesPaths.CHESS);
        Material chess = new TexturePhongMaterial(chessTexture,
                new Color(30, 30, 30),    // ambient
                new Color(255, 255, 255), // specular
                32);                               // shininess

        // * Earth
        Material earthPhong = new TexturePhongMaterial(
                new ImageTexture(Constant.TexturesPaths.EARTH),
                new Color(30, 30, 30),    // ambient
                new Color(255, 255, 255), // specular
                32                                 // shininess
        );

        // * Glass
        Material glass = new GlassMaterial(1.5);

        // * Metal
        Material metal = new MetalMaterial(new Color(192, 192, 192), 0.1);
        Material diffuseMaterial = new LambertMaterial(Color.BLUE);

        Material backgroundMaterial = new TexturePhongMaterial(
                new GradientTexture(
                        new Color(170, 0, 255),
                        new Color(255, 255, 255),
                        -1.5, 5.0
                ),
                new Color(30, 30, 30),    // ambient
                new Color(255, 255, 255), // specular
                32);                               // shininess


        // * PhongMaterial
        Material redPhong = new PhongMaterial(new Color(255, 0, 0), 0.1, 0.7, 0.6, 64);
        Material greenPhong = new PhongMaterial(new Color(0, 255, 0), 0.1, 0.7, 0.6, 64);
        Material bluePhong = new PhongMaterial(new Color(0, 0, 255), 0.1, 0.7, 0.6, 64);

        // * OBJECTS
        scene.addObject(new Plane(new Vector3(0, 3, 0), new Vector3(0, 1, 0), backgroundMaterial));  // потолок
//
//        scene.addObject(new Sphere(new Vector3(0, 0, -5), 1, earthPhong));
//        scene.addObject(new Sphere(new Vector3(-2, -1, -6), 0.5, greenPhong));
//        scene.addObject(new Sphere(new Vector3(0, -1, -3), 0.5, glass));
//        scene.addObject(new Sphere(new Vector3(-3, 0, -6), 1, redPhong));
//
//        scene.addObject(new Pyramid(new Vector3(2, -0.25, -5), 1, 1, new Vector3(-1, 1, 0), glass));
//        scene.addObject(new Pyramid(
//                new Vector3(-2, -2, -5), 1.2, 1,
//                new Vector3(0, 1, 0),
//                new PhongMaterial(new Color(255, 120, 0), 0.3, 0.6, 0.4, 10)
//        ));
//
//        scene.addObject(new Cube(new Vector3(-2, 0.5, -5), 2, glass));
//        scene.addObject(new Cube(new Vector3(2, 0, -6), 1, bluePhong));
        Material defaultMat = new PhongMaterial(new Color(48, 48, 48), 0.2, 0.6, 0.3, 16);
        List<Triangle> model = ObjParser.load(Constant.TexturesPaths.PAWN_OBJ, earthPhong);


        Vector3 offset = new Vector3(0, 3, -15);
        double scale = 6.0;

        for (Triangle tri : model) {
            scene.addObject(new Triangle(
                    Constant.flipY(tri.getV0()).multiply(scale).add(offset),
                    Constant.flipY(tri.getV1()).multiply(scale).add(offset),
                    Constant.flipY(tri.getV2()).multiply(scale).add(offset),
                    defaultMat
            ));

        }
        offset = new Vector3(-2, 3, -10);
        scale = 5.0;
        for (Triangle tri : model) {
            scene.addObject(new Triangle(
                    Constant.flipY(tri.getV0()).multiply(scale).add(offset),
                    Constant.flipY(tri.getV1()).multiply(scale).add(offset),
                    Constant.flipY(tri.getV2()).multiply(scale).add(offset),
                    defaultMat
            ));

        }

        // * LIGHT
        scene.addLight(new Light(new Vector3(0, -5, 0), 2));
        scene.addLight(new Light(new Vector3(-5, 5, -5), 1.5));
        scene.addLight(new Light(new Vector3(5, 2, 0), 1.0)); // дополнительный источник

        scene.buildBVH();

        // * Camera
        Camera camera = new Camera(
                new Vector3(0, 0, 0),
                new Vector3(0, 0, -1),
                60,
                Constant.Scene_parameters.WIDTH,
                Constant.Scene_parameters.HEIGHT
        );

        // * Rendering
        long[] times = renderScene(camera, scene, image);

        // * Saving
        savingFile(image, times[0], times[1], "output");
    }

    public static long[] renderScene(Camera camera, Scene scene, BufferedImage image) {
        long startTime = System.nanoTime();

        int threads = Runtime.getRuntime().availableProcessors();
        ExecutorService executor = Executors.newFixedThreadPool(threads);
        AtomicInteger pixelCounter = new AtomicInteger(0);
        int[][] pixelBuffer = new int[Constant.Scene_parameters.HEIGHT][Constant.Scene_parameters.WIDTH];

        try (ProgressBar pb = new ProgressBar("Ray tracing", Constant.Scene_parameters.WIDTH * Constant.Scene_parameters.HEIGHT)) {
            List<Future<?>> tasks = new ArrayList<>();

            for (int y = 0; y < Constant.Scene_parameters.HEIGHT; y++) {
                final int row = y;
                tasks.add(executor.submit(() -> {
                    for (int x = 0; x < Constant.Scene_parameters.WIDTH; x++) {
                        Ray ray = camera.getRay(x, row);
                        Color color = scene.trace(ray, Constant.Scene_parameters.MAX_DEPTH);
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

        for (int y = 0; y < Constant.Scene_parameters.HEIGHT; y++) {
            for (int x = 0; x < Constant.Scene_parameters.WIDTH; x++) {
                image.setRGB(x, y, pixelBuffer[y][x]);
            }
        }

        return new long[]{startTime, endTime};
    }

    public static void savingFile(BufferedImage image, long startTime, long endTime, String filename) throws IOException {
        try (ProgressBar progressBar = new ProgressBar("Saving file", 1)) {
            ImageIO.write(image, "png", new File(filename + ".png"));
            progressBar.step();
        }

        double duration = (endTime - startTime) / 1_000_000_000.0;
        System.out.println("Время рендеринга: " + String.format("%.2f", duration) + "c.");
    }
}

