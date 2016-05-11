package net.ilexiconn.llibrary.client.gui.survivaltab;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.ilexiconn.llibrary.LLibrary;
import net.ilexiconn.llibrary.server.event.SurvivalTabClickEvent;
import net.ilexiconn.llibrary.server.network.SurvivalTabMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class SurvivalTabGUI extends GuiButton {
    public static final ResourceLocation TABS_TEXTURE = new ResourceLocation("llibrary", "textures/gui/survival_tab.png");

    private SurvivalTab survivalTab;

    public SurvivalTabGUI(int id, SurvivalTab survivalTab) {
        super(id, 0, 0, 0, 0, "");
        this.survivalTab = survivalTab;
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GuiContainer container = (GuiContainer) mc.currentScreen;
        boolean isSelected = mc.currentScreen.getClass() == this.survivalTab.getContainer();
        mc.renderEngine.bindTexture(SurvivalTabGUI.TABS_TEXTURE);

        String label = I18n.format(this.survivalTab.getLabel());
        int textWidth = mc.fontRenderer.getStringWidth(label) + 4;
        this.xPosition = container.guiLeft + (LLibrary.CONFIG.areTabsLeftSide() ? -textWidth : container.xSize);
        this.yPosition = container.guiTop + this.survivalTab.getColumn() * 17 + 3;
        this.width = textWidth;
        this.height = 17;

        if (LLibrary.CONFIG.areTabsLeftSide()) {
            if (isSelected) {
                this.drawTexturedModalRect(this.xPosition + textWidth, this.yPosition, 4, 0, 3, 17);
                this.drawTexturedModalRect(this.xPosition - 3, this.yPosition, 0, 0, 4, 17);
                for (int i = 0; i < textWidth; i++) {
                    this.drawTexturedModalRect(this.xPosition + i, this.yPosition, 14, 0, 1, 17);
                }
            } else {
                this.drawTexturedModalRect(this.xPosition - 3, this.yPosition, 7, 0, 4, 17);
                for (int i = 0; i < textWidth; i++) {
                    this.drawTexturedModalRect(this.xPosition + i, this.yPosition, 11, 0, 1, 17);
                }
            }
        } else {
            if (isSelected) {
                this.drawTexturedModalRect(this.xPosition - 3, this.yPosition, 12, 0, 2, 17);
                this.drawTexturedModalRect(this.xPosition - 1 + textWidth, this.yPosition, 15, 0, 4, 17);
                for (int i = 0; i < textWidth; i++) {
                    this.drawTexturedModalRect(this.xPosition - 1 + i, this.yPosition, 14, 0, 1, 17);
                }
            } else {
                this.drawTexturedModalRect(this.xPosition + textWidth, this.yPosition, 20, 0, 4, 17);
                for (int i = 0; i < textWidth; i++) {
                    this.drawTexturedModalRect(this.xPosition + i, this.yPosition, 19, 0, 1, 17);
                }
            }
        }

        mc.fontRenderer.drawString(label, this.xPosition + 2, this.yPosition + 5, 4210752);
    }

    @Override
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
        if (super.mousePressed(mc, mouseX, mouseY)) {
            if (mc.currentScreen.getClass() != this.survivalTab.getContainer()) {
                MinecraftForge.EVENT_BUS.post(new SurvivalTabClickEvent(this.survivalTab.getLabel(), mc.thePlayer));
                LLibrary.NETWORK_WRAPPER.sendToServer(new SurvivalTabMessage(this.survivalTab.getLabel()));
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    @Override
    public void func_146113_a(SoundHandler soundHandler) {

    }
}
