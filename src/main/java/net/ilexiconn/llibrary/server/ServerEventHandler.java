package net.ilexiconn.llibrary.server;

import net.ilexiconn.llibrary.LLibrary;
import net.ilexiconn.llibrary.server.capability.EntityDataCapabilityImplementation;
import net.ilexiconn.llibrary.server.capability.IEntityDataCapability;
import net.ilexiconn.llibrary.server.config.ConfigHandler;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ServerEventHandler {
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onAttachCapabilities(AttachCapabilitiesEvent.Entity event) {
        event.addCapability(new ResourceLocation("llibrary", "ExtendedEntityDataCapability"), new ICapabilitySerializable() {
            @Override
            public NBTBase serializeNBT() {
                Capability<IEntityDataCapability> capability = LLibrary.ENTITY_DATA_CAPABILITY;
                IEntityDataCapability instance = capability.getDefaultInstance();
                instance.setEntity(event.getEntity());
                return capability.getStorage().writeNBT(capability, instance, null);
            }

            @Override
            public void deserializeNBT(NBTBase nbt) {
                Capability<IEntityDataCapability> capability = LLibrary.ENTITY_DATA_CAPABILITY;
                IEntityDataCapability instance = capability.getDefaultInstance();
                instance.setEntity(event.getEntity());
                capability.getStorage().readNBT(capability, instance, null, nbt);
            }

            @Override
            public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
                return LLibrary.ENTITY_DATA_CAPABILITY == capability;
            }

            @Override
            public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
                return (T) new EntityDataCapabilityImplementation();
            }
        });
    }

    @SubscribeEvent
    public void playerClone(PlayerEvent.Clone event) {
        if (event.wasDeath) {
            NBTTagCompound compound = new NBTTagCompound();
            EntityDataCapabilityImplementation.getCapability(event.original).saveToNBT(compound);
            EntityDataCapabilityImplementation.getCapability(event.entityPlayer).loadFromNBT(compound);
        }
    }

    @SubscribeEvent
    public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (ConfigHandler.INSTANCE.hasConfigForID(event.modID)) {
            ConfigHandler.INSTANCE.saveConfigForID(event.modID);
        }
    }
}
