package net.ilexiconn.llibrary.client.util;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.vecmath.*;
import java.util.Stack;

@SideOnly(Side.CLIENT)
public class Matrix {
    public Stack<Matrix4d> matrixStack;

    public Matrix() {
        matrixStack = new Stack<>();
        Matrix4d mat = new Matrix4d();
        mat.setIdentity();
        matrixStack.push(mat);
    }

    public void push() {
        matrixStack.push(new Matrix4d(matrixStack.peek()));
    }

    public void pop() {
        if (matrixStack.size() < 2) {
            throw new StackUnderflowError();
        }
        matrixStack.pop();
    }

    public void translate(double x, double y, double z) {
        Matrix4d mat = matrixStack.peek();
        Matrix4d translation = new Matrix4d();
        translation.setIdentity();
        translation.setTranslation(new Vector3d(x, y, z));
        mat.mul(translation);
    }

    public void rotate(double angle, double x, double y, double z) {
        Matrix4d mat = matrixStack.peek();
        Matrix4d rotation = new Matrix4d();
        rotation.setIdentity();
        rotation.setRotation(new AxisAngle4d(x, y, z, angle * (Math.PI / 180)));
        mat.mul(rotation);
    }

    public void scale(double x, double y, double z) {
        Matrix4d mat = matrixStack.peek();
        Matrix4d scale = new Matrix4d();
        scale.m00 = x;
        scale.m11 = y;
        scale.m22 = z;
        scale.m33 = 1;
        mat.mul(scale);
    }

    public void transform(Point3f point) {
        Matrix4d mat = matrixStack.peek();
        mat.transform(point);
    }

    public void transform(Vector3f point) {
        Matrix4d mat = matrixStack.peek();
        mat.transform(point);
    }

    public Point3f getTranslation() {
        Matrix4d mat = matrixStack.peek();
        Point3f translation = new Point3f();
        mat.transform(translation);
        return translation;
    }

    public Quat4f getRotation() {
        Matrix4d mat = matrixStack.peek();
        Quat4f rotation = new Quat4f();
        mat.get(rotation);
        return rotation;
    }

    public Vector3f getScale() {
        Matrix4d mat = matrixStack.peek();
        float x = (float) Math.sqrt(mat.m00 * mat.m00 + mat.m10 * mat.m10 + mat.m20 * mat.m20);
        float y = (float) Math.sqrt(mat.m01 * mat.m01 + mat.m11 * mat.m11 + mat.m21 * mat.m21);
        float z = (float) Math.sqrt(mat.m02 * mat.m02 + mat.m12 * mat.m12 + mat.m22 * mat.m22);
        return new Vector3f(x, y, z);
    }
}
