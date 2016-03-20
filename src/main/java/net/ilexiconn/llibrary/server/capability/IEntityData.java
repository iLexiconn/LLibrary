package net.ilexiconn.llibrary.server.capability;

import net.minecraft.nbt.NBTTagCompound;

public interface IEntityData {
    void writeToNBT(NBTTagCompound compound);

    void readFromNBT(NBTTagCompound compound);

    String getIdentifier();
}
