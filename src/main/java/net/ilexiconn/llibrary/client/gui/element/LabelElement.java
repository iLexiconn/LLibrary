package net.ilexiconn.llibrary.client.gui.element;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.ilexiconn.llibrary.LLibrary;
import net.minecraft.client.gui.GuiScreen;

@SideOnly(Side.CLIENT)
public class LabelElement<T extends GuiScreen> extends Element<T> {
    private String text;

    public LabelElement(T gui, String text, float posX, float posY) {
        super(gui, posX, posY, 0, 0);
        this.text = text;
    }

    @Override
    public void render(float mouseX, float mouseY, float partialTicks) {
        this.drawString(this.text, this.getPosX(), this.getPosY(), LLibrary.CONFIG.getTextColor(), false);
    }
}

