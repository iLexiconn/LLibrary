package net.ilexiconn.llibrary.client;

import net.ilexiconn.llibrary.client.gui.SnackbarGUI;
import net.ilexiconn.llibrary.client.model.tabula.baked.BakedTabulaLoader;
import net.ilexiconn.llibrary.server.ServerProxy;
import net.ilexiconn.llibrary.server.network.AbstractMessage;
import net.ilexiconn.llibrary.server.snackbar.Snackbar;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Timer;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

@SideOnly(Side.CLIENT)
public class ClientProxy extends ServerProxy {
    public static final ClientEventHandler CLIENT_EVENT_HANDLER = new ClientEventHandler();
    public static final Minecraft MINECRAFT = Minecraft.getMinecraft();
    public static final int UPDATE_BUTTON_ID = "UPDATE_BUTTON_ID".hashCode();
    public static final List<SnackbarGUI> SNACKBAR_LIST = new ArrayList<>();

    private Timer timer;

    @Override
    public void onPreInit() {
        super.onPreInit();

        MinecraftForge.EVENT_BUS.register(ClientProxy.CLIENT_EVENT_HANDLER);
        ModelLoaderRegistry.registerLoader(BakedTabulaLoader.INSTANCE);

        timer = ReflectionHelper.getPrivateValue(Minecraft.class, MINECRAFT, "timer", "field_71428_T", "aa");
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
            ClientProxy.MINECRAFT.addScheduledTask(() -> message.onClientReceived(MINECRAFT, message, MINECRAFT.thePlayer, messageContext));
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
