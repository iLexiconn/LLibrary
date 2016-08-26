package net.ilexiconn.llibrary.client.gui.element;

import com.google.common.collect.Lists;
import net.ilexiconn.llibrary.client.ClientProxy;
import net.ilexiconn.llibrary.client.gui.ElementGUI;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Mouse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SideOnly(Side.CLIENT)
public enum ElementHandler {
    INSTANCE;

    private Map<GuiScreen, List<Element<?>>> elementMap = new HashMap<>();
    private Element<?> currentlyClicking;

    public <T extends GuiScreen> Element<T> addElement(T gui, Element<T> element) {
        if (this.elementMap.containsKey(gui)) {
            this.elementMap.get(gui).add(element);
        } else {
            List<Element<?>> elementList = new ArrayList<>();
            elementList.add(element);
            this.elementMap.put(gui, elementList);
        }
        if (gui instanceof ElementGUI) {
            if (!((ElementGUI) gui).doesRequireInit()) {
                element.init();
            }
        }
        return element;

    }

    public <T extends GuiScreen> Element<T> removeElement(T gui, Element<T> element) {
        if (this.elementMap.containsKey(gui)) {
            this.elementMap.get(gui).remove(element);
            return element;
        }
        return null;
    }

    public <T extends GuiScreen> void clearElements(T gui) {
        if (this.elementMap.containsKey(gui)) {
            this.elementMap.remove(gui);
        }
    }

    public <T extends GuiScreen> boolean isElementOnTop(T gui, Element<T> element) {
        if (this.elementMap.containsKey(gui)) {
            List<Element<T>> elementList = Lists.reverse(new ArrayList<>((List<Element<T>>) ((List<?>) this.elementMap.get(gui))));
            this.addChildren(elementList);
            float mouseX = this.getPreciseMouseX(gui);
            float mouseY = this.getPreciseMouseY(gui);
            for (Element<T> e : elementList) {
                if (e.isVisible() && mouseX >= e.getPosX() && mouseY >= e.getPosY() && mouseX < e.getPosX() + e.getWidth() && mouseY < e.getPosY() + e.getHeight()) {
                    return element == e || (element.getParent() != null && element.getParent() == e);
                }
            }
            return false;
        } else {
            return true;
        }
    }

    public <T extends GuiScreen> void onInit(T gui) {
        if (this.elementMap.containsKey(gui)) {
            List<Element<T>> elementList = new ArrayList<>((List<Element<T>>) ((List<?>) this.elementMap.get(gui)));
            this.addChildren(elementList);
            new ArrayList<>(elementList).stream().forEach(Element::init);
        }
    }

    public <T extends GuiScreen> void onUpdate(T gui) {
        if (this.elementMap.containsKey(gui)) {
            List<Element<T>> elementList = new ArrayList<>((List<Element<T>>) ((List<?>) this.elementMap.get(gui)));
            this.addChildren(elementList);
            elementList.stream().forEach(Element::update);
        }
    }

    public <T extends GuiScreen> void onRender(T gui, float partialTicks) {
        if (this.elementMap.containsKey(gui)) {
            List<Element<T>> elementList = new ArrayList<>((List<Element<T>>) ((List<?>) this.elementMap.get(gui)));
            float mouseX = this.getPreciseMouseX(gui);
            float mouseY = this.getPreciseMouseY(gui);
            elementList.stream().forEach(element -> this.onRenderElement(element, mouseX, mouseY, partialTicks));
        }
    }

    private <T extends GuiScreen> void onRenderElement(Element<T> element, float mouseX, float mouseY, float partialTicks) {
        if (element.isVisible()) {
            element.render(mouseX, mouseY, partialTicks);
            for (Element<T> child : element.getChildren()) {
                this.onRenderElement(child, mouseX, mouseY, partialTicks);
            }
        }
    }

    public <T extends GuiScreen> void onMouseClicked(T gui, int button) {
        if (this.elementMap.containsKey(gui)) {
            List<Element<T>> elementList = new ArrayList<>((List<Element<T>>) ((List<?>) this.elementMap.get(gui)));
            this.addChildren(elementList);
            float mouseX = this.getPreciseMouseX(gui);
            float mouseY = this.getPreciseMouseY(gui);
            for (Element<T> element : Lists.reverse(elementList)) {
                if (element.isVisible() && element.isEnabled()) {
                    if (element.mouseClicked(mouseX, mouseY, button)) {
                        this.currentlyClicking = element;
                        return;
                    }
                }
            }
        }
    }

    public <T extends GuiScreen> void onMouseDragged(T gui, int button, long timeSinceClick) {
        if (this.elementMap.containsKey(gui)) {
            List<Element<T>> elementList = new ArrayList<>((List<Element<T>>) ((List<?>) this.elementMap.get(gui)));
            this.addChildren(elementList);
            float mouseX = this.getPreciseMouseX(gui);
            float mouseY = this.getPreciseMouseY(gui);
            for (Element<T> element : Lists.reverse(elementList)) {
                if (element.isVisible() && element.isEnabled() && this.currentlyClicking == element) {
                    if (element.mouseDragged(mouseX, mouseY, button, timeSinceClick)) {
                        return;
                    }
                }
            }
        }
    }

    public <T extends GuiScreen> void onMouseReleased(T gui, int button) {
        if (this.elementMap.containsKey(gui)) {
            List<Element<T>> elementList = new ArrayList<>((List<Element<T>>) ((List<?>) this.elementMap.get(gui)));
            this.addChildren(elementList);
            float mouseX = this.getPreciseMouseX(gui);
            float mouseY = this.getPreciseMouseY(gui);
            for (Element<T> element : Lists.reverse(elementList)) {
                if (element.isVisible() && element.isEnabled() && this.currentlyClicking == element) {
                    if (element.mouseReleased(mouseX, mouseY, button)) {
                        return;
                    }
                }
            }
        }
    }

    public <T extends GuiScreen> void onKeyPressed(T gui, char character, int key) {
        if (this.elementMap.containsKey(gui)) {
            List<Element<T>> elementList = Lists.reverse(new ArrayList<>(new ArrayList<>((List<Element<T>>) ((List<?>) this.elementMap.get(gui)))));
            this.addChildren(elementList);
            for (Element<T> element : elementList) {
                if (element.isVisible() && element.isEnabled()) {
                    if (element.keyPressed(character, key)) {
                        return;
                    }
                }
            }
        }
    }

    public <T extends GuiScreen> void onMouseScrolled(T gui, int amount) {
        if (this.elementMap.containsKey(gui)) {
            List<Element<T>> elementList = Lists.reverse(new ArrayList<>(new ArrayList<>((List<Element<T>>) ((List<?>) this.elementMap.get(gui)))));
            this.addChildren(elementList);
            float mouseX = this.getPreciseMouseX(gui);
            float mouseY = this.getPreciseMouseY(gui);
            for (Element<T> element : elementList) {
                if (element.isVisible() && element.isEnabled()) {
                    if (element.mouseScrolled(mouseX, mouseY, amount)) {
                        return;
                    }
                }
            }
        }
    }

    public <T extends GuiScreen> float getPreciseMouseX(T gui) {
        ScaledResolution scaledResolution = new ScaledResolution(ClientProxy.MINECRAFT);
        return (float) Mouse.getX() / scaledResolution.getScaleFactor();
    }

    public <T extends GuiScreen> float getPreciseMouseY(T gui) {
        return gui.height - (float) Mouse.getY() * gui.height / (float) gui.mc.displayHeight - 1.0F;
    }

    private <T extends GuiScreen> void addChildren(List<Element<T>> elements) {
        for (Element<T> element : new ArrayList<>(elements)) {
            List<Element<T>> children = new ArrayList<>();
            int index = elements.indexOf(element);
            for (Element<T> child : element.getChildren()) {
                List<Element<T>> newChildren = new ArrayList<>();
                newChildren.add(child);
                this.addChildren(newChildren);
                children.addAll(newChildren);
            }
            for (Element<T> child : children) {
                elements.add(index, child);
            }
        }
    }
}
