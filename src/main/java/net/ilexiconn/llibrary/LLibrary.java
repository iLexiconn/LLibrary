package net.ilexiconn.llibrary;

import net.ilexiconn.llibrary.client.lang.LanguageHandler;
import net.ilexiconn.llibrary.client.util.ItemTESRContext;
import net.ilexiconn.llibrary.server.ServerProxy;
import net.ilexiconn.llibrary.server.capability.IEntityDataCapability;
import net.ilexiconn.llibrary.server.config.ConfigHandler;
import net.ilexiconn.llibrary.server.config.LLibraryConfig;
import net.ilexiconn.llibrary.server.core.api.LLibraryCoreAPI;
import net.ilexiconn.llibrary.server.core.plugin.LLibraryPlugin;
import net.ilexiconn.llibrary.server.network.AnimationMessage;
import net.ilexiconn.llibrary.server.network.BlockEntityMessage;
import net.ilexiconn.llibrary.server.network.NetworkHandler;
import net.ilexiconn.llibrary.server.network.NetworkWrapper;
import net.ilexiconn.llibrary.server.network.PropertiesMessage;
import net.ilexiconn.llibrary.server.network.SnackbarMessage;
import net.ilexiconn.llibrary.server.network.SurvivalTabMessage;
import net.ilexiconn.llibrary.server.world.TickRateHandler;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLFingerprintViolationEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import java.io.File;
import java.util.Map;

@Mod(
        modid = "llibrary",
        name = "LLibrary",
        version = LLibrary.VERSION,
        acceptedMinecraftVersions = "1.12.2",
        certificateFingerprint = "${fingerprint}",
        guiFactory = "net.ilexiconn.llibrary.client.gui.LLibraryGUIFactory",
        updateJSON = "https://gist.githubusercontent.com/gegy1000/a6639456aeb8edd92cbf7cbfcf9d65d9/raw/llibrary_updates.json",
        dependencies = "required-after:forge@[14.23.5.2772,)"
)
public class LLibrary {
    public static final String VERSION = "1.7.16";

    public static final Logger LOGGER = LogManager.getLogger("LLibrary");
    @SidedProxy(serverSide = "net.ilexiconn.llibrary.server.ServerProxy", clientSide = "net.ilexiconn.llibrary.client.ClientProxy")
    public static ServerProxy PROXY;
    @Mod.Instance("llibrary")
    public static LLibrary INSTANCE;
    @CapabilityInject(IEntityDataCapability.class)
    public static Capability<IEntityDataCapability> ENTITY_DATA_CAPABILITY;
    public static LLibraryConfig CONFIG = new LLibraryConfig();
    @NetworkWrapper({ AnimationMessage.class, PropertiesMessage.class, SnackbarMessage.class, BlockEntityMessage.class, SurvivalTabMessage.class })
    public static SimpleNetworkWrapper NETWORK_WRAPPER;
    public static int QUBBLE_VERSION = 1;
    public static int QUBBLE_VANILLA_VERSION = 1;

    public static final File LLIBRARY_ROOT = new File(".", "llibrary");

    static {
        try {
            LLibraryPlugin.api = new CoreAPIHandler();
        } catch (Throwable e) {
            LOGGER.error("Failed to load LLibrary Core API. Is it missing?", e);
        }
    }

    @Mod.EventHandler
    public void onPreInit(FMLPreInitializationEvent event) {
        if (!LLibrary.LLIBRARY_ROOT.exists()) {
            LLibrary.LLIBRARY_ROOT.mkdirs();
        }

        for (ModContainer mod : Loader.instance().getModList()) {
            ConfigHandler.INSTANCE.injectConfig(mod, event.getAsmData());
            NetworkHandler.INSTANCE.injectNetworkWrapper(mod, event.getAsmData());
        }

        LLibrary.CONFIG.load();
        LLibrary.PROXY.onPreInit();
    }

    @Mod.EventHandler
    public void onInit(FMLInitializationEvent event) {
        LLibrary.PROXY.onInit();
    }

    @Mod.EventHandler
    public void onPostInit(FMLPostInitializationEvent event) {
        LLibrary.PROXY.onPostInit();
    }

    @Mod.EventHandler
    public void onFingerprintViolation(FMLFingerprintViolationEvent event) {
        LOGGER.warn("Detected invalid fingerprint for file {}! You will not receive support with this tampered version of llibrary!", event.getSource().getName());
    }

    static class CoreAPIHandler implements LLibraryCoreAPI {
        @Override
        @SideOnly(Side.CLIENT)
        public void addRemoteLocalizations(String language, Map<String, String> properties) {
            LanguageHandler.INSTANCE.addRemoteLocalizations(language, properties);
        }

        @Override
        @SideOnly(Side.CLIENT)
        public void provideStackContext(@Nonnull ItemStack stack) {
            ItemTESRContext.INSTANCE.provideStackContext(stack);
        }

        @Override
        @SideOnly(Side.CLIENT)
        public void providePerspectiveContext(@Nonnull ItemCameraTransforms.TransformType transform) {
            ItemTESRContext.INSTANCE.providePerspectiveContext(transform);
        }

        @Override
        public long getTickRate() {
            return TickRateHandler.INSTANCE.getTickRate();
        }
    }
}
