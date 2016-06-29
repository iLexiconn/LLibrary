package net.ilexiconn.llibrary.server.asm.transformer;

import org.objectweb.asm.tree.*;

public class ModContainerTransformer implements ITransformer {
    @Override
    public String getTarget() {
        return "net.minecraftforge.fml.common.FMLModContainer";
    }

    @Override
    public void transform(ClassNode node, boolean dev) {
        InsnList inject = new InsnList();
        node.methods.stream().filter(methodNode -> methodNode.name.equals("constructMod")).forEach(methodNode -> {
            for (AbstractInsnNode insnNode : methodNode.instructions.toArray()) {
                inject.add(insnNode);
                if (insnNode.getOpcode() == INVOKESTATIC && ((MethodInsnNode) insnNode).name.equals("inject")) {
                    inject.add(new FieldInsnNode(GETSTATIC, "net/ilexiconn/llibrary/server/config/ConfigHandler", "INSTANCE", "Lnet/ilexiconn/llibrary/server/config/ConfigHandler;"));
                    inject.add(new VarInsnNode(ALOAD, 0));
                    inject.add(new VarInsnNode(ALOAD, 1));
                    inject.add(new MethodInsnNode(INVOKEVIRTUAL, "net/minecraftforge/fml/common/event/FMLConstructionEvent", "getASMHarvestedData", "()Lnet/minecraftforge/fml/common/discovery/ASMDataTable;", false));
                    inject.add(new MethodInsnNode(INVOKEVIRTUAL, "net/ilexiconn/llibrary/server/config/ConfigHandler", "injectConfig", "(Lnet/minecraftforge/fml/common/ModContainer;Lnet/minecraftforge/fml/common/discovery/ASMDataTable;)V", false));
                    inject.add(new FieldInsnNode(GETSTATIC, "net/ilexiconn/llibrary/server/network/NetworkHandler", "INSTANCE", "Lnet/ilexiconn/llibrary/server/network/NetworkHandler;"));
                    inject.add(new VarInsnNode(ALOAD, 0));
                    inject.add(new VarInsnNode(ALOAD, 1));
                    inject.add(new MethodInsnNode(INVOKEVIRTUAL, "net/minecraftforge/fml/common/event/FMLConstructionEvent", "getASMHarvestedData", "()Lnet/minecraftforge/fml/common/discovery/ASMDataTable;", false));
                    inject.add(new MethodInsnNode(INVOKEVIRTUAL, "net/ilexiconn/llibrary/server/network/NetworkHandler", "injectNetworkWrapper", "(Lnet/minecraftforge/fml/common/ModContainer;Lnet/minecraftforge/fml/common/discovery/ASMDataTable;)V", false));
                }
            }
            methodNode.instructions.clear();
            methodNode.instructions.add(inject);
        });
    }
}
