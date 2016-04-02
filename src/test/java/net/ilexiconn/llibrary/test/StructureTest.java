package net.ilexiconn.llibrary.test;

import net.ilexiconn.llibrary.server.structure.RotationAngle;
import net.ilexiconn.llibrary.server.structure.StructureBuilder;
import net.ilexiconn.llibrary.server.structure.StructureGenerator;
import net.ilexiconn.llibrary.server.structure.StructureHandler;
import net.minecraft.block.BlockStairs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod(modid = "StructureTest")
public class StructureTest {
    @Mod.EventHandler
    public void onPreInit(FMLPreInitializationEvent event) {
        GameRegistry.register(new TestItem());

        StructureBuilder builder = new StructureBuilder();
        int width = 5;
        int height = 4;
        int depth = 5;
        builder.startComponent()
                .cube(-width / 2, 0, -depth / 2, width, height + 1, depth, Blocks.planks)
                .fillCube(-width / 2, 0, -depth / 2, width, 1, depth, Blocks.cobblestone)
                .wireCube(-width / 2, 0, -depth / 2, width, height, depth, Blocks.cobblestone)

                .cube(-width / 2, height, -depth / 2, width, 1, depth, Blocks.log)

                .cube(-width / 2, height + 1, -depth / 2, width, 1, depth, Blocks.oak_fence)

                .fillCube(0, 1, -depth / 2, 1, 2, 1, Blocks.air)
                .setBlock(0, 0, -depth / 2 - 1, Blocks.stone_stairs.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.SOUTH))

                .setBlock(-width / 2, 2, 0, Blocks.glass_pane)
                .setBlock(width / 2, 2, 0, Blocks.glass_pane)
                .setBlock(0, 2, depth / 2, Blocks.glass_pane)

                .setBlock(0, 1, 0, Blocks.furnace)

                .fillCube(-1, 1, 1, 1, height, 1, Blocks.ladder)
                .endComponent();

        StructureHandler.INSTANCE.registerStructure("testStructure", builder);
    }

    public static class TestItem extends Item {
        public TestItem() {
            setUnlocalizedName("structuretest");
            setRegistryName(new ResourceLocation("StructureTest", "structuretest"));
        }

        @Override
        public EnumActionResult onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
            if (world.isRemote) {
                return EnumActionResult.PASS;
            }
            StructureGenerator gen = StructureHandler.INSTANCE.getStructure("testStructure").rotateClockwise(RotationAngle.DEGREES_180);
            gen.generate(world, pos.getX(), pos.getY() + 1, pos.getZ(), world.rand);
            return EnumActionResult.SUCCESS;
        }
    }
}
