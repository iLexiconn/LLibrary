package net.ilexiconn.llibrary.client.util;

public class ClientUtils {
    private static long lastUpdate = System.currentTimeMillis();

    public static void updateLast() {
        lastUpdate = System.currentTimeMillis();
    }

    public static float updateValue(float current, float target) {
        float off = (off = target - current) > 0.01F || off < -0.01F ? ClientUtils.smoothenValue(off, 0.7F) : 0.0F;
        return target - off;
    }

    public static float smoothenValue(float value, float factor) {
        float times = (System.currentTimeMillis() - lastUpdate) / 16.666666666666668F;
        return (float) (value * Math.pow(factor, times));
    }
}
