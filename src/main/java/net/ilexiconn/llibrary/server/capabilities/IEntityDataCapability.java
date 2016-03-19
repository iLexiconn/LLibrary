package net.ilexiconn.llibrary.server.capabilities;

import net.minecraft.nbt.NBTTagCompound;

public interface IEntityDataCapability {
    void save(NBTTagCompound nbt);
    void load(NBTTagCompound nbt);

    void add(ExtendedEntityDataManager manager);
}
