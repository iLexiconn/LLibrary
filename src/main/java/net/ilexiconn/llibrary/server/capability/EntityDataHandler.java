package net.ilexiconn.llibrary.server.capability;

import net.minecraft.entity.Entity;

/**
 * @author gegy1000
 * @since 1.0.0
 */
public enum EntityDataHandler {
    INSTANCE;

    /**
     * Registers an Extended Entity Data Manager to the given entity
     * @param entity the entity to add data to
     * @param manager the data manager
     */
    public void registerExtendedEntityData(Entity entity, IEntityData manager) {
        EntityDataCapabilityImplementation.getCapability(entity).addManager(manager);
    }
}
