package net.ilexiconn.llibrary.server.capability;

import net.ilexiconn.llibrary.LLibrary;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

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
    public void init(Entity entity, World world) {
        this.entity = entity;
        for (IEntityData entityData : EntityDataHandler.INSTANCE.getEntityData(entity)) {
            entityData.init(entity, world);
        }
    }

    @Override
    public void saveToNBT(NBTTagCompound compound) {
        for (IEntityData entityData : EntityDataHandler.INSTANCE.getEntityData(entity)) {
            NBTTagCompound managerTag = new NBTTagCompound();
            entityData.saveNBTData(managerTag);
            compound.setTag(entityData.getID(), managerTag);
        }
    }

    @Override
    public void loadFromNBT(NBTTagCompound compound) {
        for (IEntityData entityData : EntityDataHandler.INSTANCE.getEntityData(entity)) {
            NBTTagCompound managerTag = compound.getCompoundTag(entityData.getID());
            entityData.loadNBTData(managerTag);
        }
    }
}
