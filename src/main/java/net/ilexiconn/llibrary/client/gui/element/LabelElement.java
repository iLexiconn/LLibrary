package net.ilexiconn.llibrary.client.gui.element;

import net.ilexiconn.llibrary.LLibrary;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class LabelElement<T extends GuiScreen> extends Element<T> {
    private String text;

    public LabelElement(T gui, String text, float posX, float posY) {
        super(gui, posX, posY, 0, 0);
        this.text = text;
    }

    @Override
    public void render(float mouseX, float mouseY, float partialTicks) {
        this.getGUI().mc.fontRendererObj.drawString(this.text, this.getPosX(), this.getPosY(), LLibrary.CONFIG.getTextColor(), false);
    }
}

