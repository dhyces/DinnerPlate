package dhyces.dinnerplate.registry;

import java.util.function.Supplier;

import dhyces.dinnerplate.DinnerPlate;
import dhyces.dinnerplate.item.MeasuringCupItem;
import dhyces.dinnerplate.item.MockFoodItem;
import dhyces.dinnerplate.item.PlateItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ItemRegistry {

	private static final DeferredRegister<Item> ITEM_REGISTER = DeferredRegister.create(ForgeRegistries.ITEMS, DinnerPlate.MODID);

	public static final RegistryObject<Item> PLATE_ITEM;
	public static final RegistryObject<Item> MIXING_BOWL_ITEM;
	public static final RegistryObject<Item> MOCK_FOOD_ITEM;
	public static final RegistryObject<Item> MEASURING_CUP_ITEM;

	public static final RegistryObject<Item> MUSHROOM_STEW_BUCKET;
	public static final RegistryObject<Item> BEETROOT_SOUP_BUCKET;
	public static final RegistryObject<Item> RABBIT_STEW_BUCKET;

	public static void register(IEventBus bus) {
		ITEM_REGISTER.register(bus);
	}

	private static RegistryObject<Item> register(String id, Supplier<Item> supplier) {
		return ITEM_REGISTER.register(id, supplier);
	}

	static {
		PLATE_ITEM = register("plate", () -> new PlateItem(new Item.Properties().stacksTo(8).tab(DinnerPlate.tab)));
		MIXING_BOWL_ITEM = register("mixing_bowl", () -> new BlockItem(BlockRegistry.MIXING_BOWL_BLOCK.get(), new Item.Properties().stacksTo(1).tab(DinnerPlate.tab)));
		MEASURING_CUP_ITEM = register("measuring_cup", () -> new MeasuringCupItem(new Item.Properties().stacksTo(1).tab(DinnerPlate.tab)));

		MOCK_FOOD_ITEM = register("mock_food", MockFoodItem::new);

		MUSHROOM_STEW_BUCKET = register("mushroom_stew_bucket", () -> simpleBucket(FluidRegistry.MUSHROOM_STEW_FLUID));
		BEETROOT_SOUP_BUCKET = register("beetroot_soup_bucket", () -> simpleBucket(FluidRegistry.BEETROOT_SOUP_FLUID));
		RABBIT_STEW_BUCKET = register("rabbit_stew_bucket", () -> simpleBucket(FluidRegistry.RABBIT_STEW_FLUID));
	}
	
	public static Item registerCompostable(Item item, float value) {
		addToComposter(item, value);
		return item;
	}
	
	public static void addToComposter(Item item, float value) {
		ComposterBlock.COMPOSTABLES.put(item, value);
	}

	private static Item simpleBucket(Supplier<? extends Fluid> fluid) {
		return new BucketItem(fluid, new Item.Properties().stacksTo(1).tab(DinnerPlate.tab));

	}
}
