package dhyces.dinnerplate.registry;

import java.util.function.Supplier;

import dhyces.dinnerplate.DinnerPlate;
import dhyces.dinnerplate.fluid.StewFluid;
import net.minecraft.util.FastColor.ARGB32;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class FluidRegistry {

	private static final DeferredRegister<Fluid> FLUID_REGISTRY = DeferredRegister.create(ForgeRegistries.FLUIDS, DinnerPlate.MODID);

	public static final RegistryObject<ForgeFlowingFluid> MUSHROOM_STEW_FLUID_FLOWING;
	public static final RegistryObject<ForgeFlowingFluid> MUSHROOM_STEW_FLUID;
	public static final RegistryObject<ForgeFlowingFluid> BEETROOT_SOUP_FLUID_FLOWING;
	public static final RegistryObject<ForgeFlowingFluid> BEETROOT_SOUP_FLUID;
	public static final RegistryObject<ForgeFlowingFluid> RABBIT_STEW_FLUID_FLOWING;
	public static final RegistryObject<ForgeFlowingFluid> RABBIT_STEW_FLUID;

	public static void register(IEventBus bus) {
		FLUID_REGISTRY.register(bus);
	}

	private static RegistryObject<Fluid> register(String id, Supplier<Fluid> supplier) {
		return FLUID_REGISTRY.register(id, supplier);
	}

	private static RegistryObject<ForgeFlowingFluid> registerFlowingFluid(String id, Supplier<ForgeFlowingFluid> supplier) {
		if (supplier.get() instanceof ForgeFlowingFluid)
			return FLUID_REGISTRY.register(id, supplier);
		return null;
	}

	static {
		MUSHROOM_STEW_FLUID_FLOWING = registerFlowingFluid("flowing_mushroom_stew", () -> new StewFluid.Flowing(ARGB32.color(255, 186, 109, 74),
																												FluidRegistry.MUSHROOM_STEW_FLUID,
																												FluidRegistry.MUSHROOM_STEW_FLUID_FLOWING,
																												BlockRegistry.MUSHROOM_STEW_FLUID_BLOCK,
																												ItemRegistry.MUSHROOM_STEW_BUCKET));
		MUSHROOM_STEW_FLUID = registerFlowingFluid("mushroom_stew", () -> new StewFluid.Source(ARGB32.color(255, 186, 109, 74),
																							   FluidRegistry.MUSHROOM_STEW_FLUID,
																							   FluidRegistry.MUSHROOM_STEW_FLUID_FLOWING,
																							   BlockRegistry.MUSHROOM_STEW_FLUID_BLOCK,
																							   ItemRegistry.MUSHROOM_STEW_BUCKET));
		BEETROOT_SOUP_FLUID_FLOWING = registerFlowingFluid("flowing_beetroot_soup", () -> new StewFluid.Flowing(0xFFC60030,
																												FluidRegistry.BEETROOT_SOUP_FLUID,
																												FluidRegistry.BEETROOT_SOUP_FLUID_FLOWING,
																												BlockRegistry.BEETROOT_SOUP_FLUID_BLOCK,
																												ItemRegistry.BEETROOT_SOUP_BUCKET));
		BEETROOT_SOUP_FLUID = registerFlowingFluid("beetroot_soup", () -> new StewFluid.Source(0xFFC60030,
																							   FluidRegistry.BEETROOT_SOUP_FLUID,
																							   FluidRegistry.BEETROOT_SOUP_FLUID_FLOWING,
																							   BlockRegistry.BEETROOT_SOUP_FLUID_BLOCK,
																							   ItemRegistry.BEETROOT_SOUP_BUCKET));
		RABBIT_STEW_FLUID_FLOWING = registerFlowingFluid("flowing_rabbit_stew", () -> new StewFluid.Flowing(ARGB32.color(255, 226, 157, 74),
																											FluidRegistry.RABBIT_STEW_FLUID,
																											FluidRegistry.RABBIT_STEW_FLUID_FLOWING,
																											BlockRegistry.RABBIT_STEW_FLUID_BLOCK,
																											ItemRegistry.RABBIT_STEW_BUCKET));
		RABBIT_STEW_FLUID = registerFlowingFluid("rabbit_stew", () -> new StewFluid.Source(ARGB32.color(255, 226, 157, 74),
																						   FluidRegistry.RABBIT_STEW_FLUID,
																						   FluidRegistry.RABBIT_STEW_FLUID_FLOWING,
																						   BlockRegistry.RABBIT_STEW_FLUID_BLOCK,
																						   ItemRegistry.RABBIT_STEW_BUCKET));
	}
}
