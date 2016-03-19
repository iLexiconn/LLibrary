package net.ilexiconn.llibrary.server.capabilities;

import net.ilexiconn.llibrary.LLibrary;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;

import java.util.ArrayList;
import java.util.List;

public class EntityDataCapabilityImplementation implements IEntityDataCapability {
    private List<ExtendedEntityDataManager> managers = new ArrayList<>();

    public static IEntityDataCapability get(Entity entity) {
        return entity.getCapability(LLibrary.ENTITY_DATA_CAPABILITY, null);
    }

    @Override
    public void save(NBTTagCompound nbt) {
        for (ExtendedEntityDataManager manager : managers) {
            NBTTagCompound managerTag = new NBTTagCompound();
            manager.write(managerTag);
            nbt.setTag(manager.getIdentifier(), managerTag);
        }
    }

    @Override
    public void load(NBTTagCompound nbt) {
        for (ExtendedEntityDataManager manager : managers) {
            NBTTagCompound managerTag = nbt.getCompoundTag(manager.getIdentifier());
            manager.read(managerTag);
        }
    }

    @Override
    public void add(ExtendedEntityDataManager manager) {
        managers.add(manager);
    }
}
