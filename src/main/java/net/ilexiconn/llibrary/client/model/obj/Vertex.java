package net.ilexiconn.llibrary.client.model.obj;

import org.lwjgl.util.vector.Vector3f;

import java.util.Locale;

/**
 * @author iLexiconn
 * @since 1.3.0
 */
public class Vertex {
    private Vector3f position;
    private int index;

    public Vertex(float x, float y, float z) {
        this(new Vector3f(x, y, z));
    }

    public Vertex(Vector3f position) {
        this.position = position;
    }

    public void register(OBJModel model) {
        this.index = model.getVertexIndex();
    }

    public Vector3f getPosition() {
        return position;
    }

    public int getIndex() {
        return index;
    }

    @Override
    public String toString() {
        return "v " + String.format(Locale.US, "%.6f", position.x) + " " + String.format(Locale.US, "%.6f", position.y) + " " + String.format(Locale.US, "%.6f", position.z);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Vertex) {
            Vertex vertex = (Vertex) obj;
            return vertex.position.x == position.x && vertex.position.y == position.y && vertex.position.z == position.z;
        }
        return false;
    }
}
