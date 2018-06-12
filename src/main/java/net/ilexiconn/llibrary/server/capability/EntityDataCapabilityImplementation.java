package net.ilexiconn.llibrary.server.capability;

import net.ilexiconn.llibrary.LLibrary;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * @author gegy1000
 * @since 1.0.0
 */
// TODO: 1.13: Move into IEntityDataCapability.Impl
public class EntityDataCapabilityImplementation implements IEntityDataCapability {
    private final List<IEntityData> attachedData;

    public EntityDataCapabilityImplementation(List<IEntityData> data) {
        this.attachedData = data;
    }

    public EntityDataCapabilityImplementation() {
        this(new ArrayList<>());
    }

    @Deprecated
    public static IEntityDataCapability getCapability(Entity entity) {
        return entity.getCapability(LLibrary.ENTITY_DATA_CAPABILITY, null);
    }

    @Override
    public void init(Entity entity, World world, boolean init) {
        if (init) {
            for (IEntityData entityData : this.attachedData) {
                entityData.init(entity, world);
            }
        }
    }

    @Override
    public void saveToNBT(NBTTagCompound compound) {
        for (IEntityData entityData : this.attachedData) {
            NBTTagCompound managerTag = new NBTTagCompound();
            entityData.saveNBTData(managerTag);
            compound.setTag(entityData.getID(), managerTag);
        }
    }

    @Override
    public void loadFromNBT(NBTTagCompound compound) {
        for (IEntityData entityData : this.attachedData) {
            NBTTagCompound managerTag = compound.getCompoundTag(entityData.getID());
            entityData.loadNBTData(managerTag);
        }
    }

    @Override
    public <T extends Entity> void registerData(IEntityData<T> data) {
        this.attachedData.add(data);
    }

    @Nullable
    @Override
    @SuppressWarnings("unchecked")
    public <T extends Entity> IEntityData<T> getData(String identifier) {
        Optional<IEntityData> retrieved = this.attachedData.stream().filter(data -> data.getID().equals(identifier)).findFirst();
        return (IEntityData<T>) retrieved.orElse(null);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Entity> List<IEntityData<T>> getData() {
        List data = Collections.unmodifiableList(this.attachedData);
        return (List<IEntityData<T>>) data;
    }

    @Override
    public NBTBase serializeNBT() {
        Capability<IEntityDataCapability> capability = LLibrary.ENTITY_DATA_CAPABILITY;
        return capability.getStorage().writeNBT(capability, this, null);
    }

    @Override
    public void deserializeNBT(NBTBase nbt) {
        Capability<IEntityDataCapability> capability = LLibrary.ENTITY_DATA_CAPABILITY;
        capability.getStorage().readNBT(capability, this, null, nbt);
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        return LLibrary.ENTITY_DATA_CAPABILITY == capability;
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        if (capability == LLibrary.ENTITY_DATA_CAPABILITY) {
            return LLibrary.ENTITY_DATA_CAPABILITY.cast(this);
        }
        return null;
    }
}
