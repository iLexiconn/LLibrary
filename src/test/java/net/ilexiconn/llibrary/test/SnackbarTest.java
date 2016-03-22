package net.ilexiconn.llibrary.test;

import net.ilexiconn.llibrary.server.snackbar.Snackbar;
import net.ilexiconn.llibrary.server.snackbar.SnackbarHandler;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

@Mod(modid = "SnackbarTest")
public class SnackbarTest {
    @SidedProxy
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
            MinecraftForge.EVENT_BUS.register(ClientProxy.CLIENT_EVENT_HANDLER);
        }
    }

    @SideOnly(Side.CLIENT)
    public static class ClientEventHandler {
        @SubscribeEvent
        public void onKeyInputPost(GuiScreenEvent.KeyboardInputEvent.Post event) {
            if (Keyboard.isKeyDown(ClientProxy.SNACKBAR_KEY.getKeyCode())) {
                SnackbarHandler.INSTANCE.showSnackbar(Snackbar.create("Snackbar test"));
            }
        }
    }
}
