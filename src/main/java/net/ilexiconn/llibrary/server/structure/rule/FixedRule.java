package net.ilexiconn.llibrary.server.structure.rule;

import net.ilexiconn.llibrary.server.structure.BlockCoords;
import net.minecraft.world.World;

import java.util.Random;

/**
 * @author jglrxavpok
 * @since 1.1.0
 */
public class FixedRule extends RepeatRule {
    protected int times;
    protected int countdown;

    public FixedRule(int times) {
        super();
        this.times = times;
        this.countdown = times;
    }

    public boolean continueRepeating(World world, Random rand, BlockCoords position) {
        return countdown > 0;
    }

    public void repeat(World world, Random rand, BlockCoords position) {
        countdown--;
        position.x += getSpacingX();
        position.y += getSpacingY();
        position.z += getSpacingZ();
    }

    @Override
    public void reset(World world, Random random, BlockCoords pos) {
        countdown = times;
    }
}
