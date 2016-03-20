package net.ilexiconn.llibrary.server;

import net.ilexiconn.llibrary.LLibrary;
import net.ilexiconn.llibrary.server.capability.EntityDataCapabilityImplementation;
import net.ilexiconn.llibrary.server.config.ConfigHandler;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ServerEventHandler {
    @SubscribeEvent
    public void onEntityLoad(AttachCapabilitiesEvent.Entity event) {
        event.addCapability(new ResourceLocation("llibrary", "ExtendedEntityDataCapability"), new ICapabilityProvider() {
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
            NBTTagCompound data = new NBTTagCompound();
            EntityDataCapabilityImplementation.getCapability(event.original).saveToNBT(data);
            EntityDataCapabilityImplementation.getCapability(event.entityPlayer).loadFromNBT(data);
        }
    }

    @SubscribeEvent
    public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (ConfigHandler.INSTANCE.hasConfigForID(event.modID)) {
            ConfigHandler.INSTANCE.saveConfigForID(event.modID);
        }
    }
}
