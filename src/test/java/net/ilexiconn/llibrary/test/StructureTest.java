package net.ilexiconn.llibrary.test;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import net.ilexiconn.llibrary.server.structure.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

@Mod(modid = "StructureTest")
public class StructureTest {
    @Mod.EventHandler
    public void onPreInit(FMLPreInitializationEvent event) {
        GameRegistry.registerItem(new TestItem(), "structuretest");

        StructureBuilder builder = new StructureBuilder();
        int width = 5;
        int height = 4;
        int depth = 5;
        builder.startComponent()
                .cube(-width / 2, 0, -depth / 2, width, height + 1, depth, Blocks.planks)
                .fillCube(-width / 2, 0, -depth / 2, width, 1, depth, Blocks.cobblestone)
                .wireCube(-width / 2, 0, -depth / 2, width, height, depth, Blocks.cobblestone)

                .cube(-width / 2, height, -depth / 2, width, 1, depth, Blocks.log)

                .cube(-width / 2, height + 1, -depth / 2, width, 1, depth, Blocks.fence)

                .fillCube(0, 1, -depth / 2, 1, 2, 1, Blocks.air)
                .setBlock(0, 0, -depth / 2 - 1, BlockState.create(Blocks.stone_stairs, 3))

                .setBlock(-width / 2, 2, 0, Blocks.glass_pane)
                .setBlock(width / 2, 2, 0, Blocks.glass_pane)
                .setBlock(0, 2, depth / 2, Blocks.glass_pane)

                .setBlock(0, 1, 0, BlockState.create(Blocks.furnace, 3))

                .fillCube(-1, 1, 1, 1, height, 1, BlockState.create(Blocks.ladder, 3))
                .endComponent();

        StructureHandler.INSTANCE.registerStructure("testStructure", builder);
    }

    public static class TestItem extends Item {
        public TestItem() {
            setUnlocalizedName("structuretest");
        }

        @Override
        public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int meta, float hitX, float hitY, float hitZ) {
            if (world.isRemote) {
                return true;
            }

            StructureGenerator gen = StructureHandler.INSTANCE.getStructure("testStructure").rotateClockwise(RotationAngle.DEGREES_180);
            gen.generate(world, x, y + 1, z, world.rand);
            return true;
        }
    }
}
