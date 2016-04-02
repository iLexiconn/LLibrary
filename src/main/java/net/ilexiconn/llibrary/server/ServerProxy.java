package net.ilexiconn.llibrary.server;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.server.FMLServerHandler;
import net.ilexiconn.llibrary.LLibrary;
import net.ilexiconn.llibrary.client.lang.LanguageHandler;
import net.ilexiconn.llibrary.server.network.AbstractMessage;
import net.ilexiconn.llibrary.server.network.AnimationMessage;
import net.ilexiconn.llibrary.server.network.PropertiesMessage;
import net.ilexiconn.llibrary.server.network.SnackbarMessage;
import net.ilexiconn.llibrary.server.snackbar.Snackbar;
import net.ilexiconn.llibrary.server.update.UpdateHandler;
import net.minecraftforge.common.MinecraftForge;

public class ServerProxy {
    public void onPreInit() {
        MinecraftForge.EVENT_BUS.register(ServerEventHandler.INSTANCE);
        FMLCommonHandler.instance().bus().register(ServerEventHandler.INSTANCE);

        LLibrary.NETWORK_WRAPPER.registerMessage(AnimationMessage.class, AnimationMessage.class, 0, Side.CLIENT);
        LLibrary.NETWORK_WRAPPER.registerMessage(SnackbarMessage.class, SnackbarMessage.class, 1, Side.CLIENT);
        LLibrary.NETWORK_WRAPPER.registerMessage(PropertiesMessage.class, PropertiesMessage.class, 2, Side.CLIENT);

        LanguageHandler.INSTANCE.load();
    }

    public void onInit() {
        UpdateHandler.INSTANCE.registerUpdateChecker(LLibrary.INSTANCE, "http://pastebin.com/raw/4WCeUU9p");
    }

    public void onPostInit() {
        UpdateHandler.INSTANCE.searchForUpdates();
    }

    public <T extends AbstractMessage<T>> void handleMessage(final T message, final MessageContext messageContext) {
        message.onServerReceived(FMLServerHandler.instance().getServer(), message, messageContext.getServerHandler().playerEntity, messageContext);
    }

    public float getPartialTicks() {
        return 0.0F;
    }

    public void showSnackbar(Snackbar snackbar) {
        LLibrary.NETWORK_WRAPPER.sendToAll(new SnackbarMessage(snackbar));
    }
}
