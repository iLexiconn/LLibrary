package net.ilexiconn.llibrary.server;

import net.ilexiconn.llibrary.LLibrary;
import net.ilexiconn.llibrary.server.network.AbstractMessage;
import net.ilexiconn.llibrary.server.network.AnimationMessage;
import net.ilexiconn.llibrary.server.network.SnackbarMessage;
import net.ilexiconn.llibrary.server.snackbar.Snackbar;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.server.FMLServerHandler;

public class ServerProxy {
    public static final ServerEventHandler SERVER_EVENT_HANDLER = new ServerEventHandler();

    public void onPreInit() {
        MinecraftForge.EVENT_BUS.register(ServerProxy.SERVER_EVENT_HANDLER);

        LLibrary.NETWORK_WRAPPER.registerMessage(AnimationMessage.class, AnimationMessage.class, 0, Side.CLIENT);
        LLibrary.NETWORK_WRAPPER.registerMessage(SnackbarMessage.class, SnackbarMessage.class, 1, Side.CLIENT);
    }

    public void onInit() {

    }

    public void onPostInit() {

    }

    public <MESSAGE extends AbstractMessage<MESSAGE>> void handleMessage(final MESSAGE message, final MessageContext messageContext) {
        WorldServer world = (WorldServer) messageContext.getServerHandler().playerEntity.worldObj;
        world.addScheduledTask(() -> message.onServerReceived(FMLServerHandler.instance().getServer(), message, messageContext.getServerHandler().playerEntity, messageContext));
    }

    public float getPartialTicks() {
        return 0.0F;
    }

    public void showSnackbar(Snackbar snackbar) {
        LLibrary.NETWORK_WRAPPER.sendToAll(new SnackbarMessage(snackbar));
    }
}
