package net.ilexiconn.llibrary.server.asm;

import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LLibraryClassTransformer implements IClassTransformer {
    private static final String RENDER_PLAYER = "net.minecraft.client.renderer.entity.RenderPlayer";
    private static final String MODEL_PLAYER = "net.minecraft.client.model.ModelPlayer";

    private Map<String, String> mappings = new HashMap<>();

    public LLibraryClassTransformer() {
        this.mappings.put(RENDER_PLAYER, "buk");
        this.mappings.put(MODEL_PLAYER, "bjf");
        this.mappings.put("renderLeftArm", "c");
        this.mappings.put("renderRightArm", "b");
        this.mappings.put("setRotationAngles", "a");
        this.mappings.put("render", "a");
        this.mappings.put("net/minecraft/client/entity/AbstractClientPlayer", "bmq");
        this.mappings.put("net/minecraft/entity/Entity", "rr");
        this.mappings.put("getMainModel", "b");
        this.mappings.put("net/minecraft/client/renderer/entity/RenderManager", "brm");
        this.mappings.put("net/minecraft/client/model/ModelBase", "bjc");
        this.mappings.put("mainModel", "g");
        this.mappings.put("rotateAngleX", "f");
    }

    public String getMappingFor(String name) {
        if (LLibraryPlugin.isObfuscated()) {
            return this.mappings.get(name);
        } else {
            return name;
        }
    }

    @Override
    public byte[] transform(String name, String transformedName, byte[] bytes) {
        if (name.equals(this.getMappingFor(RENDER_PLAYER))) {
            return transformRenderPlayer(bytes, name);
        } else if (name.equals(this.getMappingFor(MODEL_PLAYER))) {
            return transformModelPlayer(bytes, name);
        }
        return bytes;
    }

    private byte[] transformRenderPlayer(byte[] bytes, String name) {
        ClassReader cr = new ClassReader(bytes);
        ClassNode classNode = new ClassNode();
        cr.accept(classNode, 0);
        for (MethodNode methodNode : classNode.methods) {
            boolean leftArm = methodNode.name.equals(this.getMappingFor("renderLeftArm"));
            boolean rightArm = methodNode.name.equals(this.getMappingFor("renderRightArm"));
            String renderPlayerFriendlyName = this.getMappingFor(RENDER_PLAYER).replaceAll("\\.", "/");
            if ((leftArm || rightArm) && methodNode.desc.equals("(L" + this.getMappingFor("net/minecraft/client/entity/AbstractClientPlayer") + ";)V")) {
                String prefix = "render" + (leftArm ? "Left" : "Right") + "Arm";
                String desc = "(L" + this.getMappingFor("net/minecraft/client/entity/AbstractClientPlayer") + ";L" + renderPlayerFriendlyName + ";)";
                InsnList inject = new InsnList();
                InsnNode returnNode = new InsnNode(Opcodes.RETURN);
                AbstractInsnNode setRotationAngles = null;
                boolean next = false;
                List<AbstractInsnNode> nodesForLabel = new ArrayList<>();
                List<List<AbstractInsnNode>> rotateAngleAssignments = new ArrayList<>();
                for (AbstractInsnNode node : methodNode.instructions.toArray()) {
                    if (node.getOpcode() == Opcodes.RETURN && node != returnNode) {
                        inject.add(new VarInsnNode(Opcodes.ALOAD, 1));
                        inject.add(new VarInsnNode(Opcodes.ALOAD, 0));
                        inject.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "net/ilexiconn/llibrary/server/asm/LLibraryASMHandler", prefix + "Post", desc + "V", false));
                    } else if (node.getOpcode() == Opcodes.INVOKEVIRTUAL && ((MethodInsnNode) node).desc.equals("(FFFFFFL" + this.getMappingFor("net/minecraft/entity/Entity") + ";)V")) {
                        next = true;
                        setRotationAngles = node;
                    } else if (next) {
                        LabelNode label = new LabelNode();
                        inject.add(new VarInsnNode(Opcodes.ALOAD, 1));
                        inject.add(new VarInsnNode(Opcodes.ALOAD, 0));
                        inject.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "net/ilexiconn/llibrary/server/asm/LLibraryASMHandler", prefix + "Pre", desc + "Z", false));
                        inject.add(new JumpInsnNode(Opcodes.IFEQ, label));
                        inject.add(returnNode);
                        inject.add(label);
                        next = false;
                    }
                    if (node instanceof LabelNode) {
                        for (AbstractInsnNode nodeForLabel : nodesForLabel) {
                            if (nodeForLabel.getOpcode() == Opcodes.PUTFIELD && ((FieldInsnNode) nodeForLabel).name.equals(this.getMappingFor("rotateAngleX"))) {
                                rotateAngleAssignments.add(new ArrayList<>(nodesForLabel));
                                inject.remove(nodeForLabel);
                                break;
                            }
                        }
                        nodesForLabel.clear();
                    } else {
                        nodesForLabel.add(node);
                    }
                    inject.add(node);
                }
                methodNode.instructions.clear();
                methodNode.instructions.add(inject);
                if (setRotationAngles != null) {
                    for (List<AbstractInsnNode> assignment : rotateAngleAssignments) {
                        InsnList list = new InsnList();
                        assignment.stream().filter(node -> !(node instanceof LineNumberNode)).forEach(node -> list.add(node.clone(new HashMap<>())));
                        methodNode.instructions.insertBefore(setRotationAngles.getNext(), list);
                    }
                }
            } else if (methodNode.name.equals("<init>") && methodNode.desc.equals("(L" + this.getMappingFor("net/minecraft/client/renderer/entity/RenderManager;Z)V"))) {
                String modelPlayerFriendlyName = this.getMappingFor(MODEL_PLAYER).replaceAll("\\.", "/");
                String desc = "(L" + renderPlayerFriendlyName + ";L" + modelPlayerFriendlyName + ";Z)L" + modelPlayerFriendlyName + ";";
                InsnList inject = new InsnList();
                for (AbstractInsnNode node : methodNode.instructions.toArray()) {
                    if (node.getOpcode() == Opcodes.RETURN) {
                        inject.add(new VarInsnNode(Opcodes.ALOAD, 0));
                        inject.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, renderPlayerFriendlyName, this.getMappingFor("getMainModel"), "()L" + modelPlayerFriendlyName + ";", false));
                        inject.add(new VarInsnNode(Opcodes.ASTORE, 4));
                        inject.add(new VarInsnNode(Opcodes.ALOAD, 0));
                        inject.add(new VarInsnNode(Opcodes.ALOAD, 4));
                        inject.add(new VarInsnNode(Opcodes.ILOAD, 2));
                        inject.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "net/ilexiconn/llibrary/server/asm/LLibraryASMHandler", "assign", desc, false));
                        inject.add(new VarInsnNode(Opcodes.ASTORE, 5));
                        inject.add(new VarInsnNode(Opcodes.ALOAD, 0));
                        inject.add(new VarInsnNode(Opcodes.ALOAD, 5));
                        inject.add(new FieldInsnNode(Opcodes.PUTFIELD, renderPlayerFriendlyName, this.getMappingFor("mainModel"), "L" + this.getMappingFor("net/minecraft/client/model/ModelBase") + ";"));
                    }
                    inject.add(node);
                }
                methodNode.instructions.clear();
                methodNode.instructions.add(inject);
            }
        }
        ClassWriter cw = new ClassWriter(cr, ClassWriter.COMPUTE_MAXS);
        classNode.accept(cw);
        saveBytecode(name, cw);
        return cw.toByteArray();
    }

    private byte[] transformModelPlayer(byte[] bytes, String name) {
        ClassReader cr = new ClassReader(bytes);
        ClassNode classNode = new ClassNode();
        cr.accept(classNode, 0);
        for (MethodNode methodNode : classNode.methods) {
            if (methodNode.name.equals(this.getMappingFor("setRotationAngles")) && methodNode.desc.equals("(FFFFFFL" + this.getMappingFor("net/minecraft/entity/Entity") + ";)V")) {
                String desc = "(L" + this.getMappingFor(MODEL_PLAYER).replaceAll("\\.", "/") + ";L" + this.getMappingFor("net/minecraft/entity/Entity") + ";FFFFFF)V";
                InsnList inject = new InsnList();
                for (AbstractInsnNode node : methodNode.instructions.toArray()) {
                    if (node.getOpcode() == Opcodes.RETURN) {
                        inject.add(new VarInsnNode(Opcodes.ALOAD, 0));
                        inject.add(new VarInsnNode(Opcodes.ALOAD, 7));
                        inject.add(new VarInsnNode(Opcodes.FLOAD, 1));
                        inject.add(new VarInsnNode(Opcodes.FLOAD, 2));
                        inject.add(new VarInsnNode(Opcodes.FLOAD, 3));
                        inject.add(new VarInsnNode(Opcodes.FLOAD, 4));
                        inject.add(new VarInsnNode(Opcodes.FLOAD, 5));
                        inject.add(new VarInsnNode(Opcodes.FLOAD, 6));
                        inject.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "net/ilexiconn/llibrary/server/asm/LLibraryASMHandler", "setRotationAngles", desc, false));
                    }
                    inject.add(node);
                }
                methodNode.instructions.clear();
                methodNode.instructions.add(inject);
            } else if (methodNode.name.equals(this.getMappingFor("render")) && methodNode.desc.equals("(L" + this.getMappingFor("net/minecraft/entity/Entity") + ";FFFFFF)V")) {
                String desc = "(L" + this.getMappingFor(MODEL_PLAYER).replaceAll("\\.", "/") + ";L" + this.getMappingFor("net/minecraft/entity/Entity") + ";FFFFFF)V";
                InsnList inject = new InsnList();
                for (AbstractInsnNode node : methodNode.instructions.toArray()) {
                    if (node.getOpcode() == Opcodes.RETURN) {
                        inject.add(new VarInsnNode(Opcodes.ALOAD, 0));
                        inject.add(new VarInsnNode(Opcodes.ALOAD, 1));
                        inject.add(new VarInsnNode(Opcodes.FLOAD, 2));
                        inject.add(new VarInsnNode(Opcodes.FLOAD, 3));
                        inject.add(new VarInsnNode(Opcodes.FLOAD, 4));
                        inject.add(new VarInsnNode(Opcodes.FLOAD, 5));
                        inject.add(new VarInsnNode(Opcodes.FLOAD, 6));
                        inject.add(new VarInsnNode(Opcodes.FLOAD, 7));
                        inject.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "net/ilexiconn/llibrary/server/asm/LLibraryASMHandler", "renderModel", desc, false));
                    }
                    inject.add(node);
                }
                methodNode.instructions.clear();
                methodNode.instructions.add(inject);
            } else if (methodNode.name.equals("<init>")) {
                String desc = "(L" + this.getMappingFor(MODEL_PLAYER).replaceAll("\\.", "/") + ";)V";
                InsnList inject = new InsnList();
                for (AbstractInsnNode node : methodNode.instructions.toArray()) {
                    if (node.getOpcode() == Opcodes.RETURN) {
                        inject.add(new VarInsnNode(Opcodes.ALOAD, 0));
                        inject.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "net/ilexiconn/llibrary/server/asm/LLibraryASMHandler", "constructModel", desc, false));
                    }
                    inject.add(node);
                }
                methodNode.instructions.clear();
                methodNode.instructions.add(inject);
            }
        }
        ClassWriter cw = new ClassWriter(cr, ClassWriter.COMPUTE_MAXS);
        classNode.accept(cw);
        saveBytecode(name, cw);
        return cw.toByteArray();
    }

    private void saveBytecode(String name, ClassWriter cw) {
        try {
            File debugDir = new File("debug/");
            debugDir.mkdirs();
            FileOutputStream out = new FileOutputStream(new File(debugDir, name + ".class"));
            out.write(cw.toByteArray());
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
