package net.ilexiconn.llibrary.server.entity.block;

import net.minecraft.nbt.NBTTagCompound;

/**
 * @author gegy1000
 * @since 1.2.1
 */
public interface ITrackableTile
{
    /**
     * @return the frequency to track this tile. 0 being every tick
     */
    int getTrackingFrequency();

    /**
     * Saves the data to be sent in the tile tracking packet.
     * @param compound the NBTTagCompound to write the tracking data to.
     */
    void saveTrackingData(NBTTagCompound compound);

    /**
     * Reads tracking data from the given compound.
     * @param compound the compound to read from.
     */
    void readTrackingData(NBTTagCompound compound);

    /**
     * Gets called whenever data is sent.
     */
    void onSync();
}
