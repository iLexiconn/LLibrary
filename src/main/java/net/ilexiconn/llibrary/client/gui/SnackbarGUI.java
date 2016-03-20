package net.ilexiconn.llibrary.client.gui;

import net.ilexiconn.llibrary.client.ClientProxy;
import net.ilexiconn.llibrary.client.util.ClientUtils;
import net.ilexiconn.llibrary.server.snackbar.Snackbar;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class SnackbarGUI extends Gui {
    private Snackbar snackbar;
    private int maxAge;
    private int age;
    private float yOffset = 20;

    public SnackbarGUI(Snackbar snackbar) {
        this.snackbar = snackbar;
        this.maxAge = snackbar.getDuration() > 0 ? snackbar.getDuration() : ClientProxy.MINECRAFT.fontRendererObj.getStringWidth(snackbar.getMessage()) * 2;
    }

    public void updateSnackbar() {
        this.age++;
        if (this.age > this.maxAge) {
            ClientProxy.CLIENT_EVENT_HANDLER.snackbarGUI = null;
        }
    }

    public void drawSnackbar() {
        GlStateManager.pushMatrix();
        GlStateManager.translate(0.0F, yOffset, 0.0F);
        ScaledResolution resolution = new ScaledResolution(ClientProxy.MINECRAFT);
        drawRect(0, resolution.getScaledHeight() - 20, resolution.getScaledWidth(), resolution.getScaledHeight(), 0xFF333333);
        ClientProxy.MINECRAFT.fontRendererObj.drawString(this.snackbar.getMessage(), 10, resolution.getScaledHeight() - 14, 0xFFFFFFFF);
        GlStateManager.popMatrix();

        if (this.age < this.maxAge - 73) {
            yOffset = ClientUtils.updateValue(yOffset, 0.0F);
        } else {
            yOffset = ClientUtils.updateValue(yOffset, 20.0F);
        }
    }
}
