package net.ilexiconn.llibrary.server.core.plugin;

import net.ilexiconn.llibrary.server.asm.PatchClassWriter;
import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodNode;

public class LLibraryTransformer implements IClassTransformer {
    private static final String RUNTIME_PATCHER = "RuntimePatcher";

    @Override
    public byte[] transform(String name, String transformedName, byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        if (!name.startsWith("$") && name.contains(RUNTIME_PATCHER)) {
            LLibraryPlugin.LOGGER.debug("Found runtime patcher {}", name);
            ClassReader classReader = new ClassReader(bytes);
            ClassNode classNode = new ClassNode();
            classReader.accept(classNode, 0);

            for (MethodNode methodNode : classNode.methods) {
                InsnList insnList = methodNode.instructions;
                for (AbstractInsnNode node = insnList.getFirst(); node != null; node = node.getNext()) {
                    if (node.getOpcode() == Opcodes.LDC) {
                        LdcInsnNode ldc = (LdcInsnNode) node;
                        if (ldc.cst instanceof Type) {
                            ldc.cst = ((Type) ldc.cst).getClassName();
                        }
                    }
                }
            }

            classNode.visitAnnotation("Lnet/ilexiconn/llibrary/server/asm/Transformed;", true);

            ClassWriter classWriter = new PatchClassWriter(ClassWriter.COMPUTE_FRAMES);
            classNode.accept(classWriter);
            bytes = classWriter.toByteArray();
        }
        return bytes;
    }
}
