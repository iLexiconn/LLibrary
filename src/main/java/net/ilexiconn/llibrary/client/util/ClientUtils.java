package net.ilexiconn.llibrary.client.util;

import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;

/**
 * @author iLexiconn
 * @since 1.0.0
 */
@SideOnly(Side.CLIENT)
public class ClientUtils {
    private static long lastUpdate = System.currentTimeMillis();

    /**
     * Update the current time.
     */
    public static void updateLast() {
        ClientUtils.lastUpdate = System.currentTimeMillis();
    }

    /**
     * Update a value with the default factor of 0.5.
     *
     * @param current the current value
     * @param target  the target
     * @return the updated value
     */
    public static float updateValue(float current, float target) {
        return ClientUtils.updateValue(current, target, 0.5F);
    }

    /**
     * Update a value with a custom factor. How higher the factor, how slower the update. The value will get updated
     * slower if the user has a low framerate.
     *
     * @param current the current value
     * @param target  the target
     * @param factor  the factor
     * @return the updated value
     */
    public static float updateValue(float current, float target, float factor) {
        float times = (System.currentTimeMillis() - ClientUtils.lastUpdate) / 16.666666666666668F;
        float off = (off = target - current) > 0.01F || off < -0.01F ? off * (float) Math.pow(factor, times) : 0.0F;
        return target - off;
    }

    public static float interpolate(float prev, float current, float partialTicks) {
        return prev + partialTicks * (current - prev);
    }

    /**
     * Sets a player specific texture. This can be used to change the skin, cape or elytra texture.
     *
     * @param player  the player to set the texture to
     * @param type    the type of texture to apply
     * @param texture the texture to apply
     * @return true if the texture was successfully applied
     */
    public static boolean setPlayerTexture(AbstractClientPlayer player, MinecraftProfileTexture.Type type, ResourceLocation texture) {
        if (texture != null) {
            player.func_152121_a(type, texture);
            return true;
        }
        return false;
    }

    public static boolean isKeyComboCtrlX(int keyID) {
        return keyID == 45 && GuiScreen.isCtrlKeyDown() && !GuiScreen.isShiftKeyDown() && !ClientUtils.isAltKeyDown();
    }

    public static boolean isKeyComboCtrlV(int keyID) {
        return keyID == 47 && GuiScreen.isCtrlKeyDown() && !GuiScreen.isShiftKeyDown() && !ClientUtils.isAltKeyDown();
    }

    public static boolean isKeyComboCtrlC(int keyID) {
        return keyID == 46 && GuiScreen.isCtrlKeyDown() && !GuiScreen.isShiftKeyDown() && !ClientUtils.isAltKeyDown();
    }

    public static boolean isKeyComboCtrlA(int keyID) {
        return keyID == 30 && GuiScreen.isCtrlKeyDown() && !GuiScreen.isShiftKeyDown() && !ClientUtils.isAltKeyDown();
    }

    public static boolean isAltKeyDown() {
        return Keyboard.isKeyDown(56) || Keyboard.isKeyDown(184);
    }
}
