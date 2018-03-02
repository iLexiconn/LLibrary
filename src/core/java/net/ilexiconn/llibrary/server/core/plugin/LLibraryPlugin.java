package net.ilexiconn.llibrary.server.core.plugin;

import net.ilexiconn.llibrary.server.asm.MappingHandler;
import net.ilexiconn.llibrary.server.core.api.LLibraryCoreAPI;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

@IFMLLoadingPlugin.Name("llibrary")
@IFMLLoadingPlugin.MCVersion("1.12.2")
@IFMLLoadingPlugin.SortingIndex(1001)
@IFMLLoadingPlugin.TransformerExclusions("net.ilexiconn.llibrary.server.asm")
public class LLibraryPlugin implements IFMLLoadingPlugin {
    public static final Logger LOGGER = LogManager.getLogger("LLibrary Core");

    public static LLibraryCoreAPI api;

    public static boolean inDevelopment;

    @Override
    public String[] getASMTransformerClass() {
        return new String[] { "net.ilexiconn.llibrary.server.core.plugin.LLibraryTransformer", "net.ilexiconn.llibrary.server.core.patcher.LLibraryRuntimePatcher" };
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
        try (InputStream input = this.getClass().getResourceAsStream("/llibrary.mappings")) {
            MappingHandler.INSTANCE.parseMappings(input);
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse LLibrary mappings", e);
        }
    }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }
}
