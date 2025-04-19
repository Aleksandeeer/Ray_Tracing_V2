package org.example.math;

import java.util.Random;

public class Vector3 {
    public final double x, y, z;
    private static final Random rand = new Random();

    public Vector3(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3 negate() {
        return new Vector3(-x, -y, -z);
    }

    public Vector3 add(Vector3 v) {
        return new Vector3(x + v.x, y + v.y, z + v.z);
    }

    public Vector3 subtract(Vector3 v) {
        return new Vector3(x - v.x, y - v.y, z - v.z);
    }

    public Vector3 multiply(double scalar) {
        return new Vector3(x * scalar, y * scalar, z * scalar);
    }

    public double dot(Vector3 v) {
        return x * v.x + y * v.y + z * v.z;
    }

    public Vector3 normalize() {
        double len = Math.sqrt(x * x + y * y + z * z);
        return new Vector3(x / len, y / len, z / len);
    }

    public double length() {
        return Math.sqrt(x * x + y * y + z * z);
    }

    public Vector3 cross(Vector3 v) {
        double cx = this.y * v.z - this.z * v.y;
        double cy = this.z * v.x - this.x * v.z;
        double cz = this.x * v.y - this.y * v.x;
        return new Vector3(cx, cy, cz);
    }

    public static Vector3 randomInUnitSphere() {
        while (true) {
            Vector3 p = new Vector3(
                    rand.nextDouble() * 2 - 1,
                    rand.nextDouble() * 2 - 1,
                    rand.nextDouble() * 2 - 1
            );
            if (p.lengthSquared() < 1) return p;
        }
    }

    public static Vector3 randomUnitVector() {
        return randomInUnitSphere().normalize();
    }

    public double lengthSquared() {
        return x * x + y * y + z * z;
    }
}