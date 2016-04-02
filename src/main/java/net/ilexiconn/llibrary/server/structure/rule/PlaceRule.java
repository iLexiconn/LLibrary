package net.ilexiconn.llibrary.server.structure.rule;

import net.ilexiconn.llibrary.server.structure.BlockCoords;
import net.minecraft.world.World;

import java.util.Random;

/**
 * @author jglrxavpok
 * @since 1.1.0
 */
public class PlaceRule extends RepeatRule {
    private boolean placed;

    @Override
    public boolean continueRepeating(World world, Random rand, BlockCoords position) {
        return !placed;
    }

    @Override
    public void repeat(World world, Random rand, BlockCoords position) {
        placed = true;
    }

    @Override
    public void reset(World world, Random random, BlockCoords pos) {
        placed = false;
    }
}
