package net.ilexiconn.llibrary.client;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.ReflectionHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.ilexiconn.llibrary.client.gui.SnackbarGUI;
import net.ilexiconn.llibrary.server.ServerProxy;
import net.ilexiconn.llibrary.server.network.AbstractMessage;
import net.ilexiconn.llibrary.server.snackbar.Snackbar;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Timer;
import net.minecraftforge.common.MinecraftForge;

import java.util.ArrayList;
import java.util.List;

@SideOnly(Side.CLIENT)
public class ClientProxy extends ServerProxy {
    public static final Minecraft MINECRAFT = Minecraft.getMinecraft();
    public static final int UPDATE_BUTTON_ID = "UPDATE_BUTTON_ID".hashCode();
    public static final List<SnackbarGUI> SNACKBAR_LIST = new ArrayList<>();

    private Timer timer;

    @Override
    public void onPreInit() {
        super.onPreInit();

        MinecraftForge.EVENT_BUS.register(ClientEventHandler.INSTANCE);
        FMLCommonHandler.instance().bus().register(ClientEventHandler.INSTANCE);

        timer = ReflectionHelper.getPrivateValue(Minecraft.class, ClientProxy.MINECRAFT, "timer", "field_71428_T", "Q");
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
        if (messageContext.side.isServer()) {
            super.handleMessage(message, messageContext);
        } else {
            message.onClientReceived(ClientProxy.MINECRAFT, message, ClientProxy.MINECRAFT.thePlayer, messageContext);
        }
    }

    @Override
    public float getPartialTicks() {
        return timer.renderPartialTicks;
    }

    @Override
    public void showSnackbar(Snackbar snackbar) {
        ClientProxy.SNACKBAR_LIST.add(new SnackbarGUI(snackbar));
    }
}
