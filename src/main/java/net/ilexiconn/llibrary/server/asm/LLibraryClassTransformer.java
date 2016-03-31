package net.ilexiconn.llibrary.server.asm;

import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class LLibraryClassTransformer implements IClassTransformer {
    private static final String RENDER_PLAYER = "net.minecraft.client.renderer.entity.RenderPlayer";
    private static final String MODEL_BIPED = "net.minecraft.client.model.ModelBiped";

    private Map<String, String> obfMappings = new HashMap<>();
    private Map<String, String> mappings = new HashMap<>();

    public LLibraryClassTransformer() {
        obfMappings.put(RENDER_PLAYER, "bop");
        obfMappings.put(MODEL_BIPED, "bhm");
        obfMappings.put("renderFirstPersonArm", "a");
        obfMappings.put("setRotationAngles", "a");
        obfMappings.put("render", "a");
        obfMappings.put("net/minecraft/entity/player/EntityPlayer", "yz");
        obfMappings.put("net/minecraft/entity/Entity", "sa");
        for (Map.Entry<String, String> entry : obfMappings.entrySet()) {
            mappings.put(entry.getKey(), entry.getKey());
        }
    }

    @Override
    public byte[] transform(String name, String transformedName, byte[] bytes) {
        if (name.equals(obfMappings.get(RENDER_PLAYER))) {
            return transformRenderPlayer(bytes, name, obfMappings);
        } else if (name.equals(mappings.get(RENDER_PLAYER))) {
            return transformRenderPlayer(bytes, name, mappings);
        } else if (name.equals(obfMappings.get(MODEL_BIPED))) {
            return transformModelPlayer(bytes, name, obfMappings);
        } else if (name.equals(mappings.get(MODEL_BIPED))) {
            return transformModelPlayer(bytes, name, mappings);
        }
        return bytes;
    }

    private byte[] transformRenderPlayer(byte[] bytes, String name, Map<String, String> mappings) {
        ClassReader cr = new ClassReader(bytes);
        ClassNode classNode = new ClassNode();
        cr.accept(classNode, 0);
        for (MethodNode methodNode : classNode.methods) {
            if (methodNode.name.equals("renderFirstPersonArm") && methodNode.desc.equals("(L" + mappings.get("net/minecraft/entity/player/EntityPlayer") + ";)V")) {
                String desc = "(L" + mappings.get("net/minecraft/entity/player/EntityPlayer") + ";L" + mappings.get(RENDER_PLAYER).replaceAll("\\.", "/") + ";)";
                InsnList inject = new InsnList();
                LabelNode label = new LabelNode();
                inject.add(new VarInsnNode(Opcodes.ALOAD, 1));
                inject.add(new VarInsnNode(Opcodes.ALOAD, 0));
                inject.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "net/ilexiconn/llibrary/server/asm/LLibraryASMHandler", "renderArmPre", desc + "Z", false));
                inject.add(new JumpInsnNode(Opcodes.IFEQ, label));
                InsnNode returnNode = new InsnNode(Opcodes.RETURN);
                inject.add(returnNode);
                inject.add(label);
                for (AbstractInsnNode node : methodNode.instructions.toArray()) {
                    if (node.getOpcode() == Opcodes.RETURN && node != returnNode) {
                        inject.add(new VarInsnNode(Opcodes.ALOAD, 1));
                        inject.add(new VarInsnNode(Opcodes.ALOAD, 0));
                        inject.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "net/ilexiconn/llibrary/server/asm/LLibraryASMHandler", "renderArmPost", desc + "V", false));
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

    private byte[] transformModelPlayer(byte[] bytes, String name, Map<String, String> mappings) {
        ClassReader cr = new ClassReader(bytes);
        ClassNode classNode = new ClassNode();
        cr.accept(classNode, 0);
        for (MethodNode methodNode : classNode.methods) {
            if (methodNode.name.equals(mappings.get("setRotationAngles")) && methodNode.desc.equals("(FFFFFFL" + mappings.get("net/minecraft/entity/Entity") + ";)V")) {
                String desc = "(L" + mappings.get(MODEL_BIPED).replaceAll("\\.", "/") + ";L" + mappings.get("net/minecraft/entity/Entity") + ";FFFFFF)V";
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
            } else if (methodNode.name.equals(mappings.get("render")) && methodNode.desc.equals("(L" + mappings.get("net/minecraft/entity/Entity") + ";FFFFFF)V")) {
                String desc = "(L" + mappings.get(MODEL_BIPED).replaceAll("\\.", "/") + ";L" + mappings.get("net/minecraft/entity/Entity") + ";FFFFFF)V";
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
                String desc = "(L" + mappings.get(MODEL_BIPED).replaceAll("\\.", "/") + ";)V";
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
