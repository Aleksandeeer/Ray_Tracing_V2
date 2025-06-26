package org.example.objects.triangles;

import org.example.math.Vector2;
import org.example.math.Vector3;

public class ObjTriangle {
    public Vector3 v0, v1, v2;
    public Vector3 n0, n1, n2;
    public Vector2 t0, t1, t2;

    public ObjTriangle(Vector3 v0, Vector3 v1, Vector3 v2,
                    Vector3 n0, Vector3 n1, Vector3 n2,
                    Vector2 t0, Vector2 t1, Vector2 t2) {
        this.v0 = v0;
        this.v1 = v1;
        this.v2 = v2;
        this.n0 = n0;
        this.n1 = n1;
        this.n2 = n2;
        this.t0 = t0;
        this.t1 = t1;
        this.t2 = t2;
    }
}
