package net.ilexiconn.llibrary.client.model.tabula.container;

import java.util.List;

public class TabulaAnimationComponentContainer {
    private String name;
    private String identifier;

    private int startKey;
    private int length;

    private double[] posChange;
    private double[] rotChange;
    private double[] scaleChange;
    private double opacityChange;

    private double[] posOffset;
    private double[] rotOffset;
    private double[] scaleOffset;
    private double opacityOffset;

    private List<double[]> progressionCoords;

    private boolean hidden;

    public String getName() {
        return this.name;
    }

    public String getIdentifier() {
        return this.identifier;
    }

    public int getStartKey() {
        return this.startKey;
    }

    public int getEndKey() {
        return this.startKey + this.length;
    }

    public int getLength() {
        return this.length;
    }

    public double[] getPositionChange() {
        return this.posChange;
    }

    public double[] getRotationChange() {
        return this.rotChange;
    }

    public double[] getScaleChange() {
        return this.scaleChange;
    }

    public double getOpacityChange() {
        return this.opacityChange;
    }

    public double[] getPositionOffset() {
        return this.posOffset;
    }

    public double[] getRotationOffset() {
        return this.rotOffset;
    }

    public double[] getScaleOffset() {
        return this.scaleOffset;
    }

    public double getOpacityOffset() {
        return this.opacityOffset;
    }

    public List<double[]> getProgressionCoords() {
        return this.progressionCoords;
    }

    public boolean isHidden() {
        return this.hidden;
    }
}
