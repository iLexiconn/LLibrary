package net.ilexiconn.llibrary.server.structure;

/**
 * @author jglrxavpok
 * @since 1.1.0
 */
public enum RotationAngle {
    DEGREES_90(90, 1),
    DEGREES_180(180, 2),
    DEGREES_270(270, 3);

    private final int angle;
    private final int turnsCount;

    RotationAngle(int angle, int turnsCount) {
        this.angle = angle;
        this.turnsCount = turnsCount;
    }

    public int getValue() {
        return angle;
    }

    public int getTurnsCount() {
        return turnsCount;
    }
}
