package net.ilexiconn.llibrary.client.model.qubble;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author iLexiconn
 * @since 1.3.0
 */
public class QubbleCube implements INBTSerializable<NBTTagCompound> {
    private String name;
    private List<QubbleCube> children = new ArrayList<>();
    private int dimensionX = 1;
    private int dimensionY = 1;
    private int dimensionZ = 1;
    private float positionX;
    private float positionY;
    private float positionZ;
    private float offsetX;
    private float offsetY;
    private float offsetZ;
    private float rotationX;
    private float rotationY;
    private float rotationZ;
    private float scaleX = 1.0F;
    private float scaleY = 1.0F;
    private float scaleZ = 1.0F;
    private int textureX;
    private int textureY;
    private boolean textureMirrored;
    private float opacity;

    private QubbleCube() {
    }

    public static QubbleCube create(String name) {
        QubbleCube cube = new QubbleCube();
        cube.setName(name);
        return cube;
    }

    public static QubbleCube deserialize(NBTTagCompound compound) {
        QubbleCube cube = new QubbleCube();
        cube.deserializeNBT(compound);
        return cube;
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound compound = new NBTTagCompound();
        compound.setString("name", this.name);
        NBTTagList childrenTag = new NBTTagList();
        for (QubbleCube cube : this.children) {
            childrenTag.appendTag(cube.serializeNBT());
        }
        compound.setTag("children", childrenTag);
        if (this.dimensionX != 1 || this.dimensionY != 1 || this.dimensionZ != 1) {
            NBTTagCompound dimensionTag = new NBTTagCompound();
            dimensionTag.setInteger("x", this.dimensionX);
            dimensionTag.setInteger("y", this.dimensionY);
            dimensionTag.setInteger("z", this.dimensionZ);
            compound.setTag("dimension", dimensionTag);
        }
        if (this.positionX != 0.0F || this.positionY != 0.0F || this.positionZ != 0.0F) {
            NBTTagCompound positionTag = new NBTTagCompound();
            positionTag.setFloat("x", this.positionX);
            positionTag.setFloat("y", this.positionY);
            positionTag.setFloat("z", this.positionZ);
            compound.setTag("position", positionTag);
        }
        if (this.offsetX != 0.0F || this.offsetY != 0.0F || this.offsetZ != 0.0F) {
            NBTTagCompound offsetTag = new NBTTagCompound();
            offsetTag.setFloat("x", this.offsetX);
            offsetTag.setFloat("y", this.offsetY);
            offsetTag.setFloat("z", this.offsetZ);
            compound.setTag("offset", offsetTag);
        }
        if (this.rotationX != 0.0F || this.rotationY != 0.0F || this.rotationZ != 0.0F) {
            NBTTagCompound rotationTag = new NBTTagCompound();
            rotationTag.setFloat("x", this.rotationX);
            rotationTag.setFloat("y", this.rotationY);
            rotationTag.setFloat("z", this.rotationZ);
            compound.setTag("rotation", rotationTag);
        }
        if (this.scaleX != 1.0F || this.scaleY != 1.0F || this.scaleZ != 1.0F) {
            NBTTagCompound scaleTag = new NBTTagCompound();
            scaleTag.setFloat("x", this.scaleX);
            scaleTag.setFloat("y", this.scaleY);
            scaleTag.setFloat("z", this.scaleZ);
            compound.setTag("scale", scaleTag);
        }
        if (this.textureX != 0 || this.textureY != 0 || this.textureMirrored) {
            NBTTagCompound textureTag = new NBTTagCompound();
            textureTag.setInteger("x", this.textureX);
            textureTag.setInteger("y", this.textureY);
            textureTag.setBoolean("mirrored", this.textureMirrored);
            compound.setTag("texture", textureTag);
        }
        compound.setFloat("opacity", this.opacity);
        return compound;
    }

    @Override
    public void deserializeNBT(NBTTagCompound compound) {
        this.name = compound.getString("name");
        this.children = new ArrayList<>();
        NBTTagList childrenTag = compound.getTagList("children", Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < childrenTag.tagCount(); i++) {
            QubbleCube cube = new QubbleCube();
            cube.deserializeNBT(childrenTag.getCompoundTagAt(i));
            this.children.add(cube);
        }
        if (compound.hasKey("dimension")) {
            NBTTagCompound dimensionTag = compound.getCompoundTag("dimension");
            this.dimensionX = dimensionTag.getInteger("x");
            this.dimensionY = dimensionTag.getInteger("y");
            this.dimensionZ = dimensionTag.getInteger("z");
        }
        if (compound.hasKey("position")) {
            NBTTagCompound positionTag = compound.getCompoundTag("position");
            this.positionX = positionTag.getFloat("x");
            this.positionY = positionTag.getFloat("y");
            this.positionZ = positionTag.getFloat("z");
        }
        if (compound.hasKey("offset")) {
            NBTTagCompound offsetTag = compound.getCompoundTag("offset");
            this.offsetX = offsetTag.getFloat("x");
            this.offsetY = offsetTag.getFloat("y");
            this.offsetZ = offsetTag.getFloat("z");
        }
        if (compound.hasKey("rotation")) {
            NBTTagCompound rotationTag = compound.getCompoundTag("rotation");
            this.rotationX = rotationTag.getFloat("x");
            this.rotationY = rotationTag.getFloat("y");
            this.rotationZ = rotationTag.getFloat("z");
        }
        if (compound.hasKey("scale")) {
            NBTTagCompound scaleTag = compound.getCompoundTag("scale");
            this.scaleX = scaleTag.getFloat("x");
            this.scaleY = scaleTag.getFloat("y");
            this.scaleZ = scaleTag.getFloat("z");
        }
        if (compound.hasKey("texture")) {
            NBTTagCompound textureTag = compound.getCompoundTag("texture");
            this.textureX = textureTag.getInteger("x");
            this.textureY = textureTag.getInteger("y");
            this.textureMirrored = textureTag.getBoolean("mirrored");
        }
        this.opacity = compound.getFloat("opacity");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<QubbleCube> getChildren() {
        return children;
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

    public float getPositionX() {
        return positionX;
    }

    public float getPositionY() {
        return positionY;
    }

    public float getPositionZ() {
        return positionZ;
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

    public float getRotationX() {
        return rotationX;
    }

    public float getRotationY() {
        return rotationY;
    }

    public float getRotationZ() {
        return rotationZ;
    }

    public float getScaleX() {
        return scaleX;
    }

    public float getScaleY() {
        return scaleY;
    }

    public float getScaleZ() {
        return scaleZ;
    }

    public int getTextureX() {
        return textureX;
    }

    public int getTextureY() {
        return textureY;
    }

    public boolean isTextureMirrored() {
        return textureMirrored;
    }

    public void setTextureMirrored(boolean textureMirrored) {
        this.textureMirrored = textureMirrored;
    }

    public float getOpacity() {
        return opacity;
    }

    public void setOpacity(float opacity) {
        this.opacity = opacity;
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

    public void setScale(float x, float y, float z) {
        this.scaleX = x;
        this.scaleY = y;
        this.scaleZ = z;
    }

    public QubbleCube copy() {
        QubbleCube cube = QubbleCube.create(this.getName());
        cube.getChildren().addAll(this.getChildren().stream().map(QubbleCube::copy).collect(Collectors.toList()));
        cube.setDimensions(this.getDimensionX(), this.getDimensionY(), this.getDimensionZ());
        cube.setPosition(this.getPositionX(), this.getPositionY(), this.getPositionZ());
        cube.setOffset(this.getOffsetX(), this.getOffsetY(), this.getOffsetZ());
        cube.setRotation(this.getRotationX(), this.getRotationY(), this.getRotationZ());
        cube.setScale(this.getScaleX(), this.getScaleY(), this.getScaleZ());
        cube.setTexture(this.getTextureX(), this.getTextureY());
        cube.setTextureMirrored(this.isTextureMirrored());
        cube.setOpacity(this.getOpacity());
        return cube;
    }
}
