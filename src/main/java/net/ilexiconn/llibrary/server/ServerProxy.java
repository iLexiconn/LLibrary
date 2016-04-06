package net.ilexiconn.llibrary.server;

import net.ilexiconn.llibrary.LLibrary;
import net.ilexiconn.llibrary.client.lang.LanguageHandler;
import net.ilexiconn.llibrary.server.network.AbstractMessage;
import net.ilexiconn.llibrary.server.network.SnackbarMessage;
import net.ilexiconn.llibrary.server.snackbar.Snackbar;
import net.ilexiconn.llibrary.server.update.UpdateHandler;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.server.FMLServerHandler;

public class ServerProxy {
    public void onPreInit() {
        MinecraftForge.EVENT_BUS.register(ServerEventHandler.INSTANCE);
        LanguageHandler.INSTANCE.load();
    }

    public void onInit() {
        UpdateHandler.INSTANCE.registerUpdateChecker(LLibrary.INSTANCE, "http://pastebin.com/raw/1RBJqD37");
    }

    public void onPostInit() {
        if (LLibrary.CONFIG.versionCheck) {
            UpdateHandler.INSTANCE.searchForUpdates();
        }
    }

    public <T extends AbstractMessage<T>> void handleMessage(final T message, final MessageContext messageContext) {
        WorldServer world = (WorldServer) messageContext.getServerHandler().playerEntity.worldObj;
        world.addScheduledTask(() -> message.onServerReceived(FMLServerHandler.instance().getServer(), message, messageContext.getServerHandler().playerEntity, messageContext));
    }

    public float getPartialTicks() {
        return 0.0F;
    }

    public void showSnackbar(Snackbar snackbar) {
        LLibrary.NETWORK_WRAPPER.sendToAll(new SnackbarMessage(snackbar));
    }

    public void setTickRate(long tickRate) {
    }
}
