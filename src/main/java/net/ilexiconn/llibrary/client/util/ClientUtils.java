package net.ilexiconn.llibrary.client.util;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientUtils {
    private static long lastUpdate = System.currentTimeMillis();

    public static void updateLast() {
        lastUpdate = System.currentTimeMillis();
    }

    public static float updateValue(float current, float target) {
        return ClientUtils.updateValue(current, target, 0.5F);
    }

    public static float updateValue(float current, float target, float factor) {
        float times = (System.currentTimeMillis() - lastUpdate) / 16.666666666666668F;
        float off = (off = target - current) > 0.01F || off < -0.01F ? off * (float) Math.pow(factor, times) : 0.0F;
        return target - off;
    }
}
