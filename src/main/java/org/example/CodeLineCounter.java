package org.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CodeLineCounter {

    public static long countLines(Path path) throws IOException {
        List<Path> javaFiles;
        try (Stream<Path> paths = Files.walk(path)) {
            javaFiles = paths
                    .filter(p -> p.toString().endsWith(".java"))
                    .toList();
        }

        long totalLines = 0;

        for (Path file : javaFiles) {
            System.out.println("Файл: " + file.toString());
            try (Stream<String> lines = Files.lines(file)) {
                long count = lines
                        .map(String::trim)
                        .filter(line -> !line.isEmpty() && !line.startsWith("//"))
                        .count();
                totalLines += count;
            } catch (IOException e) {
                System.err.println("Не удалось прочитать файл: " + file);
            }
        }

        return totalLines;
    }

    public static void main(String[] args) throws IOException {
        Path projectDir = Paths.get("src");
        long lines = countLines(projectDir);
        System.out.println("Количество строк кода: " + lines);
    }
}
