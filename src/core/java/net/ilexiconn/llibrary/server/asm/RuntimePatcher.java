package net.ilexiconn.llibrary.server.asm;

import net.ilexiconn.llibrary.server.core.patcher.LLibraryRuntimePatcher;
import net.ilexiconn.llibrary.server.core.plugin.LLibraryPlugin;
import net.minecraft.launchwrapper.IClassTransformer;
import org.apache.commons.io.IOUtils;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

/**
 * Class to patch other classes at runtime. Return a class extending this class as class transformer in your coremod.
 *
 * NOTES:
 *  Do NOT place the patcher in the same directory or in a subdirectory of the plugin. The plugin should also have a
 *  sorting index >1001, or LLibrary will be unable to call the patcher properly.
 *  Also make sure to put 'RuntimePatcher' in the name of the class.
 *
 * @see LLibraryRuntimePatcher
 * @since 1.5.0
 * @author iLexiconn
 */
public abstract class RuntimePatcher implements IClassTransformer, Opcodes {
    private Map<String, ClassPatcher> patcherMap = new HashMap<>();

    public RuntimePatcher() {
        // Do not implement the annotation manually. It will be injected by LLibraryTransformer
        if (this.getClass().getAnnotation(Transformed.class) == null) {
            throw new RuntimeException("RuntimePatcher has not been transformed and cannot be loaded.");
        }
    }

    public abstract void onInit();

    @Override
    public byte[] transform(String name, String transformedName, byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        if (this.patcherMap.isEmpty()) {
            this.onInit();
        }
        byte[] prev = bytes;
        bytes = this.handlePatches(bytes, MappingHandler.INSTANCE.getClassMapping(transformedName));
        if (prev != bytes) {
            this.saveBytecode(transformedName, bytes);
        }
        return bytes;
    }

    private byte[] handlePatches(byte[] bytes, String cls) {
        ClassPatcher patcher = this.patcherMap.get(cls);
        if (patcher != null) {
            ClassReader classReader = new ClassReader(bytes);
            ClassNode classNode = new ClassNode();
            classReader.accept(classNode, 0);
            patcher.handlePatches(classNode);
            ClassWriter classWriter = new PatchClassWriter(classReader, ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
            classNode.accept(classWriter);
            return classWriter.toByteArray();
        }
        return bytes;
    }

    /**
     * Create a new {@link ClassPatcher} for the given class.
     *
     * @param obj the {@link Class} object or the full name of the target class.
     * @return the new {@link ClassPatcher} instance.
     */
    public ClassPatcher patchClass(Object obj) {
        ClassPatcher patcher = null;
        if (obj instanceof String) {
            String cls = MappingHandler.INSTANCE.getClassMapping((String) obj);
            patcher = new ClassPatcher(cls);
            this.patcherMap.put(cls, patcher);
        }
        return patcher;
    }

    /**
     * Set the place for the patch. You can use your custom predicate, this is just a helper method.
     *
     * @param at the place
     * @param args optional arguments for the location, see {@link At}
     * @return the predicate of the location
     */
    public Predicate<MethodPatcher.PredicateData> at(At at, Object... args) {
        return at.getPredicate(args);
    }

    private void saveBytecode(String name, byte[] bytes) {
        File debugDir = new File("llibrary/debug/");
        if (!debugDir.exists()) {
            debugDir.mkdirs();
        }
        File outputFile = new File(debugDir, name + ".class");
        try (FileOutputStream out = new FileOutputStream(outputFile)) {
            IOUtils.write(bytes, out);
        } catch (IOException e) {
            LLibraryPlugin.LOGGER.error("Failed to save debug patched class for {}", name, e);
        }
    }

    /**
     * Patch locations.
     */
    public enum At {
        /**
         * At a method call
         * Arguments: String method, Object... desc
         */
        METHOD {
            @Override
            public Predicate<MethodPatcher.PredicateData> getPredicate(Object... args) {
                return new Predicate<MethodPatcher.PredicateData>() {
                    private String mappedName;
                    private String mappedDesc;

                    @Override
                    public boolean test(MethodPatcher.PredicateData predicateData) {
                        if (predicateData.node instanceof MethodInsnNode) {
                            MethodInsnNode methodNode = (MethodInsnNode) predicateData.node;
                            if (this.mappedDesc == null) {
                                this.mappedDesc = MappingHandler.INSTANCE.getClassMapping(Descriptors.method(Arrays.copyOfRange(args, 1, args.length)));
                            }
                            if (this.mappedName == null) {
                                this.mappedName = MappingHandler.INSTANCE.getMethodMapping(predicateData.cls, (String) args[0], this.mappedDesc);
                            }
                            return methodNode.name.equals(this.mappedName) && (this.mappedDesc.isEmpty() || methodNode.desc.equals(this.mappedDesc));
                        }
                        return false;
                    }
                };
            }
        },
        /**
         * At all the returns
         * Arguments: none
         */
        RETURN {
            @Override
            public Predicate<MethodPatcher.PredicateData> getPredicate(Object... args) {
                return predicateData -> {
                    int opcode = predicateData.node.getOpcode();
                    return opcode == Opcodes.RETURN || opcode == Opcodes.IRETURN || opcode == Opcodes.LRETURN || opcode == Opcodes.FRETURN || opcode == Opcodes.DRETURN || opcode == Opcodes.ARETURN;
                };
            }
        },
        /**
         * At a LDC node
         * Arguments: Object value
         */
        LDC {
            @Override
            public Predicate<MethodPatcher.PredicateData> getPredicate(Object... args) {
                return predicateData -> {
                    if (predicateData.node instanceof LdcInsnNode) {
                        LdcInsnNode ldcNode = (LdcInsnNode) predicateData.node;
                        if (ldcNode.cst.equals(args[0])) {
                            return true;
                        }
                    }
                    return false;
                };
            }
        },
        /**
         * At a node
         * Arguments: int opcode
         */
        NODE {
            @Override
            public Predicate<MethodPatcher.PredicateData> getPredicate(Object... args) {
                return predicateData -> predicateData.node.getOpcode() == (int) args[0];
            }
        };

        public abstract Predicate<MethodPatcher.PredicateData> getPredicate(Object... args);
    }

    public enum Patch {
        BEFORE {
            @Override
            public void apply(MethodPatcher.PatchData patch, MethodNode methodNode, AbstractInsnNode location, Method method) {
                methodNode.instructions.insertBefore(location, method.insnList);
            }
        },
        AFTER {
            @Override
            public void apply(MethodPatcher.PatchData patch, MethodNode methodNode, AbstractInsnNode location, Method method) {
                methodNode.instructions.insert(location, method.insnList);
            }
        },
        REPLACE {
            @Override
            public void apply(MethodPatcher.PatchData patch, MethodNode methodNode, AbstractInsnNode location, Method method) {
                methodNode.instructions.clear();
                methodNode.instructions.add(method.insnList);
            }
        },
        REPLACE_NODE {
            @Override
            public void apply(MethodPatcher.PatchData patch, MethodNode methodNode, AbstractInsnNode location, Method method) {
                methodNode.instructions.insertBefore(location, method.insnList);
                methodNode.instructions.remove(location);
            }
        };

        public abstract void apply(MethodPatcher.PatchData patch, MethodNode methodNode, AbstractInsnNode location, Method method);
    }

    @Override
    public String toString() {
        return "runtimePatcher:" + this.getClass().getSimpleName() + this.patcherMap.values();
    }
}
