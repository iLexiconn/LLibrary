package net.ilexiconn.llibrary.server.capability;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import net.ilexiconn.llibrary.LLibrary;
import net.minecraft.entity.Entity;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * @author gegy1000
 * @since 1.0.0
 */
public enum EntityDataHandler {
    INSTANCE;

    private Cache<Entity, List<IEntityData<?>>> registeredEntityData = CacheBuilder.newBuilder().weakKeys().softValues().build();

    /**
     * Registers an Extended Entity Data Manager to the given entity
     *
     * @param entity the entity to add data to
     * @param entityData the data manager
     * @param <T> the entity type
     */
    public <T extends Entity> void registerExtendedEntityData(T entity, IEntityData<T> entityData) {
        try {
            List<IEntityData<?>> registered = this.registeredEntityData.get(entity, ArrayList::new);
            if (!registered.contains(entityData)) {
                registered.add(entityData);
            }
            this.registeredEntityData.put(entity, registered);
        } catch (ExecutionException e) {
            LLibrary.LOGGER.error("Failed to register extended entity data", e);
        }
    }

    /**
     * @param entity the entity
     * @param identifier the string identifier
     * @param <T> the entity type
     * @return an IEntityData instance on the given entity with the given identifier
     */
    @Nullable
    public <T extends Entity> IEntityData<T> getEntityData(T entity, String identifier) {
        List<IEntityData<T>> managers = this.getEntityData(entity);
        if (managers != null) {
            for (IEntityData manager : managers) {
                if (manager.getID().equals(identifier)) {
                    return manager;
                }
            }
        }
        return null;
    }

    /**
     * Get a list with all the registered data manager for the specified entity
     *
     * @param entity the entity instance
     * @param <T> the entity type
     * @return a list with all the data managers, never null
     */
    public <T extends Entity> List<IEntityData<T>> getEntityData(T entity) {
        List<?> entityData = this.registeredEntityData.getIfPresent(entity);
        if (entityData != null) {
            return (List<IEntityData<T>>) entityData;
        }
        return Collections.emptyList();
    }

    public void removeEntity(Entity entity) {
        this.registeredEntityData.invalidate(entity);
    }
}
