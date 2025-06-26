package org.example;

import org.example.math.Vector3;

public class Constant {
    public static class Scene_parameters {
        public static final int SCALE = 1;
        public static final int WIDTH = 800 * SCALE;
        public static final int HEIGHT = 600 * SCALE;
        public static final int MAX_DEPTH = 2;
    }

    public static class TexturesPaths {
        public static String EARTH = "src/main/resources/textures/earth.jpg";
        public static String CHESS = "src/main/resources/textures/chess.png";

        public static String PAWN_OBJ = "src/main/resources/obj/pawn.obj";
    }

    // Перевернуть объект на 180 градусов по Y
    public static Vector3 flipY(Vector3 v) {
        return new Vector3(v.x, -v.y, v.z);
    }


}
