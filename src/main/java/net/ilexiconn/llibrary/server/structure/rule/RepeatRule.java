package net.ilexiconn.llibrary.server.structure.rule;

import net.ilexiconn.llibrary.server.structure.BlockCoords;
import net.minecraft.world.World;

import java.util.Random;

/**
 * @author jglrxavpok
 * @since 1.1.0
 */
public abstract class RepeatRule {
    private int spacingX;
    private int spacingY;
    private int spacingZ;

    public void setSpacing(int spacingX, int spacingY, int spacingZ) {
        this.spacingX = spacingX;
        this.spacingY = spacingY;
        this.spacingZ = spacingZ;
    }

    public int getSpacingX() {
        return spacingX;
    }

    public int getSpacingY() {
        return spacingY;
    }

    public int getSpacingZ() {
        return spacingZ;
    }

    public abstract boolean continueRepeating(World world, Random rand, BlockCoords position);

    public abstract void repeat(World world, Random rand, BlockCoords position);

    public abstract void reset(World world, Random random, BlockCoords pos);

    public void init(World world, Random random, BlockCoords pos) {
        reset(world, random, pos);
    }
}
