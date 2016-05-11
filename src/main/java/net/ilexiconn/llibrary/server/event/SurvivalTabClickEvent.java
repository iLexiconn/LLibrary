package net.ilexiconn.llibrary.server.event;

import cpw.mods.fml.common.eventhandler.Event;
import net.minecraft.entity.player.EntityPlayer;

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
