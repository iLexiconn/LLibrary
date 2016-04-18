package net.ilexiconn.llibrary.client.model.obj;

import org.lwjgl.util.vector.Vector2f;

import java.util.Locale;

/**
 * @author iLexiconn
 * @since 1.3.0
 */
public class TextureCoords {
    private Vector2f uvCoords;
    private int index;

    public TextureCoords(float u, float v) {
        this(new Vector2f(u, v));
    }

    public TextureCoords(Vector2f uvCoords) {
        this.uvCoords = uvCoords;
    }

    public void register(OBJModel model) {
        this.index = model.getUVIndex();
    }

    public int getIndex() {
        return index;
    }

    @Override
    public String toString() {
        return "vt " + String.format(Locale.US, "%.6f", uvCoords.x) + " " + String.format(Locale.US, "%.6f", uvCoords.y);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof TextureCoords) {
            TextureCoords uv = (TextureCoords) obj;
            return uv.uvCoords.x == uvCoords.x && uv.uvCoords.y == uvCoords.y;
        }
        return false;
    }
}
