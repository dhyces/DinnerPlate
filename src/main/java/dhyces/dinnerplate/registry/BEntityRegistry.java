package dhyces.dinnerplate.registry;

import java.util.function.Supplier;

import dhyces.dinnerplate.DinnerPlate;
import dhyces.dinnerplate.blockentity.MeasuringCupBlockEntity;
import dhyces.dinnerplate.blockentity.MixingBowlBlockEntity;
import dhyces.dinnerplate.blockentity.PlateBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BEntityRegistry {

	private static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_REGISTER = DeferredRegister.create(
																									ForgeRegistries.BLOCK_ENTITIES, 
																									DinnerPlate.MODID
																											 );
	
	public static final RegistryObject<BlockEntityType<PlateBlockEntity>> PLATE_ENTITY;
	public static final RegistryObject<BlockEntityType<MixingBowlBlockEntity>> MIXING_BOWL_ENTITY;
	public static final RegistryObject<BlockEntityType<MeasuringCupBlockEntity>> MEASURING_CUP_ENTITY;
	
	public static void register(IEventBus bus) {
		BLOCK_ENTITY_REGISTER.register(bus);
	}
	private static <T extends BlockEntity> RegistryObject<BlockEntityType<T>> register(String id, Supplier<BlockEntityType<T>> supplier) {
		return BLOCK_ENTITY_REGISTER.register(id, supplier);
	}
	
	static {
		PLATE_ENTITY = register("plate", () -> BlockEntityType.Builder.of(PlateBlockEntity::new, BlockRegistry.PLATE_BLOCK.get()).build(null));
		MIXING_BOWL_ENTITY = register("mixing_bowl", () -> BlockEntityType.Builder.of(MixingBowlBlockEntity::new, BlockRegistry.MIXING_BOWL_BLOCK.get()).build(null));
		MEASURING_CUP_ENTITY = register("measuring_cup", () -> BlockEntityType.Builder.of(MeasuringCupBlockEntity::new, BlockRegistry.MEASURING_CUP_BLOCK.get()).build(null));
	}
	
}
