package net.ilexiconn.llibrary.client;

import net.ilexiconn.llibrary.client.gui.ModUpdateGUI;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientEventHandler {
    @SubscribeEvent
    public void onKeyPressed(GuiScreenEvent.KeyboardInputEvent event) {
        if (event.gui instanceof GuiMainMenu) {
            ClientProxy.MINECRAFT.displayGuiScreen(new ModUpdateGUI(event.gui));;
        }
    }
}
