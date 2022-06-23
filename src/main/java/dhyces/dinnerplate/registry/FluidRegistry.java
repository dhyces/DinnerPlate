package dhyces.dinnerplate.registry;

import dhyces.dinnerplate.DinnerPlate;
import dhyces.dinnerplate.fluid.StewFluid;
import net.minecraft.util.FastColor.ARGB32;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class FluidRegistry {

    public static final RegistryObject<ForgeFlowingFluid> MUSHROOM_STEW_FLUID_FLOWING;
    public static final RegistryObject<ForgeFlowingFluid> MUSHROOM_STEW_FLUID;
    public static final RegistryObject<ForgeFlowingFluid> BEETROOT_SOUP_FLUID_FLOWING;
    public static final RegistryObject<ForgeFlowingFluid> BEETROOT_SOUP_FLUID;
    public static final RegistryObject<ForgeFlowingFluid> RABBIT_STEW_FLUID_FLOWING;
    public static final RegistryObject<ForgeFlowingFluid> RABBIT_STEW_FLUID;
    private static final DeferredRegister<Fluid> FLUID_REGISTER = DeferredRegister.create(ForgeRegistries.Keys.FLUIDS, DinnerPlate.MODID);

    static {
        MUSHROOM_STEW_FLUID_FLOWING = registerFlowingFluid("flowing_mushroom_stew", () -> new StewFluid.Flowing(FluidTypeRegistry.MUSHROOM_STEW_FLUID_TYPE,
                FluidRegistry.MUSHROOM_STEW_FLUID,
                FluidRegistry.MUSHROOM_STEW_FLUID_FLOWING,
                BlockRegistry.MUSHROOM_STEW_FLUID_BLOCK,
                ItemRegistry.MUSHROOM_STEW_BUCKET));
        MUSHROOM_STEW_FLUID = registerFlowingFluid("mushroom_stew", () -> new StewFluid.Source(FluidTypeRegistry.MUSHROOM_STEW_FLUID_TYPE,
                FluidRegistry.MUSHROOM_STEW_FLUID,
                FluidRegistry.MUSHROOM_STEW_FLUID_FLOWING,
                BlockRegistry.MUSHROOM_STEW_FLUID_BLOCK,
                ItemRegistry.MUSHROOM_STEW_BUCKET));
        BEETROOT_SOUP_FLUID_FLOWING = registerFlowingFluid("flowing_beetroot_soup", () -> new StewFluid.Flowing(FluidTypeRegistry.BEETROOT_SOUP_FLUID_TYPE,
                FluidRegistry.BEETROOT_SOUP_FLUID,
                FluidRegistry.BEETROOT_SOUP_FLUID_FLOWING,
                BlockRegistry.BEETROOT_SOUP_FLUID_BLOCK,
                ItemRegistry.BEETROOT_SOUP_BUCKET));
        BEETROOT_SOUP_FLUID = registerFlowingFluid("beetroot_soup", () -> new StewFluid.Source(FluidTypeRegistry.BEETROOT_SOUP_FLUID_TYPE,
                FluidRegistry.BEETROOT_SOUP_FLUID,
                FluidRegistry.BEETROOT_SOUP_FLUID_FLOWING,
                BlockRegistry.BEETROOT_SOUP_FLUID_BLOCK,
                ItemRegistry.BEETROOT_SOUP_BUCKET));
        RABBIT_STEW_FLUID_FLOWING = registerFlowingFluid("flowing_rabbit_stew", () -> new StewFluid.Flowing(FluidTypeRegistry.RABBIT_STEW_FLUID_TYPE,
                FluidRegistry.RABBIT_STEW_FLUID,
                FluidRegistry.RABBIT_STEW_FLUID_FLOWING,
                BlockRegistry.RABBIT_STEW_FLUID_BLOCK,
                ItemRegistry.RABBIT_STEW_BUCKET));
        RABBIT_STEW_FLUID = registerFlowingFluid("rabbit_stew", () -> new StewFluid.Source(FluidTypeRegistry.RABBIT_STEW_FLUID_TYPE,
                FluidRegistry.RABBIT_STEW_FLUID,
                FluidRegistry.RABBIT_STEW_FLUID_FLOWING,
                BlockRegistry.RABBIT_STEW_FLUID_BLOCK,
                ItemRegistry.RABBIT_STEW_BUCKET));
    }

    public static void register(IEventBus bus) {
        FLUID_REGISTER.register(bus);
    }

    private static RegistryObject<ForgeFlowingFluid> registerFlowingFluid(String id, Supplier<ForgeFlowingFluid> supplier) {
        return FLUID_REGISTER.register(id, supplier);
    }
}
