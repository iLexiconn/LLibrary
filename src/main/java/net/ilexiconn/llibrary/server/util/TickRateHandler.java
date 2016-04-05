package net.ilexiconn.llibrary.server.util;

import net.ilexiconn.llibrary.LLibrary;

public enum TickRateHandler
{
    INSTANCE;

    public static final long DEFAULT_TICK_RATE = 50L;

    private long tickRate = DEFAULT_TICK_RATE;

    public long getTickRate() {
        return tickRate;
    }

    public void setTickRate(long tickRate) {
        if (this.tickRate != tickRate) {
            LLibrary.PROXY.setTickRate(tickRate);
        }
        this.tickRate = tickRate;
    }

    public void setTickRate(float tickRate) {
        this.setTickRate((long) (DEFAULT_TICK_RATE / tickRate));
    }
}
