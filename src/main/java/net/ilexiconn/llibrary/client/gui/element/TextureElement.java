package net.ilexiconn.llibrary.client.gui.element;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;

@SideOnly(Side.CLIENT)
public class TextureElement<T extends GuiScreen> extends Element<T> {
    private ResourceLocation texture;
    private int u;
    private int v;

    public TextureElement(T gui, ResourceLocation texture, float posX, float posY, int u, int v, int width, int height) {
        super(gui, posX, posY, width, height);
        this.texture = texture;
        this.u = u;
        this.v = v;
    }

    public void render(float mouseX, float mouseY, float partialTicks) {
        this.getGUI().mc.getTextureManager().bindTexture(this.texture);
        this.getGUI().drawTexturedModalRect((int) this.getPosX(), (int) this.getPosY(), this.u, this.v, this.getWidth(), this.getHeight());
    }
}
