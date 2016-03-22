package net.ilexiconn.llibrary.server.capability;

import net.ilexiconn.llibrary.LLibrary;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;

/**
 * @author gegy1000
 * @since 1.0.0
 */
public class EntityDataCapabilityImplementation implements IEntityDataCapability {
    private Entity entity;

    public static IEntityDataCapability getCapability(Entity entity) {
        return entity.getCapability(LLibrary.ENTITY_DATA_CAPABILITY, null);
    }

    @Override
    public void saveToNBT(NBTTagCompound compound) {
        for (IEntityData manager : EntityDataHandler.getManagers(entity)) {
            NBTTagCompound managerTag = new NBTTagCompound();
            manager.writeToNBT(managerTag);
            compound.setTag(manager.getIdentifier(), managerTag);
        }
    }

    @Override
    public void loadFromNBT(NBTTagCompound compound) {
        for (IEntityData manager : EntityDataHandler.getManagers(entity)) {
            NBTTagCompound managerTag = compound.getCompoundTag(manager.getIdentifier());
            manager.readFromNBT(managerTag);
        }
    }

    @Override
    public void setEntity(Entity entity) {
        this.entity = entity;
    }
}
