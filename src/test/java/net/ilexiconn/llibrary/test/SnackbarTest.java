package net.ilexiconn.llibrary.test;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.ilexiconn.llibrary.server.snackbar.Snackbar;
import net.ilexiconn.llibrary.server.snackbar.SnackbarHandler;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;

@Mod(modid = "SnackbarTest")
public class SnackbarTest {
    @SidedProxy(serverSide = "net.ilexiconn.llibrary.test.SnackbarTest$ServerProxy", clientSide = "net.ilexiconn.llibrary.test.SnackbarTest$ClientProxy")
    public static ServerProxy PROXY;

    @Mod.EventHandler
    public void onPreInit(FMLPreInitializationEvent event) {
        SnackbarTest.PROXY.onPreInit();
    }

    public static class ServerProxy {
        public void onPreInit() {

        }
    }

    @SideOnly(Side.CLIENT)
    public static class ClientProxy extends ServerProxy {
        public static final ClientEventHandler CLIENT_EVENT_HANDLER = new ClientEventHandler();
        public static final KeyBinding SNACKBAR_KEY = new KeyBinding("snackbar", Keyboard.KEY_U, "key.llibrary.debug");

        @Override
        public void onPreInit() {
            super.onPreInit();
            ClientRegistry.registerKeyBinding(ClientProxy.SNACKBAR_KEY);
            FMLCommonHandler.instance().bus().register(ClientProxy.CLIENT_EVENT_HANDLER);
        }
    }

    @SideOnly(Side.CLIENT)
    public static class ClientEventHandler {
        @SubscribeEvent
        public void onKeyInputPost(InputEvent.KeyInputEvent event) {
            if (Keyboard.isKeyDown(ClientProxy.SNACKBAR_KEY.getKeyCode())) {
                SnackbarHandler.INSTANCE.showSnackbar(Snackbar.create("Snackbar test"));
            }
        }
    }
}
