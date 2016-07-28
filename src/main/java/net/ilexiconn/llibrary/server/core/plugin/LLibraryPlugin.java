package net.ilexiconn.llibrary.server.core.plugin;

<<<<<<< HEAD
import cpw.mods.fml.relauncher.IFMLLoadingPlugin;
=======
import net.ilexiconn.llibrary.server.asm.MappingHandler;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import org.apache.commons.compress.utils.IOUtils;
>>>>>>> a513c66... ASM cleanup

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

<<<<<<< HEAD
@IFMLLoadingPlugin.Name("llibrary-core")
@IFMLLoadingPlugin.MCVersion("1.7.10")
=======
@IFMLLoadingPlugin.Name("llibrary")
@IFMLLoadingPlugin.MCVersion("1.10.2")
@IFMLLoadingPlugin.SortingIndex(1001)
>>>>>>> a513c66... ASM cleanup
@IFMLLoadingPlugin.TransformerExclusions("net.ilexiconn.llibrary.server.asm")
public class LLibraryPlugin implements IFMLLoadingPlugin {
    public static boolean inDevelopment;

    @Override
    public String[] getASMTransformerClass() {
        return new String[]{"net.ilexiconn.llibrary.server.core.plugin.LLibraryTransformer", "net.ilexiconn.llibrary.server.core.patcher.LLibraryRuntimePatcher"};
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
        LLibraryPlugin.inDevelopment = !(Boolean) data.get("runtimeDeobfuscationEnabled");
        InputStream stream = null;
        try {
            stream = this.getClass().getResourceAsStream("/llibrary.mappings");
            MappingHandler.INSTANCE.parseMappings(stream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            IOUtils.closeQuietly(stream);
        }
    }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }
}
