package net.ilexiconn.llibrary.client.gui;

import net.ilexiconn.llibrary.LLibrary;
import net.ilexiconn.llibrary.client.gui.element.Element;
import net.ilexiconn.llibrary.client.gui.element.ElementHandler;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@SideOnly(Side.CLIENT)
public abstract class ElementGUI extends GuiScreen {
    protected List<Element> elementList = new ArrayList<>();

    public abstract void initElements();

    @Override
    public void initGui() {
        ElementHandler.INSTANCE.clearElements(this);
        this.elementList.clear();
        this.initElements();
        for (Element element : this.elementList) {
            ElementHandler.INSTANCE.addElement(this, element);
        }
        ElementHandler.INSTANCE.init(this);
    }

    @Override
    public void updateScreen() {
        ElementHandler.INSTANCE.update(this);
    }

    public abstract void drawScreen(float mouseX, float mouseY, float partialTicks);

    @Override
    public final void drawScreen(int mouseX, int mouseY, float partialTicks) {
        Gui.drawRect(0, 0, this.width, this.height, LLibrary.CONFIG.getTertiaryColor());
        this.drawScreen(ElementHandler.INSTANCE.getPreciseMouseX(this), ElementHandler.INSTANCE.getPreciseMouseY(this), partialTicks);
        ElementHandler.INSTANCE.render(this, partialTicks);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        ElementHandler.INSTANCE.mouseClicked(this, mouseButton);
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        ElementHandler.INSTANCE.mouseDragged(this, clickedMouseButton, timeSinceLastClick);
        super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        ElementHandler.INSTANCE.mouseReleased(this, state);
        super.mouseReleased(mouseX, mouseY, state);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        ElementHandler.INSTANCE.keyPressed(this, typedChar, keyCode);
        super.keyTyped(typedChar, keyCode);
    }

    @Override
    public void onGuiClosed() {
        ElementHandler.INSTANCE.clearElements(this);
        this.elementList.clear();
    }
}
