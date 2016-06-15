package net.ilexiconn.llibrary;

import net.ilexiconn.llibrary.server.entity.multipart.IMultipartEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.world.World;

public class TestEntity extends EntityCreeper implements IMultipartEntity {
    private Entity[] parts = {this.create(0.0F, 0.0F, 2.0F, 1.0F, 1.0F, 10.0F)};

    public TestEntity(World world) {
        super(world);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        this.updateParts();
    }

    @Override
    public Entity[] getParts() {
        return this.parts;
    }
}
