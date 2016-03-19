package net.ilexiconn.llibrary.server;

import net.ilexiconn.llibrary.server.network.AbstractMessage;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.server.FMLServerHandler;

public class ServerProxy {
    public static final ServerEventHandler SERVER_EVENT_HANDLER = new ServerEventHandler();

    public void onPreInit() {
        MinecraftForge.EVENT_BUS.register(ServerProxy.SERVER_EVENT_HANDLER);
    }

    public void onInit() {

    }

    public void onPostInit() {

    }

    public <MESSAGE extends AbstractMessage<MESSAGE>> void handleMessage(final MESSAGE message, final MessageContext messageContext) {
        WorldServer world = (WorldServer) messageContext.getServerHandler().playerEntity.worldObj;
        world.addScheduledTask(new Runnable() {
            @Override
            public void run() {
                message.onServerReceived(FMLServerHandler.instance().getServer(), message, messageContext.getServerHandler().playerEntity, messageContext);
            }
        });
    }
}
