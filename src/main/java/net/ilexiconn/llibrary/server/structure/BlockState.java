package net.ilexiconn.llibrary.server.structure;

import net.minecraft.block.Block;

public class BlockState {
    private Block block;
    private int meta;

    private BlockState(Block block, int meta) {
        this.block = block;
        this.meta = meta;
    }

    public static BlockState create(Block block) {
        return BlockState.create(block, 0);
    }

    public static BlockState create(Block block, int meta) {
        return new BlockState(block, meta);
    }

    public Block getBlock() {
        return block;
    }

    public int getMeta() {
        return meta;
    }
}
