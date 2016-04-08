package net.ilexiconn.llibrary.server.entity.block;

import io.netty.buffer.ByteBuf;
import net.minecraft.tileentity.TileEntity;

/**
 * @author gegy1000
 * @since 1.2.1
 */
public class BlockPos {
    private int x, y, z;

    public BlockPos(TileEntity tileEntity) {
        this(tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord);
    }

    public BlockPos(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public void write(ByteBuf buffer) {
        buffer.writeInt(x);
        buffer.writeInt(y);
        buffer.writeInt(z);
    }

    public static BlockPos read(ByteBuf buffer) {
        return new BlockPos(buffer.readInt(), buffer.readInt(), buffer.readInt());
    }

    @Override
    public int hashCode() {
        return x * 43 + y - z;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof BlockPos) {
            BlockPos pos = (BlockPos) obj;
            return pos.x == x && pos.y == y && pos.z == z;
        }
        return false;
    }
}
