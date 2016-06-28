package net.ilexiconn.llibrary.server.structure.rule;

import net.minecraft.util.math.BlockPos.MutableBlockPos;
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

    public boolean continueRepeating(World world, Random rand, MutableBlockPos position) {
        return countdown > 0;
    }

    public void repeat(World world, Random rand, MutableBlockPos position) {
        countdown--;
        position.setPos(
                position.getX() + getSpacingX(),
                position.getY() + getSpacingY(),
                position.getZ() + getSpacingZ()
        );
    }

    @Override
    public void reset(World world, Random random, MutableBlockPos pos) {
        countdown = times;
    }
}
