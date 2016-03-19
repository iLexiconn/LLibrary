package net.ilexiconn.llibrary.client;

import net.ilexiconn.llibrary.server.ServerProxy;
import net.ilexiconn.llibrary.server.network.AbstractMessage;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientProxy extends ServerProxy {
    public static final ClientEventHandler CLIENT_EVENT_HANDLER = new ClientEventHandler();

    @Override
    public void onPreInit() {
        super.onPreInit();
        MinecraftForge.EVENT_BUS.register(ClientProxy.CLIENT_EVENT_HANDLER);
    }

    @Override
    public void onInit() {
        super.onInit();
    }

    @Override
    public void onPostInit() {
        super.onPostInit();
    }

    @Override
    public <MESSAGE extends AbstractMessage<MESSAGE>> void handleMessage(final MESSAGE message, final MessageContext messageContext) {
        final Minecraft minecraft = Minecraft.getMinecraft();
        minecraft.addScheduledTask(new Runnable() {
            @Override
            public void run() {
                message.onClientReceived(minecraft, message, minecraft.thePlayer, messageContext);
            }
        });
    }
}
