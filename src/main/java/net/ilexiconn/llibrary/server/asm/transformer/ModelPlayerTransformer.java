package net.ilexiconn.llibrary.server.asm.transformer;

import net.ilexiconn.llibrary.client.event.PlayerModelEvent;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import org.objectweb.asm.tree.*;

public class ModelPlayerTransformer implements ITransformer {
    @Override
    public String getTarget() {
        return "net.minecraft.client.model.ModelPlayer";
    }

    @Override
    public void transform(ClassNode node, boolean dev) {
        for (MethodNode methodNode : node.methods) {
            if (methodNode.name.equals("setRotationAngles") || methodNode.name.equals("func_78087_a")) {
                InsnList insnList = methodNode.instructions;
                for (AbstractInsnNode insnNode : insnList.toArray()) {
                    if (insnNode.getOpcode() == RETURN) {
                        insnList.insertBefore(insnNode, new VarInsnNode(ALOAD, 0));
                        insnList.insertBefore(insnNode, new VarInsnNode(ALOAD, 7));
                        insnList.insertBefore(insnNode, new VarInsnNode(FLOAD, 1));
                        insnList.insertBefore(insnNode, new VarInsnNode(FLOAD, 2));
                        insnList.insertBefore(insnNode, new VarInsnNode(FLOAD, 3));
                        insnList.insertBefore(insnNode, new VarInsnNode(FLOAD, 4));
                        insnList.insertBefore(insnNode, new VarInsnNode(FLOAD, 5));
                        insnList.insertBefore(insnNode, new VarInsnNode(FLOAD, 6));
                        insnList.insertBefore(insnNode, new MethodInsnNode(INVOKESTATIC, "net/ilexiconn/llibrary/server/asm/transformer/ModelPlayerTransformer", "setRotationAngles", "(Lnet/minecraft/client/model/ModelPlayer;Lnet/minecraft/entity/Entity;FFFFFF)V", false));
                    }
                }
            } else if (methodNode.name.equals("render") || methodNode.name.equals("func_78088_a")) {
                InsnList insnList = methodNode.instructions;
                for (AbstractInsnNode insnNode : insnList.toArray()) {
                    if (insnNode.getOpcode() == RETURN) {
                        insnList.insertBefore(insnNode, new VarInsnNode(ALOAD, 0));
                        insnList.insertBefore(insnNode, new VarInsnNode(ALOAD, 1));
                        insnList.insertBefore(insnNode, new VarInsnNode(FLOAD, 2));
                        insnList.insertBefore(insnNode, new VarInsnNode(FLOAD, 3));
                        insnList.insertBefore(insnNode, new VarInsnNode(FLOAD, 4));
                        insnList.insertBefore(insnNode, new VarInsnNode(FLOAD, 5));
                        insnList.insertBefore(insnNode, new VarInsnNode(FLOAD, 6));
                        insnList.insertBefore(insnNode, new VarInsnNode(FLOAD, 7));
                        insnList.insertBefore(insnNode, new MethodInsnNode(INVOKESTATIC, "net/ilexiconn/llibrary/server/asm/transformer/ModelPlayerTransformer", "renderModel", "(Lnet/minecraft/client/model/ModelPlayer;Lnet/minecraft/entity/Entity;FFFFFF)V", false));
                    }
                }
            } else if (methodNode.name.equals("<init>")) {
                InsnList insnList = methodNode.instructions;
                for (AbstractInsnNode insnNode : insnList.toArray()) {
                    if (insnNode.getOpcode() == RETURN) {
                        insnList.insertBefore(insnNode, new VarInsnNode(ALOAD, 0));
                        insnList.insertBefore(insnNode, new MethodInsnNode(INVOKESTATIC, "net/ilexiconn/llibrary/server/asm/transformer/ModelPlayerTransformer", "constructModel", "(Lnet/minecraft/client/model/ModelPlayer;)V", false));
                    }
                }
            }
        }
    }

    @SuppressWarnings("unused")
    public static void setRotationAngles(ModelPlayer model, Entity entity, float limbSwing, float limbSwingAmount, float rotation, float rotationYaw, float rotationPitch, float scale) {
        MinecraftForge.EVENT_BUS.post(new PlayerModelEvent.SetRotationAngles(model, (EntityPlayer) entity, limbSwing, limbSwingAmount, rotation, rotationYaw, rotationPitch, scale));
    }

    @SuppressWarnings("unused")
    public static void renderModel(ModelPlayer model, Entity entity, float limbSwing, float limbSwingAmount, float rotation, float rotationYaw, float rotationPitch, float scale) {
        MinecraftForge.EVENT_BUS.post(new PlayerModelEvent.Render(model, (EntityPlayer) entity, limbSwing, limbSwingAmount, rotation, rotationYaw, rotationPitch, scale));
    }

    @SuppressWarnings("unused")
    public static void constructModel(ModelPlayer model) {
        MinecraftForge.EVENT_BUS.post(new PlayerModelEvent.Construct(model));
    }
}
