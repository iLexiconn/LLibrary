package net.ilexiconn.llibrary.server.asm;

import net.minecraftforge.fml.relauncher.FMLRelaunchLog;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.*;
import java.util.function.Consumer;

public class ClassPatcher {
    private static final Map<String, String> PRIMITIVE_NAMES = new HashMap<>();

    static {
        PRIMITIVE_NAMES.put("void", "V");
        PRIMITIVE_NAMES.put("boolean", "Z");
        PRIMITIVE_NAMES.put("char", "C");
        PRIMITIVE_NAMES.put("byte", "B");
        PRIMITIVE_NAMES.put("short", "S");
        PRIMITIVE_NAMES.put("int", "I");
        PRIMITIVE_NAMES.put("float", "F");
        PRIMITIVE_NAMES.put("long", "J");
        PRIMITIVE_NAMES.put("double", "D");
    }

    private String cls;
    private Map<String, MethodPatcher> patcherMap = new HashMap<>();
    private Map<String, Consumer<Method>> creationMap = new HashMap<>();
    private List<String> removeList = new ArrayList<>();

    public ClassPatcher(String cls) {
        this.cls = cls;
    }

    void handlePatches(ClassNode classNode) {
        FMLRelaunchLog.info("Patching class " + this.cls);
        for (Map.Entry<String, Consumer<Method>> entry : this.creationMap.entrySet()) {
            FMLRelaunchLog.info("   Adding method " + entry.getKey());
            String method = entry.getKey().substring(0, entry.getKey().indexOf("("));
            String desc = entry.getKey().substring(method.length());
            MethodNode methodNode = new MethodNode(Opcodes.ACC_PUBLIC, method, desc, null, null);
            Method m = new Method(this, null);
            entry.getValue().accept(m);
            methodNode.instructions.add(m.insnList);
            classNode.methods.add(methodNode);
        }
        for (MethodNode methodNode : new ArrayList<>(classNode.methods)) {
            String method = methodNode.name + methodNode.desc;
            if (this.removeList.contains(method)) {
                FMLRelaunchLog.info("   Removing method " + method);
                classNode.methods.remove(methodNode);
                continue;
            }
            MethodPatcher patcher = this.patcherMap.get(method);
            if (patcher != null) {
                patcher.handlePatches(methodNode);
            }
        }
    }

    public MethodPatcher patchMethod(String method, Object... params) {
        String desc = MappingHandler.INSTANCE.getClassMapping(this.methodDesc(params));
        method = MappingHandler.INSTANCE.getMethodMapping(this.cls, method, desc) + desc;
        MethodPatcher patcher = new MethodPatcher(this, this.cls, method);
        this.patcherMap.put(method, patcher);
        return patcher;
    }

    public ClassPatcher createMethod(String method, Object[] params, Consumer<Method> consumer) {
        String desc = MappingHandler.INSTANCE.getClassMapping(this.methodDesc(params));
        method = MappingHandler.INSTANCE.getMethodMapping(this.cls, method, desc) + desc;
        this.creationMap.put(method, consumer);
        return this;
    }

    public ClassPatcher removeMethod(String method, Object... params) {
        String desc = MappingHandler.INSTANCE.getClassMapping(this.methodDesc(params));
        method = MappingHandler.INSTANCE.getMethodMapping(this.cls, method, desc) + desc;
        this.removeList.add(method);
        return this;
    }

    String methodDesc(Object... params) {
        if (params.length == 0) {
            return "";
        }
        StringBuilder builder = new StringBuilder("(");
        for (int i = 0; i < params.length - 1; i++) {
            Object obj = params[i];
            if (obj instanceof Integer) {
                String desc = this.fieldDesc(params[++i]);
                for (int j = 0, k = (int) obj; j < k; j++) {
                    builder.append(desc);
                }
            } else {
                builder.append(this.fieldDesc(obj));
            }
        }
        builder.append(")").append(this.fieldDesc(params[params.length - 1]));
        return builder.toString();
    }

    String fieldDesc(Object obj) {
        String result = "";
        String suffix = "";
        if (obj instanceof String) {
            String cls = MappingHandler.INSTANCE.getClassMapping((String) obj);
            result = PRIMITIVE_NAMES.get(cls);
            if (result == null) {
                result = "L" + cls;
                suffix = ";";
            }
        } else if (obj instanceof Class) {
            Class cls = (Class) obj;
            result = PRIMITIVE_NAMES.get(cls.getName());
            if (result == null) {
                if (!cls.isArray()) {
                    return "L" + cls.getName() + ";";
                } else {
                    return cls.getName();
                }
            }
        }
        while (result.endsWith("[]")) {
            result = "[" + result.substring(0, result.length() - 2);
        }
        return result + suffix;
    }

    @Override
    public String toString() {
        return "class:" + this.cls + this.patcherMap.values();
    }
}
