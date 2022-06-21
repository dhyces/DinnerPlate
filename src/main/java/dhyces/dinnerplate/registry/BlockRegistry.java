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

    public static final RegistryObject<Block> WHITE_PLATE_BLOCK;
    public static final RegistryObject<Block> ORANGE_PLATE_BLOCK;
    public static final RegistryObject<Block> MAGENTA_PLATE_BLOCK;
    public static final RegistryObject<Block> LIGHT_BLUE_PLATE_BLOCK;
    public static final RegistryObject<Block> YELLOW_PLATE_BLOCK;
    public static final RegistryObject<Block> LIME_PLATE_BLOCK;
    public static final RegistryObject<Block> PINK_PLATE_BLOCK;
    public static final RegistryObject<Block> GRAY_PLATE_BLOCK;
    public static final RegistryObject<Block> LIGHT_GRAY_PLATE_BLOCK;
    public static final RegistryObject<Block> CYAN_PLATE_BLOCK;
    public static final RegistryObject<Block> PURPLE_PLATE_BLOCK;
    public static final RegistryObject<Block> BLUE_PLATE_BLOCK;
    public static final RegistryObject<Block> BROWN_PLATE_BLOCK;
    public static final RegistryObject<Block> GREEN_PLATE_BLOCK;
    public static final RegistryObject<Block> RED_PLATE_BLOCK;
    public static final RegistryObject<Block> BLACK_PLATE_BLOCK;

    public static final RegistryObject<Block> MIXING_BOWL_BLOCK;
    public static final RegistryObject<Block> MEASURING_CUP_BLOCK;

    public static final RegistryObject<LiquidBlock> MUSHROOM_STEW_FLUID_BLOCK;
    public static final RegistryObject<LiquidBlock> BEETROOT_SOUP_FLUID_BLOCK;
    public static final RegistryObject<LiquidBlock> RABBIT_STEW_FLUID_BLOCK;

    static {
        WHITE_PLATE_BLOCK = register("white_plate", () -> new PlateBlock(Properties.of(Material.CLAY, MaterialColor.QUARTZ).strength(.2F)));
        ORANGE_PLATE_BLOCK = register("orange_plate", () -> new PlateBlock(Properties.of(Material.CLAY, MaterialColor.COLOR_ORANGE).strength(.2F)));
        MAGENTA_PLATE_BLOCK = register("magenta_plate", () -> new PlateBlock(Properties.of(Material.CLAY, MaterialColor.COLOR_MAGENTA).strength(.2F)));
        LIGHT_BLUE_PLATE_BLOCK = register("light_blue_plate", () -> new PlateBlock(Properties.of(Material.CLAY, MaterialColor.COLOR_LIGHT_BLUE).strength(.2F)));
        YELLOW_PLATE_BLOCK = register("yellow_plate", () -> new PlateBlock(Properties.of(Material.CLAY, MaterialColor.COLOR_YELLOW).strength(.2F)));
        LIME_PLATE_BLOCK = register("lime_plate", () -> new PlateBlock(Properties.of(Material.CLAY, MaterialColor.COLOR_LIGHT_GREEN).strength(.2F)));
        PINK_PLATE_BLOCK = register("pink_plate", () -> new PlateBlock(Properties.of(Material.CLAY, MaterialColor.COLOR_PINK).strength(.2F)));
        GRAY_PLATE_BLOCK = register("gray_plate", () -> new PlateBlock(Properties.of(Material.CLAY, MaterialColor.COLOR_GRAY).strength(.2F)));
        LIGHT_GRAY_PLATE_BLOCK = register("light_gray_plate", () -> new PlateBlock(Properties.of(Material.CLAY, MaterialColor.COLOR_LIGHT_GRAY).strength(.2F)));
        CYAN_PLATE_BLOCK = register("cyan_plate", () -> new PlateBlock(Properties.of(Material.CLAY, MaterialColor.COLOR_CYAN).strength(.2F)));
        PURPLE_PLATE_BLOCK = register("purple_plate", () -> new PlateBlock(Properties.of(Material.CLAY, MaterialColor.COLOR_PURPLE).strength(.2F)));
        BLUE_PLATE_BLOCK = register("blue_plate", () -> new PlateBlock(Properties.of(Material.CLAY, MaterialColor.COLOR_BLUE).strength(.2F)));
        BROWN_PLATE_BLOCK = register("brown_plate", () -> new PlateBlock(Properties.of(Material.CLAY, MaterialColor.COLOR_BROWN).strength(.2F)));
        GREEN_PLATE_BLOCK = register("green_plate", () -> new PlateBlock(Properties.of(Material.CLAY, MaterialColor.COLOR_GREEN).strength(.2F)));
        RED_PLATE_BLOCK = register("red_plate", () -> new PlateBlock(Properties.of(Material.CLAY, MaterialColor.COLOR_RED).strength(.2F)));
        BLACK_PLATE_BLOCK = register("black_plate", () -> new PlateBlock(Properties.of(Material.CLAY, MaterialColor.COLOR_BLACK).strength(.2F)));

        MIXING_BOWL_BLOCK = register("mixing_bowl", () -> new MixingBowlBlock(Properties.of(Material.CLAY, MaterialColor.TERRACOTTA_BROWN).strength(.2F)));
        MEASURING_CUP_BLOCK = register("measuring_cup", () -> new MeasuringCupBlock(Properties.of(Material.GLASS).strength(.2F)));

        MUSHROOM_STEW_FLUID_BLOCK = registerFluid("mushroom_stew", () -> new LiquidBlock(FluidRegistry.MUSHROOM_STEW_FLUID, Properties.of(Material.WATER).noCollission().noLootTable().color(MaterialColor.COLOR_BROWN).strength(100)));
        BEETROOT_SOUP_FLUID_BLOCK = registerFluid("beetroot_soup", () -> new LiquidBlock(FluidRegistry.BEETROOT_SOUP_FLUID, Properties.of(Material.WATER).noCollission().noLootTable().color(MaterialColor.COLOR_RED).strength(100)));
        RABBIT_STEW_FLUID_BLOCK = registerFluid("rabbit_stew", () -> new LiquidBlock(FluidRegistry.RABBIT_STEW_FLUID, Properties.of(Material.WATER).noCollission().noLootTable().color(MaterialColor.COLOR_ORANGE).strength(100)));
    }

    public static void register(IEventBus bus) {
        BLOCK_REGISTER.register(bus);
    }

    private static RegistryObject<Block> register(String id, Supplier<Block> supplier) {
        return BLOCK_REGISTER.register(id, supplier);
    }

    private static RegistryObject<LiquidBlock> registerFluid(String id, Supplier<LiquidBlock> supplier) {
        return BLOCK_REGISTER.register(id, supplier);
    }
}
