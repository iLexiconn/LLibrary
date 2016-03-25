package net.ilexiconn.llibrary.client.model.tabula.container;

import java.util.ArrayList;
import java.util.List;

/**
 * @author gegy1000
 * @since 1.0.0
 */
public class TabulaCubeContainer {
    private String name;
    private String identifier;
    private String parentIdentifier;

    private int[] dimensions = new int[3];
    private double[] position = new double[3];
    private double[] offset = new double[3];
    private double[] rotation = new double[3];
    private double[] scale = new double[3];

    private int[] txOffset = new int[2];
    private boolean txMirror;

    private double mcScale = 1.0;
    private double opacity = 100.0;
    private boolean hidden;

    private List<TabulaCubeContainer> children = new ArrayList<>();

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
