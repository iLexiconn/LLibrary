package net.ilexiconn.llibrary.server.asm;

import net.ilexiconn.llibrary.server.core.plugin.LLibraryPlugin;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class MethodPatcher {
    private ClassPatcher patcher;
    private String cls;
    private String method;

    private List<PatchData> patches = new ArrayList<>();
    private List<PostProcessor> postProcessors = new ArrayList<>();

    public MethodPatcher(ClassPatcher patcher, String cls, String method) {
        this.patcher = patcher;
        this.cls = cls;
        this.method = method;
    }

    public ClassPatcher pop() {
        return this.patcher;
    }

    void handlePatches(MethodNode methodNode) {
        LLibraryPlugin.LOGGER.debug("   Patching method {}", this.method);
        this.patches.stream().filter(patch -> patch.predicate == null).forEach(patch -> {
            Method method = new Method(this.patcher, null);
            patch.consumer.accept(method);
            patch.at.apply(patch, methodNode, null, method);
        });
        for (AbstractInsnNode insnNode : methodNode.instructions.toArray()) {
            this.patches.stream().filter(patch -> patch.predicate != null && patch.predicate.test(new PredicateData(this.patcher, this.cls, insnNode))).forEach(patch -> {
                Method method = new Method(this.patcher, insnNode);
                patch.consumer.accept(method);
                patch.at.apply(patch, methodNode, insnNode, method);
            });
        }
        this.postProcessors.forEach(postProcessor -> postProcessor.process(this.cls, methodNode));
    }

    public MethodPatcher apply(RuntimePatcher.Patch at, Consumer<Method> consumer) {
        return this.apply(at, null, consumer);
    }

    public MethodPatcher apply(RuntimePatcher.Patch at, Predicate<PredicateData> insnType, Consumer<Method> consumer) {
        this.patches.add(new PatchData(at, insnType, consumer));
        return this;
    }

    public MethodPatcher apply(PostProcessor postProcessor) {
        this.postProcessors.add(postProcessor);
        return this;
    }

    public class PatchData {
        public final RuntimePatcher.Patch at;
        public final Predicate<PredicateData> predicate;
        public final Consumer<Method> consumer;

        public PatchData(RuntimePatcher.Patch at, Predicate<PredicateData> predicate, Consumer<Method> consumer) {
            this.at = at;
            this.predicate = predicate;
            this.consumer = consumer;
        }
    }

    public class PredicateData {
        public final ClassPatcher patcher;
        public final String cls;
        public final AbstractInsnNode node;

        public PredicateData(ClassPatcher patcher, String cls, AbstractInsnNode node) {
            this.patcher = patcher;
            this.cls = cls;
            this.node = node;
        }
    }

    @Override
    public String toString() {
        return "method:" + this.method;
    }
}
