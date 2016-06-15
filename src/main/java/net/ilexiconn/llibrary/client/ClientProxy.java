package net.ilexiconn.llibrary.client;

import com.google.gson.Gson;
import net.ilexiconn.llibrary.client.gui.SnackbarGUI;
import net.ilexiconn.llibrary.client.gui.survivaltab.SurvivalTab;
import net.ilexiconn.llibrary.client.gui.survivaltab.SurvivalTabHandler;
import net.ilexiconn.llibrary.client.lang.LanguageHandler;
import net.ilexiconn.llibrary.client.model.tabula.TabulaModelHandler;
import net.ilexiconn.llibrary.client.render.entity.PartRenderer;
import net.ilexiconn.llibrary.server.ServerProxy;
import net.ilexiconn.llibrary.server.entity.multipart.PartEntity;
import net.ilexiconn.llibrary.server.network.AbstractMessage;
import net.ilexiconn.llibrary.server.snackbar.Snackbar;
import net.ilexiconn.llibrary.server.util.WebUtils;
import net.ilexiconn.llibrary.server.world.TickRateHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.util.Timer;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

@SideOnly(Side.CLIENT)
public class ClientProxy extends ServerProxy {
    public static final Minecraft MINECRAFT = Minecraft.getMinecraft();
    public static final int UPDATE_BUTTON_ID = "UPDATE_BUTTON_ID".hashCode();
    public static final List<SnackbarGUI> SNACKBAR_LIST = new ArrayList<>();
    public static final String[] PATRONS = new Gson().fromJson(WebUtils.readPastebin("aLjMgBAV"), String[].class);
    public static final Timer TIMER = ReflectionHelper.getPrivateValue(Minecraft.class, ClientProxy.MINECRAFT, "timer", "field_71428_T", "aa");
    public static final SurvivalTab INVENTORY_TAB = SurvivalTabHandler.INSTANCE.create("container.inventory", GuiInventory.class);

    @Override
    public void onPreInit() {
        super.onPreInit();
        MinecraftForge.EVENT_BUS.register(ClientEventHandler.INSTANCE);
        ModelLoaderRegistry.registerLoader(TabulaModelHandler.INSTANCE);
        LanguageHandler.INSTANCE.load();
        RenderingRegistry.registerEntityRenderingHandler(PartEntity.class, new PartRenderer.Factory());
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
    public <T extends AbstractMessage<T>> void handleMessage(final T message, final MessageContext messageContext) {
        if (messageContext.side.isServer()) {
            super.handleMessage(message, messageContext);
        } else {
            ClientProxy.MINECRAFT.addScheduledTask(() -> message.onClientReceived(ClientProxy.MINECRAFT, message, ClientProxy.MINECRAFT.thePlayer, messageContext));
        }
    }

    @Override
    public float getPartialTicks() {
        return ClientProxy.TIMER.renderPartialTicks;
    }

    @Override
    public void showSnackbar(Snackbar snackbar) {
        ClientProxy.SNACKBAR_LIST.add(new SnackbarGUI(snackbar));
    }

    @Override
    public void setTickRate(long tickRate) {
        ClientProxy.TIMER.timerSpeed = (float) TickRateHandler.DEFAULT_TICK_RATE / tickRate;
    }
}
