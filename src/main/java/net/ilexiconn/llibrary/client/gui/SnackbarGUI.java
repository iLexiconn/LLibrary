package net.ilexiconn.llibrary.client.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.ilexiconn.llibrary.client.ClientEventHandler;
import net.ilexiconn.llibrary.client.ClientProxy;
import net.ilexiconn.llibrary.client.util.ClientUtils;
import net.ilexiconn.llibrary.server.snackbar.Snackbar;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.opengl.GL11;

/**
 * @author iLexiconn
 * @since 1.0.0
 */
@SideOnly(Side.CLIENT)
public class SnackbarGUI extends Gui {
    private Snackbar snackbar;
    private int maxAge;
    private int age;
    private float yOffset = 20;

    public SnackbarGUI(Snackbar snackbar) {
        this.snackbar = snackbar;
        this.maxAge = snackbar.getDuration() > 0 ? snackbar.getDuration() : ClientProxy.MINECRAFT.fontRenderer.getStringWidth(snackbar.getMessage()) * 3;
    }

    public void updateSnackbar() {
        this.age++;
        if (this.age > this.maxAge) {
            ClientEventHandler.INSTANCE.setOpenSnackbar(null);
        }
    }

    public void drawSnackbar() {
        GL11.glPushMatrix();
        GL11.glTranslatef(0.0F, 0.0F, 500.0F);
        ScaledResolution resolution = new ScaledResolution(ClientProxy.MINECRAFT, ClientProxy.MINECRAFT.displayWidth, ClientProxy.MINECRAFT.displayHeight);
        switch (this.snackbar.getPosition()) {
            case UP:
                GL11.glTranslatef(0.0F, -this.yOffset, 0.0F);
                Gui.drawRect(0, 20, resolution.getScaledWidth(), 0, this.snackbar.getColor());
                ClientProxy.MINECRAFT.fontRenderer.drawString(this.snackbar.getMessage(), 10, 6, 0xFFFFFFFF);
                break;
            case DOWN:
                GL11.glTranslatef(0.0F, this.yOffset, 0.0F);
                drawRect(0, resolution.getScaledHeight() - 20, resolution.getScaledWidth(), resolution.getScaledHeight(), this.snackbar.getColor());
                ClientProxy.MINECRAFT.fontRenderer.drawString(this.snackbar.getMessage(), 10, resolution.getScaledHeight() - 14, 0xFFFFFFFF);
                break;
        }
        GL11.glPopMatrix();
        this.yOffset = ClientUtils.updateValue(this.yOffset, this.age < this.maxAge - 61 ? 0.0F : 20.0F);
    }
}
