package org.example.math;

public class RefractionUtil {

    public static Vector3 reflect(Vector3 v, Vector3 n) {
        return v.subtract(n.multiply(2 * v.dot(n)));
    }

    public static Vector3 refract(Vector3 uv, Vector3 n, double etaRatio) {
        double cosTheta = Math.min(n.negate().dot(uv), 1.0);
        Vector3 rOutPerp = uv.add(n.multiply(cosTheta)).multiply(etaRatio);
        Vector3 rOutParallel = n.multiply(-Math.sqrt(Math.abs(1.0 - rOutPerp.dot(rOutPerp))));
        return rOutPerp.add(rOutParallel);
    }

    public static double schlickReflectance(double cosine, double refIdx) {
        // Approximation of reflectance using Schlick's approximation
        double r0 = (1 - refIdx) / (1 + refIdx);
        r0 = r0 * r0;
        return r0 + (1 - r0) * Math.pow((1 - cosine), 5);
    }
}
