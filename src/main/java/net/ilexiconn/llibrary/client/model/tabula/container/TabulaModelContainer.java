package net.ilexiconn.llibrary.client.model.tabula.container;

import java.util.ArrayList;
import java.util.List;

/**
 * @author gegy1000
 * @since 1.0.0
 */
public class TabulaModelContainer {
    private String modelName;
    private String authorName;

    private int projVersion;
    private String[] metadata;

    private double[] scale = new double[3];

    private int textureWidth;
    private int textureHeight;

    private List<TabulaCubeGroupContainer> cubeGroups = new ArrayList<>();
    private List<TabulaCubeContainer> cubes = new ArrayList<>();
    private List<TabulaAnimationContainer> anims = new ArrayList<>();
    private int cubeCount;

    public String getName() {
        return this.modelName;
    }

    public String getAuthor() {
        return this.authorName;
    }

    public int getProjectVersion() {
        return this.projVersion;
    }

    public String[] getMetadata() {
        return metadata;
    }

    public int getTextureWidth() {
        return this.textureWidth;
    }

    public int getTextureHeight() {
        return this.textureHeight;
    }

    public double[] getScale() {
        return this.scale;
    }

    public List<TabulaCubeGroupContainer> getCubeGroups() {
        return this.cubeGroups;
    }

    public List<TabulaCubeContainer> getCubes() {
        return this.cubes;
    }

    public List<TabulaAnimationContainer> getAnimations() {
        return this.anims;
    }

    public int getCubeCount() {
        return this.cubeCount;
    }
}
