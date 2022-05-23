package dhyces.dinnerplate.registry;

import dhyces.dinnerplate.DinnerPlate;
import dhyces.dinnerplate.fluid.StewFluid;
import net.minecraft.util.FastColor.ARGB32;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class FluidRegistry {

    private static final DeferredRegister<Fluid> FLUID_REGISTRY = DeferredRegister.create(ForgeRegistries.FLUIDS, DinnerPlate.MODID);
    public static final RegistryObject<ForgeFlowingFluid> MUSHROOM_STEW_FLUID_FLOWING;
    public static final RegistryObject<ForgeFlowingFluid> MUSHROOM_STEW_FLUID;
    public static final RegistryObject<ForgeFlowingFluid> BEETROOT_SOUP_FLUID_FLOWING;
    public static final RegistryObject<ForgeFlowingFluid> BEETROOT_SOUP_FLUID;
    public static final RegistryObject<ForgeFlowingFluid> RABBIT_STEW_FLUID_FLOWING;
    public static final RegistryObject<ForgeFlowingFluid> RABBIT_STEW_FLUID;
    public static final RegistryObject<ForgeFlowingFluid> WHEAT_FLOUR_FLUID_FLOWING;
    public static final RegistryObject<ForgeFlowingFluid> WHEAT_FLOUR_FLUID;
    public static final RegistryObject<ForgeFlowingFluid> DOUGH_FLUID_FLOWING;
    public static final RegistryObject<ForgeFlowingFluid> DOUGH_FLUID;

    static {
        MUSHROOM_STEW_FLUID_FLOWING = registerFlowingFluid("flowing_mushroom_stew", () -> new StewFluid.Flowing(ARGB32.color(255, 205, 140, 111),
                FluidRegistry.MUSHROOM_STEW_FLUID,
                FluidRegistry.MUSHROOM_STEW_FLUID_FLOWING,
                BlockRegistry.MUSHROOM_STEW_FLUID_BLOCK,
                ItemRegistry.MUSHROOM_STEW_BUCKET));
        MUSHROOM_STEW_FLUID = registerFlowingFluid("mushroom_stew", () -> new StewFluid.Source(ARGB32.color(255, 205, 140, 111),
                FluidRegistry.MUSHROOM_STEW_FLUID,
                FluidRegistry.MUSHROOM_STEW_FLUID_FLOWING,
                BlockRegistry.MUSHROOM_STEW_FLUID_BLOCK,
                ItemRegistry.MUSHROOM_STEW_BUCKET));
        BEETROOT_SOUP_FLUID_FLOWING = registerFlowingFluid("flowing_beetroot_soup", () -> new StewFluid.Flowing(ARGB32.color(255, 132, 22, 13),
                FluidRegistry.BEETROOT_SOUP_FLUID,
                FluidRegistry.BEETROOT_SOUP_FLUID_FLOWING,
                BlockRegistry.BEETROOT_SOUP_FLUID_BLOCK,
                ItemRegistry.BEETROOT_SOUP_BUCKET));
        BEETROOT_SOUP_FLUID = registerFlowingFluid("beetroot_soup", () -> new StewFluid.Source(ARGB32.color(255, 132, 22, 13),
                FluidRegistry.BEETROOT_SOUP_FLUID,
                FluidRegistry.BEETROOT_SOUP_FLUID_FLOWING,
                BlockRegistry.BEETROOT_SOUP_FLUID_BLOCK,
                ItemRegistry.BEETROOT_SOUP_BUCKET));
        RABBIT_STEW_FLUID_FLOWING = registerFlowingFluid("flowing_rabbit_stew", () -> new StewFluid.Flowing(ARGB32.color(255, 210, 129, 85),
                FluidRegistry.RABBIT_STEW_FLUID,
                FluidRegistry.RABBIT_STEW_FLUID_FLOWING,
                BlockRegistry.RABBIT_STEW_FLUID_BLOCK,
                ItemRegistry.RABBIT_STEW_BUCKET));
        RABBIT_STEW_FLUID = registerFlowingFluid("rabbit_stew", () -> new StewFluid.Source(ARGB32.color(255, 210, 129, 85),
                FluidRegistry.RABBIT_STEW_FLUID,
                FluidRegistry.RABBIT_STEW_FLUID_FLOWING,
                BlockRegistry.RABBIT_STEW_FLUID_BLOCK,
                ItemRegistry.RABBIT_STEW_BUCKET));
        WHEAT_FLOUR_FLUID_FLOWING = registerFlowingFluid("flowing_wheat_flour", () -> new ForgeFlowingFluid.Flowing(wheatProperties()));
        WHEAT_FLOUR_FLUID = registerFlowingFluid("wheat_flour", () -> new ForgeFlowingFluid.Source(wheatProperties()));
        DOUGH_FLUID_FLOWING = registerFlowingFluid("flowing_dough", () -> new ForgeFlowingFluid.Flowing(doughProperties()));
        DOUGH_FLUID = registerFlowingFluid("dough", () -> new ForgeFlowingFluid.Source(doughProperties()));
    }

    public static void register(IEventBus bus) {
        FLUID_REGISTRY.register(bus);
    }

    public static ForgeFlowingFluid.Properties doughProperties() {
        return properties(DOUGH_FLUID, DOUGH_FLUID_FLOWING, builder("block/dough", "block/dough_flow"))
                .bucket(ItemRegistry.DOUGH_BUCKET);
    }

    public static ForgeFlowingFluid.Properties wheatProperties() {
        return properties(WHEAT_FLOUR_FLUID, WHEAT_FLOUR_FLUID_FLOWING, builder("block/wheat_flour_still", "block/wheat_flour_flow")
                        .density(3000)
                        .viscosity(9000))
                .tickRate(15)
                .slopeFindDistance(1)
                .levelDecreasePerBlock(3)
                .block(BlockRegistry.WHEAT_FLOUR_BLOCK)
                .bucket(ItemRegistry.WHEAT_FLOUR_BUCKET);
    }

    public static FluidAttributes.Builder builder(String stillRL, String flowingRL) {
        return FluidAttributes.builder(DinnerPlate.modLoc(stillRL), DinnerPlate.modLoc(flowingRL));
    }

    public static ForgeFlowingFluid.Properties properties(Supplier<? extends Fluid> still, Supplier<? extends Fluid> flowing, FluidAttributes.Builder builder) {
        return new ForgeFlowingFluid.Properties(still, flowing, builder);
    }

    private static RegistryObject<Fluid> register(String id, Supplier<Fluid> supplier) {
        return FLUID_REGISTRY.register(id, supplier);
    }

    private static RegistryObject<ForgeFlowingFluid> registerFlowingFluid(String id, Supplier<ForgeFlowingFluid> supplier) {
        return FLUID_REGISTRY.register(id, supplier);
    }
}
