package net.ilexiconn.llibrary.test;

import net.ilexiconn.llibrary.server.capability.EntityDataHandler;
import net.ilexiconn.llibrary.server.capability.IEntityData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = "EntityDataTest")
public class EntityDataTest {
    public static final Logger LOGGER = LogManager.getLogger("EntityDataTest");

    @Mod.EventHandler
    public void onPreInit(FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onAttachCapabilities(EntityEvent.EntityConstructing event) {
        if (event.getEntity() instanceof EntityPlayer) {
            EntityDataHandler.INSTANCE.registerExtendedEntityData(event.getEntity(), new IEntityData() {
                @Override
                public void init(Entity entity, World world) {

                }

                @Override
                public void saveNBTData(NBTTagCompound compound) {
                    compound.setString("Test", "yay");
                    LOGGER.info("Saved value \"yay\" to \"Test\"");
                }

                @Override
                public void loadNBTData(NBTTagCompound compound) {
                    LOGGER.info("Loaded value: \"" + compound.getString("Test") + "\"");
                }

                @Override
                public String getID() {
                    return "EntityDataTest";
                }
            });
        }
    }
}
