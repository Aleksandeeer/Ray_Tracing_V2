package org.example;

import me.tongfei.progressbar.ProgressBar;
import org.example.material.Material;
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
    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;

    public static void main(String[] args) throws IOException {
        BufferedImage image;
        Scene scene;
        Camera camera;

        try (ProgressBar pb = new ProgressBar("Setting scene", 6)) {
            image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
            pb.step();

            scene = new Scene();
            pb.step();

            scene.setBackground_color(new Color(255, 255, 255));
            pb.step();

            scene.addObject(new Sphere(new Vector3(0, 0, -5), 1,
                    new Material(new Color(0, 255, 15), 0.5)));  // Сфера с красным материалом
            pb.step();

            scene.addLight(new Light(new Vector3(0, 5, -5), 1.0));  // Источник света
            pb.step();

            camera = new Camera(new Vector3(0, 0, 0), new Vector3(0, 0, -5), 90, WIDTH, HEIGHT);
            pb.step();
        }

        try (ProgressBar pb = new ProgressBar("Ray tracing", WIDTH * HEIGHT)) {
            for (int y = 0; y < HEIGHT; y++) {
                for (int x = 0; x < WIDTH; x++) {
                    pb.step();
                    Ray ray = camera.getRay(x, y);
                    Color color = scene.trace(ray); // Трассировка луча
                    image.setRGB(x, y, color.getRGB());
                }
            }
        }

        try (ProgressBar pb = new ProgressBar("Saving file", 2)) {
            File outputfile = new File("output.png");
            pb.step();
            ImageIO.write(image, "png", outputfile);
            pb.step();
        }
    }
}
