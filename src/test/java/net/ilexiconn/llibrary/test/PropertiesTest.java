package net.ilexiconn.llibrary.test;

import net.ilexiconn.llibrary.server.entity.EntityProperties;
import net.ilexiconn.llibrary.server.entity.EntityPropertiesHandler;
import net.ilexiconn.llibrary.server.entity.block.ITrackableTile;
import net.ilexiconn.llibrary.server.entity.block.TileTrackingHandler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod(modid = "PropertiesTest")
public class PropertiesTest {
    public static final Block TRACKABLE_BLOCK = new BlockContainer(Material.rock) {
        @Override
        public TileEntity createNewTileEntity(World world, int meta) {
            return new TrackableTile();
        }
    }.setUnlocalizedName("trackable_block");

    @Mod.EventHandler
    public void onPreInit(FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
        EntityPropertiesHandler.INSTANCE.registerProperties(TestEntityProperties.class);
        GameRegistry.registerBlock(TRACKABLE_BLOCK, "trackable_block");
        GameRegistry.registerTileEntity(TrackableTile.class, "propertiestest:trackable_block");
    }

    @SubscribeEvent
    public void onEntityJump(LivingEvent.LivingJumpEvent event) {
        if (event.entityLiving instanceof EntityPlayer && !event.entityLiving.worldObj.isRemote) {
            EntityPropertiesHandler.INSTANCE.getProperties(event.entityLiving, TestEntityProperties.class).testInt++;
        }
    }

    public static class TestEntityProperties extends EntityProperties<EntityPlayer> {
        public int testInt = 0;

        @Override
        public void init() {

        }

        @Override
        public String getID() {
            return "PropertiesTest";
        }

        @Override
        public Class<EntityPlayer> getEntityClass() {
            return EntityPlayer.class;
        }

        @Override
        public void saveNBTData(NBTTagCompound compound) {
            compound.setInteger("Test", this.testInt);
        }

        @Override
        public void loadNBTData(NBTTagCompound compound) {
            this.testInt = compound.getInteger("Test");
            System.out.println("Loading " + this.testInt + " on " + FMLCommonHandler.instance().getEffectiveSide());
        }

        @Override
        public int getTrackingTime() {
            return 0;
        }
    }

    public static class TrackableTile extends TileEntity implements ITrackableTile, ITickable {
        private int value;

        public TrackableTile() {
            super();
        }

        @Override
        public void onLoad() {
            super.onLoad();
            TileTrackingHandler.INSTANCE.trackTile(this);
        }

        @Override
        public int getTrackingFrequency() {
            return 10;
        }

        @Override
        public void saveTrackingData(NBTTagCompound compound) {
            compound.setInteger("value", this.value);
        }

        @Override
        public void readTrackingData(NBTTagCompound compound) {
            this.value = compound.getInteger("value");
            System.out.println("Loaded tracked value " + this.value);
        }

        @Override
        public void onSync() {
        }

        @Override
        public void update() {
            this.value++;
        }
    }
}
