package org.example.parser;

import org.example.material.Material;
import org.example.math.Vector3;
import org.example.objects.triangles.Triangle;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class ObjParser {

    public static List<Triangle> load(String path, Material material) {
        List<Vector3> vertices = new ArrayList<>();
        List<Triangle> triangles = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("v ")) {
                    String[] tokens = line.split("\\s+");
                    double x = Double.parseDouble(tokens[1]);
                    double y = Double.parseDouble(tokens[2]);
                    double z = Double.parseDouble(tokens[3]);
                    vertices.add(new Vector3(x, y, z));
                } else if (line.startsWith("f ")) {
                    String[] tokens = line.split("\\s+");
                    int[] indices = new int[3];
                    for (int i = 1; i <= 3; i++) {
                        String[] parts = tokens[i].split("/");
                        indices[i - 1] = Integer.parseInt(parts[0]) - 1; // OBJ индексация с 1
                    }

                    Vector3 v0 = vertices.get(indices[0]);
                    Vector3 v1 = vertices.get(indices[1]);
                    Vector3 v2 = vertices.get(indices[2]);

                    triangles.add(new Triangle(v0, v1, v2, material));
                }
            }
        } catch (Exception e) {
            System.err.println("Ошибка загрузки OBJ: " + e.getMessage());
        }

        return triangles;
    }
}
