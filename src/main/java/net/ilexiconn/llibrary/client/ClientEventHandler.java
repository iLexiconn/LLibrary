package net.ilexiconn.llibrary.client;

import net.ilexiconn.llibrary.client.gui.ModUpdateGUI;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.util.Rectangle;

@SideOnly(Side.CLIENT)
public class ClientEventHandler {
    @SubscribeEvent
    public void onInitGuiPost(GuiScreenEvent.InitGuiEvent.Post event) {
        if (event.gui instanceof GuiMainMenu) {
            int offsetX = 0;
            int offsetY = 0;
            int buttonX = event.gui.width / 2 - 124 + offsetX;
            int buttonY = event.gui.height / 4 + 48 + 24 * 2 + offsetY;
            while (true) {
                if (buttonX < 0) {
                    if (offsetY <= -48) {
                        buttonX = 0;
                        buttonY = 0;
                        break;
                    } else {
                        offsetX = 0;
                        offsetY -= 24;
                        buttonX = event.gui.width / 2 - 124 + offsetX;
                        buttonY = event.gui.height / 4 + 48 + 24 * 2 + offsetY;
                    }
                }

                Rectangle rectangle = new Rectangle(buttonX, buttonY, 20, 20);
                boolean intersects = false;
                for (int i = 0; i < event.gui.buttonList.size(); i++) {
                    GuiButton button = event.gui.buttonList.get(i);
                    if (!intersects) {
                        intersects = rectangle.intersects(new Rectangle(button.xPosition, button.yPosition, button.width, button.height));
                    }
                }

                if (!intersects) {
                    break;
                }

                buttonX -= 24;
            }

            event.gui.buttonList.add(new GuiButton(ClientProxy.UPDATE_BUTTON_ID, buttonX, buttonY, 20, 20, "U"));
        }
    }

    @SubscribeEvent
    public void onButtonPressPre(GuiScreenEvent.ActionPerformedEvent.Pre event) {
        if (event.gui instanceof GuiMainMenu && event.button.id == ClientProxy.UPDATE_BUTTON_ID) {
            ClientProxy.MINECRAFT.displayGuiScreen(new ModUpdateGUI((GuiMainMenu) event.gui));
            event.setCanceled(true);
        }
    }
}
