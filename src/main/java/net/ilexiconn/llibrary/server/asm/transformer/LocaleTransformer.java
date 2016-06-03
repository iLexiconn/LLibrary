package net.ilexiconn.llibrary.server.asm.transformer;

import org.objectweb.asm.tree.*;

public class LocaleTransformer implements ITransformer {
    @Override
    public String getTarget() {
        return "net.minecraft.client.resources.Locale";
    }

    @Override
    public void transform(ClassNode node, boolean dev) {
        for (MethodNode methodNode : node.methods) {
            if (methodNode.name.equals("loadLocaleDataFiles") || methodNode.name.equals("func_135022_a")) {
                InsnList insnList = methodNode.instructions;
                for (AbstractInsnNode insnNode : insnList.toArray()) {
                    if (insnNode instanceof MethodInsnNode && ((MethodInsnNode) insnNode).name.equals("format")) {
                        methodNode.instructions.insertBefore(insnNode, new FieldInsnNode(GETSTATIC, "net/ilexiconn/llibrary/client/lang/LanguageHandler", "INSTANCE", "Lnet/ilexiconn/llibrary/client/lang/LanguageHandler;"));
                        methodNode.instructions.insertBefore(insnNode, new VarInsnNode(ALOAD, 4));
                        methodNode.instructions.insertBefore(insnNode, new VarInsnNode(ALOAD, 0));
                        methodNode.instructions.insertBefore(insnNode, new FieldInsnNode(GETFIELD, "net/minecraft/client/resources/Locale", "field_135032_a", "Ljava/util/Map;"));
                        methodNode.instructions.insertBefore(insnNode, new MethodInsnNode(INVOKEVIRTUAL, "net/ilexiconn/llibrary/client/lang/LanguageHandler", "addRemoteLocalizations", "(Ljava/lang/String;Ljava/util/Map;)V", false));
                        break;
                    }
                }
                return;
            }
        }
    }
}
