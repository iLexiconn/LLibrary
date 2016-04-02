package net.ilexiconn.llibrary.server.structure;

import com.google.common.collect.Lists;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

/**
 * @author jglrxavpok
 * @since 1.1.0
 */
public abstract class StructureGenerator {
    public static final List<EnumFacing> CLOCKWISE_FACINGS = Lists.newArrayList(EnumFacing.NORTH, EnumFacing.EAST, EnumFacing.SOUTH, EnumFacing.WEST);

    public static EnumFacing getNextClockwise(EnumFacing facing) {
        int index = CLOCKWISE_FACINGS.indexOf(facing);
        if (index < 0) {
            throw new IllegalArgumentException();
        }
        index++;
        index %= CLOCKWISE_FACINGS.size();
        return CLOCKWISE_FACINGS.get(index);
    }

    public abstract void generate(World world, int x, int y, int z, Random random);

    public abstract StructureGenerator rotateClockwise(RotationAngle angle);

    public abstract StructureGenerator rotateTowards(EnumFacing facing);
}
