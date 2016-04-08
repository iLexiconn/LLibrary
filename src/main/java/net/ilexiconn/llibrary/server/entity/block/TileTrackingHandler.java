package net.ilexiconn.llibrary.server.entity.block;

import net.ilexiconn.llibrary.LLibrary;
import net.ilexiconn.llibrary.server.network.TileTrackMessage;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author gegy1000
 * @since 1.2.1
 */
public enum TileTrackingHandler {
    INSTANCE;

    private Map<BlockPos, ITrackableTile> trackedTiles = new HashMap<>();
    private Map<BlockPos, NBTTagCompound> previousData = new HashMap<>();
    private List<ITrackableTile> firstTick = new ArrayList<>();

    /**
     * Syncs the given TileEntity
     * @param tile the tile to sync
     */
    public void markDirty(TileEntity tile) {
        tile.markDirty();
        BlockPos pos = new BlockPos(tile);
        if (tile instanceof ITrackableTile) {
            if (tile.getWorldObj().isRemote) {
                LLibrary.NETWORK_WRAPPER.sendToServer(new TileTrackMessage(pos, (ITrackableTile) tile));
            } else {
                LLibrary.NETWORK_WRAPPER.sendToAll(new TileTrackMessage(pos, (ITrackableTile) tile));
            }
        }
    }

    /**
     * Registers a tracker for the given ITrackableTile.
     * Call from TileEntity#onUpdate (implement IUpdatePlayerListBox)
     * @param tile the tile to track.
     */
    public <T extends TileEntity & ITrackableTile> void trackTile(T tile) {
        BlockPos pos = new BlockPos(tile);
        if (this.trackedTiles.get(pos) != tile) {
            this.trackedTiles.put(pos, tile);
            this.firstTick.add(tile);
        }
    }

    /**
     * Removes the tracker at the given position.
     * @param pos the position to remove a tracker from.
     */
    public void removeTracker(BlockPos pos) {
        this.trackedTiles.remove(pos);
        this.previousData.remove(pos);
    }

    /**
     * Updates all the tracked tiles.
     * @param world the world
     */
    public void updateTrackers(World world) {
        long ticks = world.getWorldTime();
        List<BlockPos> invalidTrackers = new ArrayList<>();
        for (Map.Entry<BlockPos, ITrackableTile> entry : new HashMap<>(trackedTiles).entrySet()) {
            BlockPos pos = entry.getKey();
            ITrackableTile trackedTile = entry.getValue();
            int trackingFrequency = trackedTile.getTrackingFrequency();
            if (trackingFrequency == 0 || ticks % trackingFrequency == 0) {
                if (this.firstTick.contains(trackedTile) || world.getTileEntity(pos.getX(), pos.getY(), pos.getZ()) == trackedTile) {
                    NBTTagCompound tag = new NBTTagCompound();
                    trackedTile.saveTrackingData(tag);
                    if (this.previousData.get(pos) == null || !tag.equals(this.previousData.get(pos))) {
                        trackedTile.onSync();
                        this.markDirty((TileEntity) trackedTile);
                        this.previousData.put(pos, tag);
                    }
                    this.firstTick.remove(trackedTile);
                } else {
                    invalidTrackers.add(pos);
                }
            }
        }
        for (BlockPos invalidTracker : invalidTrackers) {
            this.trackedTiles.remove(invalidTracker);
        }
    }
}
