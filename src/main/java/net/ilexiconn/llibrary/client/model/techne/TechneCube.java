package net.ilexiconn.llibrary.client.model.techne;

/**
 * @author iLexiconn
 * @since 1.3.0
 */
public class TechneCube {
    private String name;
    private float offsetX;
    private float offsetY;
    private float offsetZ;
    private int dimensionX;
    private int dimensionY;
    private int dimensionZ;
    private boolean textureMirrored;
    private int textureX;
    private int textureY;
    private float rotationX;
    private float rotationY;
    private float rotationZ;
    private float positionX;
    private float positionY;
    private float positionZ;

    private TechneCube() {

    }

    public static TechneCube create(String name) {
        TechneCube cube = new TechneCube();
        cube.setName(name);
        return cube;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getOffsetX() {
        return offsetX;
    }

    public float getOffsetY() {
        return offsetY;
    }

    public float getOffsetZ() {
        return offsetZ;
    }

    public int getDimensionX() {
        return dimensionX;
    }

    public int getDimensionY() {
        return dimensionY;
    }

    public int getDimensionZ() {
        return dimensionZ;
    }

    public boolean isTextureMirrored() {
        return textureMirrored;
    }

    public void setTextureMirrored(boolean textureMirrored) {
        this.textureMirrored = textureMirrored;
    }

    public int getTextureX() {
        return textureX;
    }

    public int getTextureY() {
        return textureY;
    }

    public float getRotationX() {
        return rotationX;
    }

    public float getRotationY() {
        return rotationY;
    }

    public float getRotationZ() {
        return rotationZ;
    }

    public float getPositionX() {
        return positionX;
    }

    public float getPositionY() {
        return positionY;
    }

    public float getPositionZ() {
        return positionZ;
    }

    public void setTexture(int x, int y) {
        this.textureX = x;
        this.textureY = y;
    }

    public void setPosition(float x, float y, float z) {
        this.positionX = x;
        this.positionY = y;
        this.positionZ = z;
    }

    public void setOffset(float x, float y, float z) {
        this.offsetX = x;
        this.offsetY = y;
        this.offsetZ = z;
    }

    public void setDimensions(int x, int y, int z) {
        this.dimensionX = x;
        this.dimensionY = y;
        this.dimensionZ = z;
    }

    public void setRotation(float x, float y, float z) {
        this.rotationX = x;
        this.rotationY = y;
        this.rotationZ = z;
    }
}
