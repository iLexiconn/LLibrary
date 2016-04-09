package net.ilexiconn.llibrary.server.event;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@Cancelable
public class BlockEntityUpdateEvent extends Event {
    private final TileEntity entity;
    private final BlockPos pos;
    private final World world;

    public BlockEntityUpdateEvent(TileEntity entity) {
        this.entity = entity;
        this.pos = entity.getPos();
        this.world = entity.getWorld();
    }

    public TileEntity getEntity() {
        return entity;
    }

    public BlockPos getPos() {
        return pos;
    }

    public World getWorld() {
        return world;
    }
}
