package net.ilexiconn.llibrary.server;

import net.ilexiconn.llibrary.LLibrary;
import net.ilexiconn.llibrary.server.capability.EntityDataCapabilityImplementation;
import net.ilexiconn.llibrary.server.capability.EntityDataHandler;
import net.ilexiconn.llibrary.server.capability.IEntityDataCapability;
import net.ilexiconn.llibrary.server.config.ConfigHandler;
import net.ilexiconn.llibrary.server.entity.EntityProperties;
import net.ilexiconn.llibrary.server.entity.EntityPropertiesHandler;
import net.ilexiconn.llibrary.server.entity.PropertiesTracker;
import net.ilexiconn.llibrary.server.network.PropertiesMessage;
import net.ilexiconn.llibrary.server.world.WorldDataHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public enum ServerEventHandler {
    INSTANCE;

    private int updateTimer;

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onAttachCapabilities(AttachCapabilitiesEvent.Entity event) {
        event.addCapability(new ResourceLocation("llibrary", "ExtendedEntityDataCapability"), new ICapabilitySerializable() {
            @Override
            public NBTBase serializeNBT() {
                Capability<IEntityDataCapability> capability = LLibrary.ENTITY_DATA_CAPABILITY;
                IEntityDataCapability instance = capability.getDefaultInstance();
                instance.init(event.getEntity(), event.getEntity().getEntityWorld(), false);
                return capability.getStorage().writeNBT(capability, instance, null);
            }

            @Override
            public void deserializeNBT(NBTBase nbt) {
                Capability<IEntityDataCapability> capability = LLibrary.ENTITY_DATA_CAPABILITY;
                IEntityDataCapability instance = capability.getDefaultInstance();
                instance.init(event.getEntity(), event.getEntity().getEntityWorld(), true);
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
    public void onPlayerClone(PlayerEvent.Clone event) {
        if (event.isWasDeath()) {
            NBTTagCompound compound = new NBTTagCompound();
            EntityDataCapabilityImplementation.getCapability(event.getOriginal()).saveToNBT(compound);
            EntityDataCapabilityImplementation.getCapability(event.getEntityPlayer()).loadFromNBT(compound);
        }
    }

    @SubscribeEvent
    public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (ConfigHandler.INSTANCE.hasConfigForID(event.getModID())) {
            ConfigHandler.INSTANCE.saveConfigForID(event.getModID());
        }
    }

    @SubscribeEvent
    public void onEntityConstructing(EntityEvent.EntityConstructing event) {
        boolean cached = EntityPropertiesHandler.INSTANCE.hasEntityInCache(event.getEntity().getClass());
        List<String> entityPropertiesIDCache = !cached ? new ArrayList<>() : null;
        EntityPropertiesHandler.INSTANCE.getRegisteredProperties().filter(propEntry -> propEntry.getKey().isAssignableFrom(event.getEntity().getClass())).forEach(propEntry -> {
            for (Class<? extends EntityProperties> propClass : propEntry.getValue()) {
                try {
                    Constructor<? extends EntityProperties> constructor = propClass.getConstructor();
                    EntityProperties prop = constructor.newInstance();
                    String propID = prop.getID();
                    EntityDataHandler.INSTANCE.registerExtendedEntityData(event.getEntity(), prop);
                    if (!cached) {
                        entityPropertiesIDCache.add(propID);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        if (!cached) {
            EntityPropertiesHandler.INSTANCE.addEntityToCache(event.getEntity().getClass(), entityPropertiesIDCache);
        }
    }

    @SubscribeEvent
    public void onEntityUpdate(LivingEvent.LivingUpdateEvent event) {
        if (event.getEntity().worldObj.isRemote || !(event.getEntity() instanceof EntityPlayerMP)) {
            return;
        }
        EntityPlayerMP player = (EntityPlayerMP) event.getEntity();
        List<PropertiesTracker<?>> trackers = EntityPropertiesHandler.INSTANCE.getEntityTrackers(player);
        if (trackers != null && trackers.size() > 0) {
            boolean hasPlayer = false;
            for (PropertiesTracker tracker : trackers) {
                if (hasPlayer = tracker.getEntity() == player) {
                    break;
                }
            }
            if (!hasPlayer) {
                EntityPropertiesHandler.INSTANCE.addTracker(player, player);
            }
            Iterator<PropertiesTracker<?>> it = trackers.iterator();
            while (it.hasNext()) {
                PropertiesTracker tracker = it.next();
                Entity entity = tracker.getEntity();
                WorldServer entityWorld = DimensionManager.getWorld(entity.dimension);
                if (entity.isDead || entityWorld == null || !entityWorld.loadedEntityList.contains(entity)) {
                    it.remove();
                    tracker.removeTracker();
                    continue;
                }
                tracker.updateTracker();
                if (tracker.isTrackerReady()) {
                    tracker.onSync();
                    PropertiesMessage message = new PropertiesMessage(tracker.getProperties(), tracker.getEntity());
                    LLibrary.NETWORK_WRAPPER.sendTo(message, player);
                }
            }
        }
    }

    @SubscribeEvent
    public void onJoinWorld(EntityJoinWorldEvent event) {
        if (!event.getWorld().isRemote && event.getEntity() instanceof EntityPlayerMP) {
            EntityPlayerMP player = (EntityPlayerMP) event.getEntity();
            EntityPropertiesHandler.INSTANCE.addTracker(player, player);
        }
    }

    @SubscribeEvent
    public void onEntityStartTracking(PlayerEvent.StartTracking event) {
        if (event.getEntityPlayer() instanceof EntityPlayerMP) {
            EntityPropertiesHandler.INSTANCE.addTracker((EntityPlayerMP) event.getEntityPlayer(), event.getTarget());
        }
    }

    @SubscribeEvent
    public void onEntityStopTracking(PlayerEvent.StopTracking event) {
        if (event.getEntityPlayer() instanceof EntityPlayerMP) {
            EntityPropertiesHandler.INSTANCE.removeTracker((EntityPlayerMP) event.getEntityPlayer(), event.getTarget());
        }
    }

    @SubscribeEvent
    public void onServerTickEvent(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            this.updateTimer++;
            if (this.updateTimer > 20) {
                this.updateTimer = 0;
                Iterator<Map.Entry<EntityPlayerMP, List<PropertiesTracker<?>>>> iterator = EntityPropertiesHandler.INSTANCE.getTrackerIterator();
                while (iterator.hasNext()) {
                    Map.Entry<EntityPlayerMP, List<PropertiesTracker<?>>> trackerEntry = iterator.next();
                    EntityPlayerMP player = trackerEntry.getKey();
                    WorldServer playerWorld = DimensionManager.getWorld(player.dimension);
                    if (player == null || player.isDead || playerWorld == null || !playerWorld.loadedEntityList.contains(player)) {
                        iterator.remove();
                        trackerEntry.getValue().forEach(PropertiesTracker::removeTracker);
                    } else {
                        Iterator<PropertiesTracker<?>> it = trackerEntry.getValue().iterator();
                        while (it.hasNext()) {
                            PropertiesTracker tracker = it.next();
                            Entity entity = tracker.getEntity();
                            WorldServer entityWorld = DimensionManager.getWorld(entity.dimension);
                            if (entity == null || entity.isDead || entityWorld == null || !entityWorld.loadedEntityList.contains(entity)) {
                                it.remove();
                                tracker.removeTracker();
                            }
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        if (!event.getWorld().isRemote) {
            WorldDataHandler.INSTANCE.loadWorldData(event.getWorld().getSaveHandler(), event.getWorld());
        }
    }

    @SubscribeEvent
    public void onWorldSave(WorldEvent.Save event) {
        if (!event.getWorld().isRemote) {
            WorldDataHandler.INSTANCE.saveWorldData(event.getWorld().getSaveHandler(), event.getWorld());
        }
    }
}
