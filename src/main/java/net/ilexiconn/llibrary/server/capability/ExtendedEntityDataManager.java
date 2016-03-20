package net.ilexiconn.llibrary.server.capability;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;

public abstract class ExtendedEntityDataManager {
    public abstract void writeToNBT(NBTTagCompound compound);

    public abstract void readFromNBT(NBTTagCompound compound);

    public abstract String getIdentifier();

    public static void registerExtendedEntityData(Entity entity, ExtendedEntityDataManager manager) {
        EntityDataCapabilityImplementation.getCapability(entity).addManager(manager);
    }
}
