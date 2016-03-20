package net.ilexiconn.llibrary.client;

import net.ilexiconn.llibrary.server.ServerProxy;
import net.ilexiconn.llibrary.server.network.AbstractMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.Timer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

@SideOnly(Side.CLIENT)
public class ClientProxy extends ServerProxy {
    public static final ClientEventHandler CLIENT_EVENT_HANDLER = new ClientEventHandler();
    public static final Minecraft MINECRAFT = Minecraft.getMinecraft();

    public static final KeyBinding KEY_UPDATES_GUI = new KeyBinding("", Keyboard.KEY_U, "");

    private Timer timer;

    @Override
    public void onPreInit() {
        super.onPreInit();

        MinecraftForge.EVENT_BUS.register(ClientProxy.CLIENT_EVENT_HANDLER);
        ClientRegistry.registerKeyBinding(ClientProxy.KEY_UPDATES_GUI);

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
        MINECRAFT.addScheduledTask(() -> message.onClientReceived(MINECRAFT, message, MINECRAFT.thePlayer, messageContext));
    }

    @Override
    public float getPartialTicks() {
        return timer.renderPartialTicks;
    }
}
