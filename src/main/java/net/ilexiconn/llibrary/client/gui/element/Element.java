package net.ilexiconn.llibrary.client.gui.element;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.ilexiconn.llibrary.LLibrary;
import net.ilexiconn.llibrary.client.ClientProxy;
import net.ilexiconn.llibrary.client.gui.element.color.ColorScheme;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.Tessellator;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

@SideOnly(Side.CLIENT)
public class Element<T extends GuiScreen> {
    public static final ColorScheme DEFAULT = ColorScheme.create(() -> LLibrary.CONFIG.getPrimaryColor(), () -> LLibrary.CONFIG.getSecondaryColor());

    private final T gui;
    private Element<T> parent;
    private List<Element<T>> children = new ArrayList<>();
    private ColorScheme colorScheme = Element.DEFAULT;

    private float posX;
    private float posY;
    private int width;
    private int height;

    private boolean enabled = true;
    private boolean visible = true;

    public Element(T gui, float posX, float posY, int width, int height) {
        this.gui = gui;
        this.posX = posX;
        this.posY = posY;
        this.width = width;
        this.height = height;
    }

    public void init() {

    }

    public void update() {

    }

    public void render(float mouseX, float mouseY, float partialTicks) {

    }

    public boolean mouseClicked(float mouseX, float mouseY, int button) {
        return this.isSelected(mouseX, mouseY);
    }

    public boolean mouseDragged(float mouseX, float mouseY, int button, long timeSinceClick) {
        return this.isSelected(mouseX, mouseY);
    }

    public boolean mouseReleased(float mouseX, float mouseY, int button) {
        return this.isSelected(mouseX, mouseY);
    }

    public boolean keyPressed(char character, int key) {
        return false;
    }

    protected boolean isSelected(float mouseX, float mouseY) {
        return ElementHandler.INSTANCE.isElementOnTop(this.getGUI(), this) && mouseX >= this.getPosX() && mouseY >= this.getPosY() && mouseX < this.getPosX() + this.getWidth() && mouseY < this.getPosY() + this.getHeight();
    }

    public Element<T> withParent(Element<T> parent) {
        if (this.parent != null) {
            this.parent.children.remove(this);
        }
        this.parent = parent;
        if (this.parent != null) {
            if (!this.parent.children.contains(this)) {
                this.parent.children.add(this);
            }
        }
        this.children.clear();
        this.init();
        return this;
    }

    public Element<T> withColorScheme(ColorScheme colorScheme) {
        this.colorScheme = colorScheme;
        return this;
    }

    public T getGUI() {
        return gui;
    }

    public Element<T> getParent() {
        return parent;
    }

    public List<Element<T>> getChildren() {
        return children;
    }

    public ColorScheme getColorScheme() {
        return colorScheme;
    }

    public float getPosX() {
        return posX + (this.getParent() != null ? this.getParent().getPosX() : 0);
    }

    public void setPosX(float posX) {
        this.posX = posX;
    }

    public float getPosY() {
        return posY + (this.getParent() != null ? this.getParent().getPosY() : 0);
    }

    public void setPosY(float posY) {
        this.posY = posY;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    protected void drawRectangle(double x, double y, double width, double height, int color) {
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        float a = (float) (color >> 24 & 0xFF) / 255.0F;
        float r = (float) (color >> 16 & 0xFF) / 255.0F;
        float g = (float) (color >> 8 & 0xFF) / 255.0F;
        float b = (float) (color & 0xFF) / 255.0F;
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.setColorRGBA_F(r, g, b, a);
        tessellator.addVertex(x, y + height, 0.0);
        tessellator.addVertex(x + width, y + height, 0.0);
        tessellator.addVertex(x + width, y, 0.0);
        tessellator.addVertex(x, y, 0.0);
        tessellator.draw();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
    }

    protected void drawOutline(double x, double y, double width, double height, int color, double outlineSize) {
        this.drawRectangle(x, y, width - outlineSize, outlineSize, color);
        this.drawRectangle(x + width - outlineSize, y, outlineSize, height - outlineSize, color);
        this.drawRectangle(x, y + height - outlineSize, width, outlineSize, color);
        this.drawRectangle(x, y, outlineSize, height - outlineSize, color);
    }

    protected void startScissor() {
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        float scaleFactor = new ScaledResolution(ClientProxy.MINECRAFT, ClientProxy.MINECRAFT.displayWidth, ClientProxy.MINECRAFT.displayHeight).getScaleFactor();
        GL11.glScissor((int) (this.posX * scaleFactor), (int) ((this.gui.height - (this.posY + this.height)) * scaleFactor), (int) (this.width * scaleFactor), (int) (this.height * scaleFactor));
    }

    protected void endScissor() {
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
    }

    public int drawString(String string, float x, float y, int color, boolean shadow) {
        FontRenderer fontRenderer = this.getGUI().mc.fontRenderer;
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        fontRenderer.resetStyles();
        int l;

        if (shadow) {
            l = this.renderString(string, x + 1, y + 1, color, true);
            l = Math.max(l, this.renderString(string, x, y, color, false));
        } else {
            l = this.renderString(string, x, y, color, false);
        }

        return l;
    }

    private int renderString(String string, float x, float y, int color, boolean shadow) {
        if (string != null) {
            FontRenderer fontRenderer = this.getGUI().mc.fontRenderer;
            if (fontRenderer.bidiFlag) {
                string = fontRenderer.bidiReorder(string);
            }

            if ((color & -67108864) == 0) {
                color |= -16777216;
            }

            if (shadow) {
                color = (color & 16579836) >> 2 | color & -16777216;
            }

            fontRenderer.red = (float) (color >> 16 & 255) / 255.0F;
            fontRenderer.blue = (float) (color >> 8 & 255) / 255.0F;
            fontRenderer.green = (float) (color & 255) / 255.0F;
            fontRenderer.alpha = (float) (color >> 24 & 255) / 255.0F;
            GL11.glColor4f(fontRenderer.red, fontRenderer.blue, fontRenderer.green, fontRenderer.alpha);
            fontRenderer.posX = x;
            fontRenderer.posY = y;
            fontRenderer.renderStringAtPos(string, shadow);
            return (int) fontRenderer.posX;
        } else {
            return 0;
        }
    }
}
