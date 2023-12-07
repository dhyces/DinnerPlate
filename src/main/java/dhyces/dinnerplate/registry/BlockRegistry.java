package dhyces.dinnerplate.registry;

import dhyces.dinnerplate.DinnerPlate;
import dhyces.dinnerplate.block.MeasuringCupBlock;
import dhyces.dinnerplate.block.MixingBowlBlock;
import dhyces.dinnerplate.block.PlateBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class BlockRegistry {

    public static final DeferredRegister<Block> BLOCK_REGISTER = DeferredRegister.create(ForgeRegistries.Keys.BLOCKS, DinnerPlate.MODID);

    public static void init(IEventBus bus) {
        BLOCK_REGISTER.register(bus);
    }

    public static final RegistryObject<Block> WHITE_PLATE_BLOCK = BLOCK_REGISTER.register("white_plate", () -> new PlateBlock(Properties.of(Material.CLAY, MaterialColor.QUARTZ).strength(.2F)));
    public static final RegistryObject<Block> ORANGE_PLATE_BLOCK = BLOCK_REGISTER.register("orange_plate", () -> new PlateBlock(Properties.of(Material.CLAY, MaterialColor.COLOR_ORANGE).strength(.2F)));
    public static final RegistryObject<Block> MAGENTA_PLATE_BLOCK = BLOCK_REGISTER.register("magenta_plate", () -> new PlateBlock(Properties.of(Material.CLAY, MaterialColor.COLOR_MAGENTA).strength(.2F)));
    public static final RegistryObject<Block> LIGHT_BLUE_PLATE_BLOCK = BLOCK_REGISTER.register("light_blue_plate", () -> new PlateBlock(Properties.of(Material.CLAY, MaterialColor.COLOR_LIGHT_BLUE).strength(.2F)));
    public static final RegistryObject<Block> YELLOW_PLATE_BLOCK = BLOCK_REGISTER.register("yellow_plate", () -> new PlateBlock(Properties.of(Material.CLAY, MaterialColor.COLOR_YELLOW).strength(.2F)));
    public static final RegistryObject<Block> LIME_PLATE_BLOCK = BLOCK_REGISTER.register("lime_plate", () -> new PlateBlock(Properties.of(Material.CLAY, MaterialColor.COLOR_LIGHT_GREEN).strength(.2F)));
    public static final RegistryObject<Block> PINK_PLATE_BLOCK = BLOCK_REGISTER.register("pink_plate", () -> new PlateBlock(Properties.of(Material.CLAY, MaterialColor.COLOR_PINK).strength(.2F)));
    public static final RegistryObject<Block> GRAY_PLATE_BLOCK = BLOCK_REGISTER.register("gray_plate", () -> new PlateBlock(Properties.of(Material.CLAY, MaterialColor.COLOR_GRAY).strength(.2F)));
    public static final RegistryObject<Block> LIGHT_GRAY_PLATE_BLOCK = BLOCK_REGISTER.register("light_gray_plate", () -> new PlateBlock(Properties.of(Material.CLAY, MaterialColor.COLOR_LIGHT_GRAY).strength(.2F)));
    public static final RegistryObject<Block> CYAN_PLATE_BLOCK = BLOCK_REGISTER.register("cyan_plate", () -> new PlateBlock(Properties.of(Material.CLAY, MaterialColor.COLOR_CYAN).strength(.2F)));
    public static final RegistryObject<Block> PURPLE_PLATE_BLOCK = BLOCK_REGISTER.register("purple_plate", () -> new PlateBlock(Properties.of(Material.CLAY, MaterialColor.COLOR_PURPLE).strength(.2F)));
    public static final RegistryObject<Block> BLUE_PLATE_BLOCK = BLOCK_REGISTER.register("blue_plate", () -> new PlateBlock(Properties.of(Material.CLAY, MaterialColor.COLOR_BLUE).strength(.2F)));
    public static final RegistryObject<Block> BROWN_PLATE_BLOCK = BLOCK_REGISTER.register("brown_plate", () -> new PlateBlock(Properties.of(Material.CLAY, MaterialColor.COLOR_BROWN).strength(.2F)));
    public static final RegistryObject<Block> GREEN_PLATE_BLOCK = BLOCK_REGISTER.register("green_plate", () -> new PlateBlock(Properties.of(Material.CLAY, MaterialColor.COLOR_GREEN).strength(.2F)));
    public static final RegistryObject<Block> RED_PLATE_BLOCK = BLOCK_REGISTER.register("red_plate", () -> new PlateBlock(Properties.of(Material.CLAY, MaterialColor.COLOR_RED).strength(.2F)));
    public static final RegistryObject<Block> BLACK_PLATE_BLOCK = BLOCK_REGISTER.register("black_plate", () -> new PlateBlock(Properties.of(Material.CLAY, MaterialColor.COLOR_BLACK).strength(.2F)));

    public static final RegistryObject<Block> MIXING_BOWL_BLOCK = BLOCK_REGISTER.register("mixing_bowl", () -> new MixingBowlBlock(Properties.of(Material.CLAY, MaterialColor.TERRACOTTA_BROWN).strength(.2F)));
    public static final RegistryObject<Block> MEASURING_CUP_BLOCK = BLOCK_REGISTER.register("measuring_cup", () -> new MeasuringCupBlock(Properties.of(Material.GLASS).strength(.2F)));

    public static final RegistryObject<LiquidBlock> MUSHROOM_STEW_FLUID_BLOCK = BLOCK_REGISTER.register("mushroom_stew", () -> new LiquidBlock(FluidRegistry.MUSHROOM_STEW_FLUID, Properties.of(Material.WATER).noCollission().noLootTable().color(MaterialColor.COLOR_BROWN).strength(100)));
    public static final RegistryObject<LiquidBlock> BEETROOT_SOUP_FLUID_BLOCK = BLOCK_REGISTER.register("beetroot_soup", () -> new LiquidBlock(FluidRegistry.BEETROOT_SOUP_FLUID, Properties.of(Material.WATER).noCollission().noLootTable().color(MaterialColor.COLOR_RED).strength(100)));
    public static final RegistryObject<LiquidBlock> RABBIT_STEW_FLUID_BLOCK = BLOCK_REGISTER.register("rabbit_stew", () -> new LiquidBlock(FluidRegistry.RABBIT_STEW_FLUID, Properties.of(Material.WATER).noCollission().noLootTable().color(MaterialColor.COLOR_ORANGE).strength(100)));
}
