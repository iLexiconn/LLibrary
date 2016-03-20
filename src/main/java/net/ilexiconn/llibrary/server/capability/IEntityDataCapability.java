package net.ilexiconn.llibrary.server.capability;

import net.minecraft.nbt.NBTTagCompound;

public interface IEntityDataCapability {
    void saveToNBT(NBTTagCompound compound);

    void loadFromNBT(NBTTagCompound compound);

    void addManager(ExtendedEntityDataManager manager);
}
