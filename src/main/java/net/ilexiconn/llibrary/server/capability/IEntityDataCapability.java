package net.ilexiconn.llibrary.server.capability;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;

/**
 * @author gegy1000
 * @since 1.0.0
 */
public interface IEntityDataCapability {
    /**
     * Saves this capability to NBT
     */
    void saveToNBT(NBTTagCompound compound);

    /**
     * Loads this capability from NBT
     */
    void loadFromNBT(NBTTagCompound compound);

    void setEntity(Entity entity);
}
