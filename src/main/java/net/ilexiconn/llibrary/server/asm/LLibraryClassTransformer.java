package net.ilexiconn.llibrary.server.asm;

import net.minecraft.client.resources.Locale;
import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
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
    private static final String LOCALE = "net.minecraft.client.resources.Locale";

    private Map<String, String> mappings = new HashMap<>();

    public LLibraryClassTransformer() {
        this.mappings.put(RENDER_PLAYER, "bop");
        this.mappings.put(MODEL_BIPED, "bhm");
        this.mappings.put(LOCALE, "brs");
        this.mappings.put("renderFirstPersonArm", "a");
        this.mappings.put("setRotationAngles", "a");
        this.mappings.put("render", "a");
        this.mappings.put("net/minecraft/entity/player/EntityPlayer", "yz");
        this.mappings.put("net/minecraft/entity/Entity", "sa");
        this.mappings.put("net/minecraft/client/model/ModelBase", "bhr");
        this.mappings.put("mainModel", "i");
        this.mappings.put("net/minecraft/client/model/ModelRenderer", "bix");
        this.mappings.put("rotateAngleX", "f");
        this.mappings.put("modelBipedMain", "f");
        this.mappings.put("net/minecraft/client/resources/IResourceManager", "bqy");
        this.mappings.put("loadLocaleDataFiles", "a");
        this.mappings.put("field_135032_a", "a");
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
        } else if (name.equals(this.getMappingFor(MODEL_BIPED))) {
            return transformModelBiped(bytes, name);
        } else if (name.equals(this.getMappingFor(LOCALE))) {
            return transformLocale(bytes, name);
        }
        return bytes;
    }

    private byte[] transformLocale(byte[] bytes, String name) {
        ClassReader cr = new ClassReader(bytes);
        ClassNode classNode = new ClassNode();
        cr.accept(classNode, 0);
        for (MethodNode methodNode : classNode.methods) {
            if (methodNode.name.equals(this.getMappingFor("loadLocaleDataFiles")) && methodNode.desc.equals("(L" + this.getMappingFor("net/minecraft/client/resources/IResourceManager") + ";Ljava/util/List;)V")) {
                for (AbstractInsnNode node : methodNode.instructions.toArray()) {
                    if (node.getOpcode() == Opcodes.INVOKESTATIC && ((MethodInsnNode) node).name.equals("format")) {
                        InsnList insert = new InsnList();
                        insert.add(new FieldInsnNode(Opcodes.GETSTATIC, "net/ilexiconn/llibrary/client/lang/LanguageHandler", "INSTANCE", "Lnet/ilexiconn/llibrary/client/lang/LanguageHandler;"));
                        insert.add(new VarInsnNode(Opcodes.ALOAD, 4));
                        insert.add(new VarInsnNode(Opcodes.ALOAD, 0));
                        insert.add(new FieldInsnNode(Opcodes.GETFIELD, this.getMappingFor(LOCALE).replaceAll("\\.", "/"), this.getMappingFor("field_135032_a"), "Ljava/util/Map;"));
                        insert.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/ilexiconn/llibrary/client/lang/LanguageHandler", "addRemoteLocalizations", "(Ljava/lang/String;Ljava/util/Map;)V", false));
                        methodNode.instructions.insertBefore(node, insert);
                    }
                }
            }
        }
        ClassWriter cw = new ClassWriter(cr, ClassWriter.COMPUTE_MAXS);
        classNode.accept(cw);
        saveBytecode(name, cw);
        return cw.toByteArray();
    }

    private byte[] transformRenderPlayer(byte[] bytes, String name) {
        ClassReader cr = new ClassReader(bytes);
        ClassNode classNode = new ClassNode();
        cr.accept(classNode, 0);
        for (MethodNode methodNode : classNode.methods) {
            String renderPlayerFriendlyName = this.getMappingFor(RENDER_PLAYER).replaceAll("\\.", "/");
            if (methodNode.name.equals(this.getMappingFor("renderFirstPersonArm")) && methodNode.desc.equals("(L" + this.getMappingFor("net/minecraft/entity/player/EntityPlayer") + ";)V")) {
                String desc = "(L" + this.getMappingFor("net/minecraft/entity/player/EntityPlayer") + ";L" + renderPlayerFriendlyName + ";)";
                InsnList inject = new InsnList();
                LabelNode label = new LabelNode();
                InsnNode returnNode = new InsnNode(Opcodes.RETURN);
                AbstractInsnNode setRotationAngles = null;
                for (AbstractInsnNode node : methodNode.instructions.toArray()) {
                    if (node.getOpcode() == Opcodes.RETURN && node != returnNode) {
                        inject.add(new VarInsnNode(Opcodes.ALOAD, 1));
                        inject.add(new VarInsnNode(Opcodes.ALOAD, 0));
                        inject.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "net/ilexiconn/llibrary/server/asm/LLibraryASMHandler", "renderArmPost", desc + "V", false));
                    } else if (node.getOpcode() == Opcodes.INVOKEVIRTUAL && ((MethodInsnNode) node).desc.equals("(FFFFFFL" + this.getMappingFor("net/minecraft/entity/Entity") + ";)V")) {
                        setRotationAngles = node;
                    }
                    inject.add(node);
                }
                methodNode.instructions.clear();
                methodNode.instructions.add(inject);
                if (setRotationAngles != null) {
                    String modelBipedFriendlyName = this.getMappingFor(MODEL_BIPED).replaceAll("\\.", "/");
                    InsnList insert = new InsnList();
                    insert.add(new VarInsnNode(Opcodes.ALOAD, 0));
                    insert.add(new FieldInsnNode(Opcodes.GETFIELD, renderPlayerFriendlyName, this.getMappingFor("modelBipedMain"), "L" + modelBipedFriendlyName + ";"));
                    insert.add(new VarInsnNode(Opcodes.ASTORE, 3));
                    insert.add(new VarInsnNode(Opcodes.ALOAD, 3));
                    insert.add(new FieldInsnNode(Opcodes.GETFIELD, modelBipedFriendlyName, this.getMappingFor("bipedRightArm"), "L" + this.getMappingFor("net/minecraft/client/model/ModelRenderer") + ";"));
                    insert.add(new InsnNode(Opcodes.FCONST_0));
                    insert.add(new FieldInsnNode(Opcodes.PUTFIELD, this.getMappingFor("net/minecraft/client/model/ModelRenderer"), this.getMappingFor("rotateAngleX"), "F"));
                    insert.add(new VarInsnNode(Opcodes.ALOAD, 3));
                    insert.add(new FieldInsnNode(Opcodes.GETFIELD, modelBipedFriendlyName, this.getMappingFor("bipedLeftArm"), "L" + this.getMappingFor("net/minecraft/client/model/ModelRenderer") + ";"));
                    insert.add(new InsnNode(Opcodes.FCONST_0));
                    insert.add(new FieldInsnNode(Opcodes.PUTFIELD, this.getMappingFor("net/minecraft/client/model/ModelRenderer"), this.getMappingFor("rotateAngleX"), "F"));
                    insert.add(new VarInsnNode(Opcodes.ALOAD, 1));
                    insert.add(new VarInsnNode(Opcodes.ALOAD, 0));
                    insert.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "net/ilexiconn/llibrary/server/asm/LLibraryASMHandler", "renderArmPre", desc + "Z", false));
                    insert.add(new JumpInsnNode(Opcodes.IFEQ, label));
                    insert.add(returnNode);
                    insert.add(label);
                    methodNode.instructions.insertBefore(setRotationAngles.getNext(), insert);
                }
            } else if (methodNode.name.equals("<init>")) {
                String modelBipedFriendlyName = this.getMappingFor(MODEL_BIPED).replaceAll("\\.", "/");
                String desc = "(L" + renderPlayerFriendlyName + ";L" + this.getMappingFor("net/minecraft/client/model/ModelBase") + ";)L" + modelBipedFriendlyName + ";";
                InsnList inject = new InsnList();
                for (AbstractInsnNode node : methodNode.instructions.toArray()) {
                    if (node.getOpcode() == Opcodes.INVOKESPECIAL && ((MethodInsnNode) node).desc.equals("(L" + this.getMappingFor("net/minecraft/client/model/ModelBase") + ";F)V")) {
                        inject.add(new VarInsnNode(Opcodes.ALOAD, 0));
                        inject.add(new VarInsnNode(Opcodes.ALOAD, 0));
                        inject.add(new FieldInsnNode(Opcodes.GETFIELD, renderPlayerFriendlyName, this.getMappingFor("mainModel"), "L" + this.getMappingFor("net/minecraft/client/model/ModelBase") + ";"));
                        inject.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "net/ilexiconn/llibrary/server/asm/LLibraryASMHandler", "assign", desc, false));
                        inject.add(new VarInsnNode(Opcodes.ASTORE, 5));
                        inject.add(new VarInsnNode(Opcodes.ALOAD, 0));
                        inject.add(new VarInsnNode(Opcodes.ALOAD, 5));
                        inject.add(new FieldInsnNode(Opcodes.PUTFIELD, renderPlayerFriendlyName, this.getMappingFor("mainModel"), "L" + this.getMappingFor("net/minecraft/client/model/ModelBase") + ";"));
                        methodNode.instructions.insertBefore(node.getNext(), inject);
                    }
                }
            }
        }
        ClassWriter cw = new ClassWriter(cr, ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
        classNode.accept(cw);
        saveBytecode(name, cw);
        return cw.toByteArray();
    }

    private byte[] transformModelBiped(byte[] bytes, String name) {
        ClassReader cr = new ClassReader(bytes);
        ClassNode classNode = new ClassNode();
        cr.accept(classNode, 0);
        for (MethodNode methodNode : classNode.methods) {
            if (methodNode.name.equals(this.getMappingFor("setRotationAngles")) && methodNode.desc.equals("(FFFFFFL" + this.getMappingFor("net/minecraft/entity/Entity") + ";)V")) {
                String desc = "(L" + this.getMappingFor(MODEL_BIPED).replaceAll("\\.", "/") + ";L" + this.getMappingFor("net/minecraft/entity/Entity") + ";FFFFFF)V";
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
                String desc = "(L" + this.getMappingFor(MODEL_BIPED).replaceAll("\\.", "/") + ";L" + this.getMappingFor("net/minecraft/entity/Entity") + ";FFFFFF)V";
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
            } else if (methodNode.name.equals("<init>") && methodNode.desc.equals("(FFII)V")) {
                String desc = "(L" + this.getMappingFor(MODEL_BIPED).replaceAll("\\.", "/") + ";)V";
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
            File debugDir = new File("llibrary/debug/");
            debugDir.mkdirs();
            FileOutputStream out = new FileOutputStream(new File(debugDir, name + ".class"));
            out.write(cw.toByteArray());
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
