package net.ilexiconn.llibrary.server.structure;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.ilexiconn.llibrary.server.structure.rule.FixedRule;
import net.ilexiconn.llibrary.server.structure.rule.RepeatRule;
import net.ilexiconn.llibrary.server.util.Tuple3;
import net.minecraft.block.Block;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

import java.util.*;

/**
 * @author jglrxavpok
 * @since 1.1.0
 */
public class StructureBuilder extends StructureGenerator {
    private final HashMap<BlockCoords, BlockList> blocks;
    private int offsetX;
    private int offsetY;
    private int offsetZ;
    private List<RepeatRule> repeats;
    private List<ComponentInfo> components;
    private ComponentInfo currentLayer;

    public StructureBuilder() {
        blocks = Maps.newHashMap();
        repeats = Lists.newArrayList();
        components = Lists.newArrayList();
    }

    @Override
    public void generate(World world, int x, int y, int z, Random random) {
        BlockCoords pos = new BlockCoords();
        for (ComponentInfo layer : components) {
            for (RepeatRule rule : layer.repeats) {
                rule.reset(world, random, pos);
            }
        }
        for (ComponentInfo layer : components) {
            pos.x = x;
            pos.y = y;
            pos.z = z;
            for (RepeatRule rule : layer.repeats) {
                rule.init(world, random, pos);
                while (rule.continueRepeating(world, random, pos)) {
                    for (Map.Entry<BlockCoords, BlockList> e : layer.blocks.entrySet()) {
                        BlockCoords coords = e.getKey();
                        int blockX = coords.x + pos.x;
                        int blockY = coords.y + pos.y;
                        int blockZ = coords.z + pos.z;
                        BlockState state = e.getValue().getRandom(random);
                        world.setBlock(blockX, blockY, blockZ, state.getBlock());
                        world.setBlockMetadataWithNotify(blockX, blockY, blockZ, state.getMeta(), 2);
                    }
                    rule.repeat(world, random, pos);
                }
            }
        }
    }

    @Override
    public StructureGenerator rotateClockwise(RotationAngle angle) {
        StructureBuilder copy = new StructureBuilder();
        float radAngle = 0;
        switch (angle) {
            case DEGREES_180:
                return rotateClockwise(RotationAngle.DEGREES_90).rotateClockwise(RotationAngle.DEGREES_90);
            case DEGREES_90:
                radAngle = (float) Math.PI / 2f;
                break;
            case DEGREES_270:
                radAngle = (float) Math.PI * 3f / 2f;
                break;
        }
        for (ComponentInfo oldComp : components) {
            ComponentInfo newComp = new ComponentInfo();
            newComp.repeats.addAll(oldComp.repeats);
            newComp.facing = oldComp.facing;
            for (int i = 0; i < angle.getTurnsCount(); i++) {
                newComp.facing = getNextClockwise(newComp.facing);
            }
            HashMap<BlockCoords, BlockList> blocks = newComp.blocks;
            Tuple3<Integer, Integer, Integer> minCoords = mins(oldComp.blocks);
            Tuple3<Integer, Integer, Integer> maxCoords = maxs(oldComp.blocks);
            int width = maxCoords.getA() - minCoords.getA();
            int depth = maxCoords.getC() - minCoords.getC();

            float midX = width / 2f + minCoords.getA();
            float midZ = depth / 2f + minCoords.getC();
            for (BlockCoords coords : oldComp.blocks.keySet()) {
                float angleToCenter = (float) Math.atan2(coords.z - midZ, coords.x - midX);
                float dx = midX - coords.x;
                float dz = midZ - coords.z;
                float distToCenter = (float) Math.sqrt(dx * dx + dz * dz);
                float nangle = radAngle + angleToCenter;
                float newX = (float) Math.cos(nangle) * distToCenter;
                float newZ = (float) Math.sin(nangle) * distToCenter;
                BlockCoords newCoords = new BlockCoords((int) Math.floor(newX + midX), coords.y, (int) Math.floor(newZ + midZ));
                BlockList newList = oldComp.blocks.get(coords).copy();
                blocks.put(newCoords, newList);
            }

            copy.components.add(newComp);
        }
        return copy;
    }

    private Tuple3<Integer, Integer, Integer> maxs(HashMap<BlockCoords, BlockList> blocks) {
        Tuple3<Integer, Integer, Integer> result = new Tuple3<>();
        int maxX = Integer.MIN_VALUE;
        int maxY = Integer.MIN_VALUE;
        int maxZ = Integer.MIN_VALUE;
        for (BlockCoords coords : blocks.keySet()) {
            int x = coords.x;
            int y = coords.y;
            int z = coords.z;
            if (x > maxX) {
                maxX = x;
            }
            if (y > maxY) {
                maxY = y;
            }
            if (z > maxZ) {
                maxZ = z;
            }
        }
        result.set(maxX, maxY, maxZ);
        return result;
    }

    private Tuple3<Integer, Integer, Integer> mins(HashMap<BlockCoords, BlockList> blocks) {
        Tuple3<Integer, Integer, Integer> result = new Tuple3<>();
        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;
        int minZ = Integer.MAX_VALUE;
        for (BlockCoords coords : blocks.keySet()) {
            int x = coords.x;
            int y = coords.y;
            int z = coords.z;
            if (x < minX) {
                minX = x;
            }
            if (y < minY) {
                minY = y;
            }
            if (z < minZ) {
                minZ = z;
            }
        }
        result.set(minX, minY, minZ);
        return result;
    }

    @Override
    public StructureGenerator rotateTowards(EnumFacing facing) {
        return null;
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

    public StructureBuilder setOrientation(EnumFacing facing) {
        currentLayer.facing = facing;
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
        return setBlock(x, y, z, BlockState.create(block));
    }

    public StructureBuilder setBlock(int x, int y, int z, BlockState block) {
        return setBlock(x, y, z, new BlockList(block));
    }

    public StructureBuilder setBlock(int x, int y, int z, BlockList list) {
        blocks.put(new BlockCoords(x + offsetX, y + offsetY, z + offsetZ), list);
        return this;
    }

    public StructureBuilder cube(int startX, int startY, int startZ, int width, int height, int depth, BlockState block) {
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

    public StructureBuilder fillCube(int startX, int startY, int startZ, int width, int height, int depth, BlockState block) {
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
        return cube(startX, startY, startZ, width, height, depth, BlockState.create(block));
    }

    public StructureBuilder fillCube(int startX, int startY, int startZ, int width, int height, int depth, Block block) {
        return fillCube(startX, startY, startZ, width, height, depth, BlockState.create(block));
    }

    public StructureBuilder wireCube(int startX, int startY, int startZ, int width, int height, int depth, Block block) {
        return wireCube(startX, startY, startZ, width, height, depth, BlockState.create(block));
    }

    public StructureBuilder wireCube(int startX, int startY, int startZ, int width, int height, int depth, BlockState state) {
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
