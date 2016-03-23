package net.ilexiconn.llibrary.client.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.ilexiconn.llibrary.server.update.UpdateContainer;
import net.ilexiconn.llibrary.server.update.UpdateHandler;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiYesNoCallback;
import net.minecraft.util.StatCollector;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author iLexiconn
 * @since 1.0.0
 */
@SideOnly(Side.CLIENT)
public class ModUpdateGUI extends GuiScreen implements GuiYesNoCallback {
    private GuiMainMenu parent;
    private ModUpdateListGUI modList;
    private int selected = -1;
    private GuiButton buttonUpdate;
    private GuiButton buttonDone;
    private List<String> textList = new ArrayList<>();

    public ModUpdateGUI(GuiMainMenu parent) {
        this.parent = parent;
    }

    public ModUpdateListGUI getModList() {
        return modList;
    }

    @Override
    public void initGui() {
        int width = 0;
        for (UpdateContainer mod : UpdateHandler.INSTANCE.getOutdatedModList()) {
            width = Math.max(width, fontRendererObj.getStringWidth(mod.getModContainer().getName()) + 47);
            width = Math.max(width, fontRendererObj.getStringWidth(mod.getModContainer().getVersion()) + 47);
        }
        width = Math.min(width, 150);
        this.modList = new ModUpdateListGUI(this, width);

        this.buttonList.add(buttonDone = new GuiButton(6, this.width / 2 - 75, this.height - 38, StatCollector.translateToLocal("gui.done")));
        this.buttonList.add(buttonUpdate = new GuiButton(20, 10, this.height - 38, this.modList.getWidth(), 20, StatCollector.translateToLocal("gui.llibrary.update")));

        this.updateModInfo();
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button.enabled) {
            switch (button.id) {
                case 6: {
                    this.mc.displayGuiScreen(this.parent);
                    return;
                }
                case 20: {
                    Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
                    if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
                        try {
                            desktop.browse(new URI(UpdateHandler.INSTANCE.getOutdatedModList().get(selected).getUpdateURL()));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        super.actionPerformed(button);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        if (UpdateHandler.INSTANCE.getOutdatedModList().isEmpty()) {
            this.drawDefaultBackground();
            int i = this.width / 2;
            int j = this.height / 2;
            this.buttonDone.xPosition = width / 2 - 100;
            this.buttonDone.yPosition = height - 38;
            this.buttonList.clear();
            this.buttonList.add(buttonDone);
            this.drawScaledString(StatCollector.translateToLocal("gui.llibrary.updated.1"), i, j - 40, 0xFFFFFF, 2.0F);
            this.drawScaledString(StatCollector.translateToLocal("gui.llibrary.updated.2"), i, j - 15, 0xFFFFFF, 1.0F);
        } else {
            this.modList.drawScreen(mouseX, mouseY, partialTicks);
            int x = this.getModList().getWidth()  + 20;
            int y = 35;
            for (String text : this.textList) {
                y = drawLine(text, x, y);
            }
            this.drawCenteredString(this.fontRendererObj, StatCollector.translateToLocal("gui.llibrary.update.title"), this.width / 2, 16, 0xFFFFFF);
        }

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    public int drawLine(String text, int x, int y) {
        this.fontRendererObj.drawString(text, x, y, 0xd7edea);
        return y + 10;
    }

    public void selectModIndex(int index) {
        if (this.selected != index) {
            this.selected = index;
            this.updateModInfo();
        }
    }

    public boolean modIndexSelected(int index) {
        return this.selected == index;
    }

    private void updateModInfo() {
        this.buttonUpdate.visible = false;

        if (this.selected == -1) {
            return;
        }

        List<String> textList = new ArrayList<>();

        this.buttonUpdate.visible = true;
        this.buttonUpdate.enabled = true;
        this.buttonUpdate.displayString = StatCollector.translateToLocal("gui.llibrary.update");

        UpdateContainer updateContainer = UpdateHandler.INSTANCE.getOutdatedModList().get(selected);
        textList.add(String.format("%s (%s)", updateContainer.getModContainer().getName(), updateContainer.getModContainer().getModId()));
        textList.add(StatCollector.translateToLocal("gui.llibrary.currentVersion") + String.format(": %s", updateContainer.getModContainer().getVersion()));
        textList.add(StatCollector.translateToLocal("gui.llibrary.latestVersion") + String.format(": %s", updateContainer.getLatestVersion().getVersionString()));
        textList.add("");
        Collections.addAll(textList, UpdateHandler.INSTANCE.getChangelog(updateContainer, updateContainer.getLatestVersion()));

        this.textList = textList;
    }

    public void drawScaledString(String text, int x, int y, int color, float scale) {
        GL11.glPushMatrix();
        GL11.glScalef(scale, scale, scale);
        drawCenteredString(fontRendererObj, text, (int) (x / scale), (int) (y / scale), color);
        GL11.glPopMatrix();
    }
}