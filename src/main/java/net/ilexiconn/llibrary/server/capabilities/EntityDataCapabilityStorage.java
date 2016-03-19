package net.ilexiconn.llibrary.server.capabilities;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

public class EntityDataCapabilityStorage implements Capability.IStorage<IEntityDataCapability> {
    @Override
    public NBTBase writeNBT(Capability<IEntityDataCapability> capability, IEntityDataCapability instance, EnumFacing side) {
        NBTTagCompound nbt = new NBTTagCompound();
        instance.save(nbt);
        return nbt;
    }

    @Override
    public void readNBT(Capability<IEntityDataCapability> capability, IEntityDataCapability instance, EnumFacing side, NBTBase nbt) {
        instance.load((NBTTagCompound) nbt);
    }
}
