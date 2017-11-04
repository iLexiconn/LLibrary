package net.ilexiconn.llibrary;

import net.ilexiconn.llibrary.server.ServerProxy;
import net.ilexiconn.llibrary.server.capability.IEntityDataCapability;
import net.ilexiconn.llibrary.server.config.ConfigHandler;
import net.ilexiconn.llibrary.server.config.LLibraryConfig;
import net.ilexiconn.llibrary.server.network.AnimationMessage;
import net.ilexiconn.llibrary.server.network.BlockEntityMessage;
import net.ilexiconn.llibrary.server.network.NetworkHandler;
import net.ilexiconn.llibrary.server.network.NetworkWrapper;
import net.ilexiconn.llibrary.server.network.PropertiesMessage;
import net.ilexiconn.llibrary.server.network.SnackbarMessage;
import net.ilexiconn.llibrary.server.network.SurvivalTabMessage;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = "llibrary", name = "LLibrary", version = LLibrary.VERSION, guiFactory = "net.ilexiconn.llibrary.client.gui.LLibraryGUIFactory", updateJSON = "https://gist.github.com/gegy1000/a6639456aeb8edd92cbf7cbfcf9d65d9")
public class LLibrary {
    public static final String VERSION = "1.7.8";
    public static final Logger LOGGER = LogManager.getFormatterLogger("LLibrary");
    @SidedProxy(serverSide = "net.ilexiconn.llibrary.server.ServerProxy", clientSide = "net.ilexiconn.llibrary.client.ClientProxy")
    public static ServerProxy PROXY;
    @Mod.Instance("llibrary")
    public static LLibrary INSTANCE;
    @CapabilityInject(IEntityDataCapability.class)
    public static Capability<IEntityDataCapability> ENTITY_DATA_CAPABILITY;
    public static LLibraryConfig CONFIG = new LLibraryConfig();
    @NetworkWrapper({AnimationMessage.class, PropertiesMessage.class, SnackbarMessage.class, BlockEntityMessage.class, SurvivalTabMessage.class})
    public static SimpleNetworkWrapper NETWORK_WRAPPER;
    public static int QUBBLE_VERSION = 1;
    public static int QUBBLE_VANILLA_VERSION = 1;

    @Mod.EventHandler
    public void onPreInit(FMLPreInitializationEvent event) {
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
}
