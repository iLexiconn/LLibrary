package net.ilexiconn.llibrary.client.gui;

import com.google.common.collect.Lists;
import net.ilexiconn.llibrary.LLibrary;
import net.ilexiconn.llibrary.client.ClientProxy;
import net.ilexiconn.llibrary.client.gui.element.Element;
import net.ilexiconn.llibrary.client.gui.element.IElementGUI;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.init.SoundEvents;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Mouse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Basic GUI that supports elements. You don't have to extend this class to use the element API, it just makes it easier.
 *
 * @author iLexiconn
 * @since 1.4.0
 */
@SideOnly(Side.CLIENT)
public abstract class ElementGUI extends GuiScreen implements IElementGUI {
    private final List<Element> elements = new ArrayList<>();
    private Element currentlyClicking;

    protected abstract void initElements();

    @Override
    public void addElement(Element element) {
        this.elements.add(element);
        element.init();
    }

    @Override
    public void addElements(Collection<Element> elements) {
        elements.forEach(this::addElement);
    }

    @Override
    public void removeElement(Element element) {
        this.elements.remove(element);
    }

    @Override
    public void clearElements() {
        this.elements.clear();
    }

    @Override
    public void sendElementToFront(Element element) {
        if (this.elements.contains(element)) {
            this.elements.remove(element);
            this.elements.add(element);
        }
    }

    @Override
    public void sendElementToBack(Element element) {
        if (this.elements.contains(element)) {
            this.elements.remove(element);
            this.elements.add(0, element);
        }
    }

    @Override
    public boolean isElementOnTop(Element element) {
        float mouseX = this.getPreciseMouseX();
        float mouseY = this.getPreciseMouseY();
        for (Element e : this.getPostOrderElements()) {
            if (e.isVisible() && mouseX >= e.getPosX() && mouseY >= e.getPosY() && mouseX < e.getPosX() + e.getWidth() && mouseY < e.getPosY() + e.getHeight()) {
                return element == e || (element.getParent() != null && element.getParent() == e);
            }
        }
        return false;
    }

    @Override
    public void playClickSound() {
        this.mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0F));
    }

    @Override
    public FontRenderer getFontRenderer() {
        return this.mc.fontRendererObj;
    }

    @Override
    public TextureManager getTextureManager() {
        return this.mc.getTextureManager();
    }

    @Override
    public int getHeight() {
        return this.height;
    }

    @Override
    public int getWidth() {
        return this.width;
    }

    @Override
    public void initGui() {
        this.initElements();
    }

    @Override
    public void updateScreen() {
        this.getPostOrderElements().forEach(Element::update);
    }

    public abstract void drawScreen(float mouseX, float mouseY, float partialTicks);

    @Override
    public final void drawScreen(int mouseX, int mouseY, float partialTicks) {
        Gui.drawRect(0, 0, this.width, this.height, LLibrary.CONFIG.getTertiaryColor());

        float preciseMouseX = this.getPreciseMouseX();
        float preciseMouseY = this.getPreciseMouseY();
        this.drawScreen(preciseMouseX, preciseMouseY, partialTicks);
        this.elements.forEach(element -> this.renderElement(element, preciseMouseX, preciseMouseY, partialTicks));

        int scrollAmount = Mouse.getDWheel();
        if (scrollAmount != 0) {
            for (Element element : this.getPostOrderElements()) {
                if (element.isVisible() && element.isEnabled()) {
                    if (element.mouseScrolled(preciseMouseX, preciseMouseY, scrollAmount)) {
                        return;
                    }
                }
            }
        }
    }

    protected void renderElement(Element element, float mouseX, float mouseY, float partialTicks) {
        if (element.isVisible()) {
            element.render(mouseX, mouseY, partialTicks);
            for (Element child : (List<Element>) element.getChildren()) {
                this.renderElement(child, mouseX, mouseY, partialTicks);
            }
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        float preciseMouseX = this.getPreciseMouseX();
        float preciseMouseY = this.getPreciseMouseY();
        for (Element element : this.getPostOrderElements()) {
            if (element.isVisible() && element.isEnabled()) {
                if (element.mouseClicked(preciseMouseX, preciseMouseY, mouseButton)) {
                    this.currentlyClicking = element;
                    break;
                }
            }
        }
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        float preciseMouseX = this.getPreciseMouseX();
        float preciseMouseY = this.getPreciseMouseY();
        for (Element element : this.getPostOrderElements()) {
            if (element.isVisible() && element.isEnabled() && this.currentlyClicking == element) {
                if (element.mouseDragged(preciseMouseX, preciseMouseY, clickedMouseButton, timeSinceLastClick)) {
                    break;
                }
            }
        }
        super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        float preciseMouseX = this.getPreciseMouseX();
        float preciseMouseY = this.getPreciseMouseY();
        for (Element element : this.getPostOrderElements()) {
            if (element.isVisible() && element.isEnabled() && this.currentlyClicking == element) {
                if (element.mouseReleased(preciseMouseX, preciseMouseY, state)) {
                    break;
                }
            }
        }
        super.mouseReleased(mouseX, mouseY, state);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        for (Element element : this.getPostOrderElements()) {
            if (element.isVisible() && element.isEnabled()) {
                if (element.keyPressed(typedChar, keyCode)) {
                    break;
                }
            }
        }
        super.keyTyped(typedChar, keyCode);
    }

    @Override
    public void onGuiClosed() {
        this.clearElements();
    }

    public float getPreciseMouseX() {
        ScaledResolution scaledResolution = new ScaledResolution(ClientProxy.MINECRAFT);
        return (float) Mouse.getX() / scaledResolution.getScaleFactor();
    }

    public float getPreciseMouseY() {
        return this.height - (float) Mouse.getY() * this.height / (float) this.mc.displayHeight - 1.0F;
    }

    public List<Element> getPostOrderElements() {
        List<Element> result = new ArrayList<>();
        this.traverseRecursively(this.elements, result);
        return Lists.reverse(result);
    }

    public List<Element> getPreOrderElements() {
        List<Element> result = new ArrayList<>();
        this.traverseRecursively(this.elements, result);
        return result;
    }

    private void traverseRecursively(List<Element> in, List<Element> out) {
        for (Element element : in) {
            out.add(element);
            this.traverseRecursively(element.getChildren(), out);
        }
    }
}
