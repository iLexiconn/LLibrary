package net.ilexiconn.llibrary.server.capability;

import net.ilexiconn.llibrary.LLibrary;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;

import java.util.ArrayList;
import java.util.List;

public class EntityDataCapabilityImplementation implements IEntityDataCapability {
    private List<IEntityData> managers = new ArrayList<>();

    public static IEntityDataCapability getCapability(Entity entity) {
        return entity.getCapability(LLibrary.ENTITY_DATA_CAPABILITY, null);
    }

    @Override
    public void saveToNBT(NBTTagCompound compound) {
        for (IEntityData manager : this.managers) {
            NBTTagCompound managerTag = new NBTTagCompound();
            manager.writeToNBT(managerTag);
            compound.setTag(manager.getIdentifier(), managerTag);
        }
    }

    @Override
    public void loadFromNBT(NBTTagCompound compound) {
        for (IEntityData manager : this.managers) {
            NBTTagCompound managerTag = compound.getCompoundTag(manager.getIdentifier());
            manager.readFromNBT(managerTag);
        }
    }

    @Override
    public void addManager(IEntityData manager) {
        this.managers.add(manager);
    }
}
