package net.ilexiconn.llibrary.server.capability;

import net.minecraft.entity.Entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author gegy1000
 * @since 1.0.0
 */
public enum EntityDataHandler {
    INSTANCE;

    private Map<Entity, List<IEntityData>> registeredDataManagers = new HashMap<>();

    /**
     * Registers an Extended Entity Data Manager to the given entity
     *
     * @param entity  the entity to add data to
     * @param manager the data manager
     */
    public void registerExtendedEntityData(Entity entity, IEntityData manager) {
        List<IEntityData> registered = this.registeredDataManagers.get(entity);
        if (registered == null) {
            registered = new ArrayList<>();
        }
        if (!registered.contains(manager)) {
            registered.add(manager);
        }
        this.registeredDataManagers.put(entity, registered);
    }

    /**
     * @returns an ArrayList of registered managers for the given entity, if none are registered for that entity it will return an empty ArrayList
     */
    public static List<IEntityData> getManagers(Entity entity) {
        List<IEntityData> managers = INSTANCE.registeredDataManagers.get(entity);
        return managers == null ? new ArrayList<>() : managers;
    }
}
