package net.ilexiconn.llibrary.server.structure;

import net.ilexiconn.llibrary.server.structure.rule.FixedRule;
import net.ilexiconn.llibrary.server.structure.rule.RepeatRule;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.BlockStairs.EnumHalf;
import net.minecraft.block.BlockVine;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

import java.util.*;

/**
 * @author jglrxavpok
 * @since 1.1.0
 */
public class StructureBuilder extends StructureGenerator {
    private final HashMap<BlockPos, BlockList> blocks = new HashMap<>();
    private int offsetX;
    private int offsetY;
    private int offsetZ;
    private final List<RepeatRule> repeats = new ArrayList<>();
    private final List<ComponentInfo> components = new ArrayList<>();
    private ComponentInfo currentLayer;
    private EnumFacing front = EnumFacing.EAST;
    private EnumFacing top = EnumFacing.UP;

    @Override
    public void generate(World world, int x, int y, int z, Random random) {
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        for (ComponentInfo layer : components) {
            for (RepeatRule rule : layer.repeats) {
                rule.reset(world, random, pos);
            }
        }
        BlockPos.PooledMutableBlockPos pooledPos = BlockPos.PooledMutableBlockPos.retain();
        for (ComponentInfo layer : components) {
            pos.setPos(x, y, z);
            for (RepeatRule rule : layer.repeats) {
                rule.init(world, random, pos);
                while (rule.continueRepeating(world, random, pos)) {
                    for (Map.Entry<BlockPos, BlockList> e : layer.blocks.entrySet()) {
                        BlockPos coords = e.getKey();
                        int blockX = coords.getX() + pos.getX();
                        int blockY = coords.getY() + pos.getY();
                        int blockZ = coords.getZ() + pos.getZ();
                        world.setBlockState(pooledPos.set(blockX, blockY, blockZ), e.getValue().getRandom(random));
                    }
                    rule.repeat(world, random, pos);
                }
            }
        }
        pooledPos.release();
    }

    @Override
    public StructureBuilder rotate(EnumFacing front, EnumFacing top) {
        if (front == top || front.getOpposite() == top) {
            throw new IllegalArgumentException("Invalid rotation: " + front + " & " + top);
        }
        Vec3i frontVec = new Vec3i(front.getFrontOffsetX(), front.getFrontOffsetY(), front.getFrontOffsetZ());
        Vec3i topVec = new Vec3i(-top.getFrontOffsetX(), -top.getFrontOffsetY(), -top.getFrontOffsetZ());
        Vec3i perpVec = topVec.crossProduct(frontVec);
        StructureBuilder copy = new StructureBuilder();
        copy.front = transform(this.front, frontVec, topVec, perpVec);
        copy.top = transform(this.top, frontVec, topVec, perpVec);
        for (ComponentInfo oldComp : components) {
            ComponentInfo newComp = new ComponentInfo();
            newComp.repeats.addAll(oldComp.repeats);
            newComp.front = transform(oldComp.front, frontVec, topVec, perpVec);
            newComp.top = transform(oldComp.top, frontVec, topVec, perpVec);
            HashMap<BlockPos, BlockList> blocks = newComp.blocks;
            boolean inverted = top == EnumFacing.DOWN;
            for (BlockPos coords : oldComp.blocks.keySet()) {
                BlockPos newCoords = transform(coords, frontVec, topVec, perpVec);
                BlockList newList = oldComp.blocks.get(coords).copy();
                IBlockState[] states = newList.getStates();
                for (int i = 0; i < states.length; i++) {
                    IBlockState state = states[i];
                    if (state.getBlock() instanceof BlockStairs) {
                        EnumFacing facing = transform(state.getValue(BlockStairs.FACING), frontVec, topVec, perpVec);
                        EnumFacing perp = transform(EnumFacing.UP, frontVec, topVec, perpVec);
                        if (facing.getAxis() == Axis.Y) {
                            if (state.getValue(BlockStairs.HALF) == EnumHalf.BOTTOM) {
                                perp = perp.getOpposite();
                                if (facing == EnumFacing.UP) {
                                    state = state.cycleProperty(BlockStairs.HALF);
                                }
                            } else if (facing == EnumFacing.DOWN) {
                                state = state.cycleProperty(BlockStairs.HALF);
                            }
                            state = state.withProperty(BlockStairs.FACING, perp);
                        } else {
                            state = state.withProperty(BlockStairs.FACING, facing);
                        }
                        if (inverted) {
                            state = state.cycleProperty(BlockStairs.HALF);
                        }
                    } else if (state.getBlock() instanceof BlockSlab) {
                        if (inverted) {
                            state = state.cycleProperty(BlockSlab.HALF);
                        }
                    } else if (state.getBlock() instanceof BlockVine) {
                        EnumFacing facing = transform(state.getValue(BlockVine.NORTH) ? EnumFacing.NORTH : state.getValue(BlockVine.EAST) ? EnumFacing.EAST : state.getValue(BlockVine.SOUTH) ? EnumFacing.SOUTH : EnumFacing.WEST, frontVec, topVec, perpVec);
                        state = state.withProperty(BlockVine.NORTH, facing == EnumFacing.NORTH);
                        state = state.withProperty(BlockVine.EAST, facing == EnumFacing.EAST);
                        state = state.withProperty(BlockVine.SOUTH, facing == EnumFacing.SOUTH);
                        state = state.withProperty(BlockVine.WEST, facing == EnumFacing.WEST);
                    } else {
                        for (IProperty prop : state.getPropertyNames()) {
                            if (prop instanceof PropertyDirection) {
                                PropertyDirection propDir = (PropertyDirection) prop;
                                EnumFacing facing = state.getValue(propDir);
                                EnumFacing newFacing = transform(facing, frontVec, topVec, perpVec);
                                if (propDir.getAllowedValues().contains(newFacing)) {
                                    state = state.withProperty(propDir, newFacing);
                                }
                            }
                        }
                    }
                    states[i] = state;
                }
                blocks.put(newCoords, newList);
            }
            copy.components.add(newComp);
        }
        return copy;
    }

    private static BlockPos transform(Vec3i pos, Vec3i vec3i, Vec3i vec3i1, Vec3i vec3i2) {
        return new BlockPos(
                vec3i1.getX() * -pos.getY() + vec3i2.getX() * pos.getZ() + vec3i.getX() * pos.getX(),
                vec3i1.getY() * -pos.getY() + vec3i2.getY() * pos.getZ() + vec3i.getY() * pos.getX(),
                vec3i1.getZ() * -pos.getY() + vec3i2.getZ() * pos.getZ() + vec3i.getZ() * pos.getX()
        );
    }

    private static EnumFacing transform(EnumFacing facing, Vec3i vec3i, Vec3i vec3i1, Vec3i vec3i2) {
        BlockPos vec = transform(facing.getDirectionVec(), vec3i, vec3i1, vec3i2);
        return EnumFacing.getFacingFromVector(vec.getX(), vec.getY(), vec.getZ());
    }

    @Override
    public StructureBuilder rotateTowards(EnumFacing facing) {
        if (facing.getAxis() == Axis.Y) {
            throw new IllegalArgumentException("Must be horizontal facing: " + facing);
        }
        int idx = facing.getHorizontalIndex() - (front.getAxis() == Axis.Y ? top.getHorizontalIndex() : front.getHorizontalIndex()) - 1;
        idx = (idx % EnumFacing.HORIZONTALS.length + EnumFacing.HORIZONTALS.length) % EnumFacing.HORIZONTALS.length;
        return rotate(EnumFacing.HORIZONTALS[idx], EnumFacing.UP);
    }

    public StructureBuilder startComponent() {
        currentLayer = new ComponentInfo();
        blocks.clear();
        repeats.clear();
        offsetX = 0;
        offsetY = 0;
        offsetZ = 0;
        return this;
    }

    public StructureBuilder setOrientation(EnumFacing front, EnumFacing top) {
        currentLayer.front = front;
        currentLayer.top = top;
        return this;
    }

    public StructureBuilder endComponent() {
        currentLayer.blocks.putAll(blocks);
        currentLayer.repeats.addAll(repeats);
        components.add(currentLayer);
        return this;
    }

    public StructureBuilder setOffset(int x, int y, int z) {
        this.offsetX = x;
        this.offsetY = y;
        this.offsetZ = z;
        return this;
    }

    public StructureBuilder translate(int x, int y, int z) {
        offsetX += x;
        offsetY += y;
        offsetZ += z;
        return this;
    }

    public StructureBuilder setBlock(int x, int y, int z, Block block) {
        return setBlock(x, y, z, block.getDefaultState());
    }

    public StructureBuilder setBlock(int x, int y, int z, IBlockState block) {
        return setBlock(x, y, z, new BlockList(block));
    }

    public StructureBuilder setBlock(int x, int y, int z, BlockList list) {
        blocks.put(new BlockPos(x + offsetX, y + offsetY, z + offsetZ), list);
        return this;
    }

    public StructureBuilder cube(int startX, int startY, int startZ, int width, int height, int depth, IBlockState block) {
        return cube(startX, startY, startZ, width, height, depth, new BlockList(block));
    }

    public StructureBuilder cube(int startX, int startY, int startZ, int width, int height, int depth, BlockList list) {
        if (depth > 1) {
            fillCube(startX, startY, startZ, width, height, 1, list);
            fillCube(startX, startY, startZ + depth - 1, width, height, 1, list);
        }

        if (width > 1) {
            fillCube(startX, startY, startZ, 1, height, depth, list);
            fillCube(startX + width - 1, startY, startZ, 1, height, depth, list);
        }

        if (height > 1) {
            fillCube(startX, startY, startZ, width, 1, depth, list);
            fillCube(startX, startY + height - 1, startZ, width, 1, depth, list);
        }
        return this;
    }

    public StructureBuilder fillCube(int startX, int startY, int startZ, int width, int height, int depth, IBlockState block) {
        return fillCube(startX, startY, startZ, width, height, depth, new BlockList(block));
    }

    public StructureBuilder fillCube(int startX, int startY, int startZ, int width, int height, int depth, BlockList list) {
        for (int x = startX; x < startX + width; x++) {
            for (int y = startY; y < startY + height; y++) {
                for (int z = startZ; z < startZ + depth; z++) {
                    setBlock(x, y, z, list);
                }
            }
        }
        return this;
    }

    public StructureBuilder repeat(int spacingX, int spacingY, int spacingZ, int times) {
        return repeat(spacingX, spacingY, spacingZ, new FixedRule(times));
    }

    public StructureBuilder repeat(int spacingX, int spacingY, int spacingZ, RepeatRule repeatRule) {
        repeatRule.setSpacing(spacingX, spacingY, spacingZ);
        return addBakedRepeatRule(repeatRule);
    }

    public StructureBuilder addBakedRepeatRule(RepeatRule repeatRule) {
        repeats.add(repeatRule);
        return this;
    }

    public StructureBuilder cube(int startX, int startY, int startZ, int width, int height, int depth, Block block) {
        return cube(startX, startY, startZ, width, height, depth, block.getDefaultState());
    }

    public StructureBuilder fillCube(int startX, int startY, int startZ, int width, int height, int depth, Block block) {
        return fillCube(startX, startY, startZ, width, height, depth, block.getDefaultState());
    }

    public StructureBuilder wireCube(int startX, int startY, int startZ, int width, int height, int depth, Block block) {
        return wireCube(startX, startY, startZ, width, height, depth, block.getDefaultState());
    }

    public StructureBuilder wireCube(int startX, int startY, int startZ, int width, int height, int depth, IBlockState state) {
        return wireCube(startX, startY, startZ, width, height, depth, new BlockList(state));
    }

    private StructureBuilder wireCube(int startX, int startY, int startZ, int width, int height, int depth, BlockList list) {
        fillCube(startX, startY, startZ, 1, height, 1, list);
        fillCube(startX + width - 1, startY, startZ, 1, height, 1, list);
        fillCube(startX + width - 1, startY, startZ + depth - 1, 1, height, 1, list);
        fillCube(startX, startY, startZ + depth - 1, 1, height, 1, list);

        fillCube(startX, startY, startZ, width, 1, 1, list);
        fillCube(startX, startY + height, startZ, width, 1, 1, list);
        fillCube(startX, startY, startZ + depth - 1, width, 1, 1, list);
        fillCube(startX, startY + height, startZ + depth - 1, width, 1, 1, list);

        fillCube(startX, startY, startZ, 1, 1, depth, list);
        fillCube(startX, startY + height, startZ, 1, 1, depth, list);
        fillCube(startX + width - 1, startY, startZ, 1, 1, depth, list);
        fillCube(startX + width - 1, startY + height, startZ, 1, 1, depth, list);
        return this;
    }
}
