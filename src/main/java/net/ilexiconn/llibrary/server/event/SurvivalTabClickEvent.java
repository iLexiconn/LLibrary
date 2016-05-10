package net.ilexiconn.llibrary.server.event;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.Event;

public class SurvivalTabClickEvent extends Event {
    private String label;
    private EntityPlayer player;

    public SurvivalTabClickEvent(String label, EntityPlayer player) {
        this.label = label;
        this.player = player;
    }

    public String getLabel() {
        return label;
    }

    public EntityPlayer getPlayer() {
        return player;
    }
}
