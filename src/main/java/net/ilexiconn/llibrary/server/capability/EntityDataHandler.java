package net.ilexiconn.llibrary.server.capability;

import net.minecraft.entity.Entity;

public enum EntityDataHandler {
    INSTANCE;

    public void registerExtendedEntityData(Entity entity, IEntityData manager) {
        EntityDataCapabilityImplementation.getCapability(entity).addManager(manager);
    }
}
