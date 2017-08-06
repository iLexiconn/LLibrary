package net.ilexiconn.llibrary.client.event;

import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Event called for adjustment of the 3rd person view distance
 *
 * @author gegy1000
 * @since 1.7.7
 */
@SideOnly(Side.CLIENT)
public class PlayerViewDistanceEvent extends Event {
    private final Entity renderViewEntity;
    private final float partialTicks;
    private final double originalViewDistance;
    private double newViewDistance;

    public PlayerViewDistanceEvent(Entity renderViewEntity, float partialTicks, double viewDistance) {
        this.renderViewEntity = renderViewEntity;
        this.partialTicks = partialTicks;
        this.originalViewDistance = viewDistance;
        this.newViewDistance = viewDistance;
    }

    /**
     * @return the current player
     */
    public Entity getRenderViewEntity() {
        return this.renderViewEntity;
    }

    /**
     * @return the current partial ticks
     */
    public float getPartialTicks() {
        return this.partialTicks;
    }

    /**
     * Overrides the default view distance with the given value
     *
     * @param viewDistance new view distance to use
     */
    public void setViewDistance(double viewDistance) {
        this.newViewDistance = viewDistance;
    }

    /**
     * @return the original view distance, without modification from other events
     */
    public double getOriginalViewDistance() {
        return this.originalViewDistance;
    }

    /**
     * @return the current view distance
     */
    public double getNewViewDistance() {
        return this.newViewDistance;
    }
}
