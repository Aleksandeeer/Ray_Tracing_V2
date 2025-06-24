package org.example.material.image;

import org.example.material.textures.Texture;
import org.example.math.Vector3;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageTexture implements Texture {
    private final BufferedImage image;

    public ImageTexture(String path) throws IOException {
        this.image = ImageIO.read(new File(path));
    }

    @Override
    public Color getColor(double u, double v, Vector3 point) {
        int x = (int)(u * (image.getWidth() - 1));
        int y = (int)((1 - v) * (image.getHeight() - 1)); // инвертируем v
        return new Color(image.getRGB(x, y));
    }
}
