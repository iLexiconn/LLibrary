package net.ilexiconn.llibrary.client.model.qubble;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;

/**
 * @author iLexiconn
 * @since 1.3.0
 */
public class QubbleAnimationAction implements INBTSerializable<NBTTagCompound> {
    private String cube;
    private Action action;
    private float valueX;
    private float valueY;
    private float valueZ;

    private QubbleAnimationAction() {

    }

    public static QubbleAnimationAction create(String cube, Action action) {
        QubbleAnimationAction animation = new QubbleAnimationAction();
        animation.setCube(cube);
        animation.setAction(action);
        return animation;
    }

    public static QubbleAnimationAction deserialize(NBTTagCompound compound) {
        QubbleAnimationAction animation = new QubbleAnimationAction();
        animation.deserializeNBT(compound);
        return animation;
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound compound = new NBTTagCompound();
        compound.setString("cube", this.cube);
        compound.setString("action", this.action.name());
        NBTTagCompound valueTag = new NBTTagCompound();
        valueTag.setFloat("x", this.valueX);
        valueTag.setFloat("y", this.valueY);
        valueTag.setFloat("z", this.valueZ);
        compound.setTag("value", valueTag);
        return compound;
    }

    @Override
    public void deserializeNBT(NBTTagCompound compound) {
        this.cube = compound.getString("cube");
        this.action = Action.valueOf(compound.getString("action"));
        NBTTagCompound valueTag = compound.getCompoundTag("value");
        this.valueX = valueTag.getFloat("x");
        this.valueY = valueTag.getFloat("y");
        this.valueZ = valueTag.getFloat("z");
    }

    public String getCube() {
        return cube;
    }

    public void setCube(String cube) {
        this.cube = cube;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public float getValueX() {
        return valueX;
    }

    public float getValueY() {
        return valueY;
    }

    public float getValueZ() {
        return valueZ;
    }

    public void setValue(float x, float y, float z) {
        this.valueX = x;
        this.valueY = y;
        this.valueZ = z;
    }

    public QubbleAnimationAction copy() {
        QubbleAnimationAction animation = QubbleAnimationAction.create(this.getCube(), this.getAction());
        animation.setValue(this.getValueX(), this.getValueY(), this.getValueZ());
        return animation;
    }

    public enum Action {
        ROTATE,
        MOVE
    }
}
