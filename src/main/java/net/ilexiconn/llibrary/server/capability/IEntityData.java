package net.ilexiconn.llibrary.server.capability;

import net.minecraft.nbt.NBTTagCompound;

/**
 * @author gegy1000
 * @since 1.0.0
 */
public interface IEntityData {
    /**
     * Saves data to an assets.testmod.models.entity
     *
     * @param compound the NBTTagCompound to write the data to
     */
    void writeToNBT(NBTTagCompound compound);

    /**
     * Reads data saved on an assets.testmod.models.entity
     *
     * @param compound the NBTTagCompound to read the data from
     */
    void readFromNBT(NBTTagCompound compound);

    /**
     * @return the name of the NBTTagCompound to write/read in the entities data
     */
    String getIdentifier();
}
