package net.ilexiconn.llibrary.server;

import net.ilexiconn.llibrary.LLibrary;
import net.ilexiconn.llibrary.server.config.ConfigHandler;
import net.ilexiconn.llibrary.server.entity.EntityProperties;
import net.ilexiconn.llibrary.server.entity.EntityPropertiesHandler;
import net.ilexiconn.llibrary.server.entity.PropertiesTracker;
import net.ilexiconn.llibrary.server.network.PropertiesMessage;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
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

    @SubscribeEvent
    public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (ConfigHandler.INSTANCE.hasConfigForID(event.modID)) {
            ConfigHandler.INSTANCE.saveConfigForID(event.modID);
        }
    }

    @SubscribeEvent
    public void onEntityConstructing(EntityEvent.EntityConstructing event) {
        boolean cached = EntityPropertiesHandler.INSTANCE.hasEntityInCache(event.entity.getClass());
        List<String> entityPropertiesIDCache = !cached ? new ArrayList<>() : null;
        EntityPropertiesHandler.INSTANCE.getRegisteredProperties().filter(propEntry -> propEntry.getKey().isAssignableFrom(event.entity.getClass())).forEach(propEntry -> {
            for (Class<? extends EntityProperties> propClass : propEntry.getValue()) {
                try {
                    Constructor<? extends EntityProperties> constructor = propClass.getConstructor();
                    EntityProperties prop = constructor.newInstance();
                    String propID = prop.getID();
                    event.entity.registerExtendedProperties(propID, prop);
                    if (!cached) {
                        entityPropertiesIDCache.add(propID);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        if (!cached) {
            EntityPropertiesHandler.INSTANCE.addEntityToCache(event.entity.getClass(), entityPropertiesIDCache);
        }
    }

    @SubscribeEvent
    public void onEntityUpdate(LivingEvent.LivingUpdateEvent event) {
        if (event.entity.worldObj.isRemote || !(event.entity instanceof EntityPlayerMP)) {
            return;
        }
        EntityPlayerMP player = (EntityPlayerMP) event.entity;
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
        if (!event.world.isRemote && event.entity instanceof EntityPlayerMP) {
            EntityPlayerMP player = (EntityPlayerMP) event.entity;
            EntityPropertiesHandler.INSTANCE.addTracker(player, player);
        }
    }

    @SubscribeEvent
    public void onEntityStartTracking(PlayerEvent.StartTracking event) {
        if (event.entityPlayer instanceof EntityPlayerMP) {
            EntityPropertiesHandler.INSTANCE.addTracker((EntityPlayerMP) event.entityPlayer, event.target);
        }
    }

    @SubscribeEvent
    public void onEntityStopTracking(PlayerEvent.StopTracking event) {
        if (event.entityPlayer instanceof EntityPlayerMP) {
            EntityPropertiesHandler.INSTANCE.removeTracker((EntityPlayerMP) event.entityPlayer, event.target);
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
                    WorldServer world = DimensionManager.getWorld(player.dimension);
                    if (player.isDead || world == null || !world.loadedEntityList.contains(player)) {
                        iterator.remove();
                        trackerEntry.getValue().forEach(PropertiesTracker::removeTracker);
                    }
                }
            }
        }
    }
}
