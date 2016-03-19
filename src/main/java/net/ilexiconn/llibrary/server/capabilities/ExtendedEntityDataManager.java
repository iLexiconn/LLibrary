package net.ilexiconn.llibrary.server.capabilities;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;

public abstract class ExtendedEntityDataManager {
    public abstract void write(NBTTagCompound nbt);
    public abstract void read(NBTTagCompound nbt);
    public abstract String getIdentifier();

    public static void registerExtendedEntityData(Entity entity, ExtendedEntityDataManager manager) {
        EntityDataCapabilityImplementation.get(entity).add(manager);
    }
}
