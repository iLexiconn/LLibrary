package net.ilexiconn.llibrary.client.model;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class Transform {
    public float rotX, rotY, rotZ;
    public float offsetX, offsetY, offsetZ;

    public void addRotation(float rotX, float rotY, float rotZ) {
        this.rotX += rotX;
        this.rotY += rotY;
        this.rotZ += rotZ;
    }

    public void addOffset(float offsetX, float offsetY, float offsetZ) {
        this.offsetX += offsetX;
        this.offsetY += offsetY;
        this.offsetZ += offsetZ;
    }
}
