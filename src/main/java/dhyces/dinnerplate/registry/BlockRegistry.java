package dhyces.dinnerplate.registry;

import java.util.function.Supplier;

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

public class BlockRegistry {

	private static final DeferredRegister<Block> BLOCK_REGISTER = DeferredRegister.create(ForgeRegistries.BLOCKS, DinnerPlate.MODID);

	public static final RegistryObject<Block> PLATE_BLOCK;
	public static final RegistryObject<Block> MIXING_BOWL_BLOCK;
	public static final RegistryObject<Block> MEASURING_CUP_BLOCK;

	public static final RegistryObject<LiquidBlock> MUSHROOM_STEW_FLUID_BLOCK;
	public static final RegistryObject<LiquidBlock> BEETROOT_SOUP_FLUID_BLOCK;
	public static final RegistryObject<LiquidBlock> RABBIT_STEW_FLUID_BLOCK;

	public static void register(IEventBus bus) {
		BLOCK_REGISTER.register(bus);
	}

	private static RegistryObject<Block> register(String id, Supplier<Block> supplier) {
		return BLOCK_REGISTER.register(id, supplier);
	}

	private static RegistryObject<LiquidBlock> registerFluid(String id, Supplier<LiquidBlock> supplier) {
		return BLOCK_REGISTER.register(id, supplier);
	}

	static {
		PLATE_BLOCK = register("plate", () -> new PlateBlock(Properties.of(Material.CLAY, MaterialColor.QUARTZ).strength(.2F)));
		MIXING_BOWL_BLOCK = register("mixing_bowl", () -> new MixingBowlBlock(Properties.of(Material.CLAY, MaterialColor.TERRACOTTA_BROWN).strength(.2F)));
		MEASURING_CUP_BLOCK = register("measuring_cup", () -> new MeasuringCupBlock(Properties.of(Material.GLASS).strength(.2F)));

		MUSHROOM_STEW_FLUID_BLOCK = registerFluid("mushroom_stew", () -> new LiquidBlock(FluidRegistry.MUSHROOM_STEW_FLUID, Properties.of(Material.WATER).noCollission().noDrops().color(MaterialColor.COLOR_BROWN).strength(100)));
		BEETROOT_SOUP_FLUID_BLOCK = registerFluid("beetroot_soup", () -> new LiquidBlock(FluidRegistry.BEETROOT_SOUP_FLUID, Properties.of(Material.WATER).noCollission().noDrops().color(MaterialColor.COLOR_RED).strength(100)));
		RABBIT_STEW_FLUID_BLOCK = registerFluid("rabbit_stew", () -> new LiquidBlock(FluidRegistry.RABBIT_STEW_FLUID, Properties.of(Material.WATER).noCollission().noDrops().color(MaterialColor.COLOR_ORANGE).strength(100)));
	}
}
