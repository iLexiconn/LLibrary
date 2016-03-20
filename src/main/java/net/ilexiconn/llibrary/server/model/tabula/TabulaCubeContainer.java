package net.ilexiconn.llibrary.server.model.tabula;

import java.util.List;

public class TabulaCubeContainer {
    private String name;
    private String identifier;
    private String parentIdentifier;

    private int[] dimensions;
    private double[] position;
    private double[] offset;
    private double[] rotation;
    private double[] scale;

    private int[] txOffset;
    private boolean txMirror;

    private double mcScale = 1.0;
    private double opacity = 100.0;
    private boolean hidden;

    private List<TabulaCubeContainer> children;

    public String getName() {
        return this.name;
    }

    public String getIdentifier() {
        return this.identifier;
    }

    public String getParentIdentifier() {
        return this.parentIdentifier;
    }

    public int[] getDimensions() {
        return this.dimensions;
    }

    public double[] getPosition() {
        return this.position;
    }

    public double[] getOffset() {
        return this.offset;
    }

    public double[] getRotation() {
        return this.rotation;
    }

    public double[] getScale() {
        return this.scale;
    }

    public int[] getTextureOffset() {
        return this.txOffset;
    }

    public boolean isTextureMirrorEnabled() {
        return this.txMirror;
    }

    public double getMCScale() {
        return this.mcScale;
    }

    public double getOpacity() {
        return this.opacity;
    }

    public boolean isHidden() {
        return this.hidden;
    }

    public List<TabulaCubeContainer> getChildren() {
        return children;
    }
}
