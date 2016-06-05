package net.ilexiconn.llibrary.server.asm;

import net.ilexiconn.llibrary.server.asm.transformer.*;
import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@IFMLLoadingPlugin.Name("llibrary")
@IFMLLoadingPlugin.MCVersion("1.8.9")
@IFMLLoadingPlugin.SortingIndex(1001)
@IFMLLoadingPlugin.TransformerExclusions("net.ilexiconn.llibrary.server.asm.")
public class LLibraryPlugin implements IFMLLoadingPlugin, IClassTransformer {
    private List<ITransformer> transformerList = new ArrayList<>();

    public LLibraryPlugin() {
        this.transformerList.add(new ServerTransformer());
        this.transformerList.add(new LocaleTransformer());
        this.transformerList.add(new RenderPlayerTransformer());
        this.transformerList.add(new ModelPlayerTransformer());
    }

    @Override
    public String[] getASMTransformerClass() {
        return new String[]{this.getClass().getName()};
    }

    @Override
    public String getModContainerClass() {
        return null;
    }

    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) {

    }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }

    @Override
    public byte[] transform(String name, String transformedName, byte[] bytes) {
        for (ITransformer transformer : this.transformerList) {
            if (transformer.getTarget().equals(transformedName)) {
                ClassReader classReader = new ClassReader(bytes);
                ClassNode classNode = new ClassNode();
                classReader.accept(classNode, 0);
                transformer.transform(classNode, transformedName.equals(name));
                ClassWriter classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_MAXS);
                classNode.accept(classWriter);
                this.saveBytecode(transformedName, classWriter);
                bytes = classWriter.toByteArray();
            }
        }
        return bytes;
    }

    private void saveBytecode(String name, ClassWriter classWriter) {
        try {
            File debugDir = new File("llibrary/debug/");
            if (debugDir.exists()) {
                debugDir.delete();
            }
            debugDir.mkdirs();
            FileOutputStream out = new FileOutputStream(new File(debugDir, name + ".class"));
            out.write(classWriter.toByteArray());
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
