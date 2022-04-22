package dhyces.dinnerplate.registry;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;

import com.mojang.datafixers.util.Pair;

import dhyces.dinnerplate.DinnerPlate;
import dhyces.dinnerplate.bite.BitableProperties;
import dhyces.dinnerplate.capability.fluid.MeasuredFluidCapability;
import dhyces.dinnerplate.capability.mixed.MixedCapability;
import dhyces.dinnerplate.inventory.MixedInventory;
import dhyces.dinnerplate.item.BitableItem;
import dhyces.dinnerplate.item.MockFoodItem;
import dhyces.dinnerplate.item.PlateItem;
import dhyces.dinnerplate.item.RenderableNBTBlockItem;
import dhyces.dinnerplate.client.render.item.MeasuringCupItemRenderer;
import dhyces.dinnerplate.client.render.item.MixingBowlItemRenderer;
import dhyces.dinnerplate.util.FluidHelper;
import net.minecraft.core.Registry;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ItemRegistry {

	private static final DeferredRegister<Item> ITEM_REGISTER = DeferredRegister.create(ForgeRegistries.ITEMS, DinnerPlate.MODID);
	public static final List<Pair<Supplier<Item>, Float>> COMPOSTABLES = new LinkedList<>();

	public static final RegistryObject<Item> WHITE_PLATE_ITEM;
	public static final RegistryObject<Item> ORANGE_PLATE_ITEM;
	public static final RegistryObject<Item> MAGENTA_PLATE_ITEM;
	public static final RegistryObject<Item> LIGHT_BLUE_PLATE_ITEM;
	public static final RegistryObject<Item> YELLOW_PLATE_ITEM;
	public static final RegistryObject<Item> LIME_PLATE_ITEM;
	public static final RegistryObject<Item> PINK_PLATE_ITEM;
	public static final RegistryObject<Item> GRAY_PLATE_ITEM;
	public static final RegistryObject<Item> LIGHT_GRAY_PLATE_ITEM;
	public static final RegistryObject<Item> CYAN_PLATE_ITEM;
	public static final RegistryObject<Item> PURPLE_PLATE_ITEM;
	public static final RegistryObject<Item> BLUE_PLATE_ITEM;
	public static final RegistryObject<Item> BROWN_PLATE_ITEM;
	public static final RegistryObject<Item> GREEN_PLATE_ITEM;
	public static final RegistryObject<Item> RED_PLATE_ITEM;
	public static final RegistryObject<Item> BLACK_PLATE_ITEM;
	
	public static final RegistryObject<Item> MIXING_BOWL_ITEM;
	public static final RegistryObject<Item> MEASURING_CUP_ITEM;

	public static final RegistryObject<Item> MOCK_FOOD_ITEM;
	public static final RegistryObject<Item> BITABLE_ITEM;

	public static final RegistryObject<Item> CUT_CARROT_ITEM;

	public static final RegistryObject<Item> MUSHROOM_STEW_BUCKET;
	public static final RegistryObject<Item> BEETROOT_SOUP_BUCKET;
	public static final RegistryObject<Item> RABBIT_STEW_BUCKET;

	public static void register(IEventBus bus) {
		ITEM_REGISTER.register(bus);
		bus.addListener(ItemRegistry::registerCompostables);
	}

	private static RegistryObject<Item> register(String id, Supplier<Item> supplier) {
		return ITEM_REGISTER.register(id, supplier);
	}

	public static RegistryObject<Item> registerCompostable(String id, Supplier<Item> item, float value) {
		var obj = register(id, item);
		COMPOSTABLES.add(Pair.of(obj, value));
		return obj;
	}

	static {
		WHITE_PLATE_ITEM = register("white_plate", () -> new PlateItem(BlockRegistry.WHITE_PLATE_BLOCK.get(), new Item.Properties().stacksTo(8).tab(DinnerPlate.tab)));
		ORANGE_PLATE_ITEM = register("orange_plate", () -> new PlateItem(BlockRegistry.ORANGE_PLATE_BLOCK.get(), new Item.Properties().stacksTo(8).tab(DinnerPlate.tab)));
		MAGENTA_PLATE_ITEM = register("magenta_plate", () -> new PlateItem(BlockRegistry.MAGENTA_PLATE_BLOCK.get(), new Item.Properties().stacksTo(8).tab(DinnerPlate.tab)));
		LIGHT_BLUE_PLATE_ITEM = register("light_blue_plate", () -> new PlateItem(BlockRegistry.LIGHT_BLUE_PLATE_BLOCK.get(), new Item.Properties().stacksTo(8).tab(DinnerPlate.tab)));
		YELLOW_PLATE_ITEM = register("yellow_plate", () -> new PlateItem(BlockRegistry.YELLOW_PLATE_BLOCK.get(), new Item.Properties().stacksTo(8).tab(DinnerPlate.tab)));
		LIME_PLATE_ITEM = register("lime_plate", () -> new PlateItem(BlockRegistry.LIME_PLATE_BLOCK.get(), new Item.Properties().stacksTo(8).tab(DinnerPlate.tab)));
		PINK_PLATE_ITEM = register("pink_plate", () -> new PlateItem(BlockRegistry.PINK_PLATE_BLOCK.get(), new Item.Properties().stacksTo(8).tab(DinnerPlate.tab)));
		GRAY_PLATE_ITEM = register("gray_plate", () -> new PlateItem(BlockRegistry.GRAY_PLATE_BLOCK.get(), new Item.Properties().stacksTo(8).tab(DinnerPlate.tab)));
		LIGHT_GRAY_PLATE_ITEM = register("light_gray_plate", () -> new PlateItem(BlockRegistry.LIGHT_GRAY_PLATE_BLOCK.get(), new Item.Properties().stacksTo(8).tab(DinnerPlate.tab)));
		CYAN_PLATE_ITEM = register("cyan_plate", () -> new PlateItem(BlockRegistry.CYAN_PLATE_BLOCK.get(), new Item.Properties().stacksTo(8).tab(DinnerPlate.tab)));
		PURPLE_PLATE_ITEM = register("purple_plate", () -> new PlateItem(BlockRegistry.PURPLE_PLATE_BLOCK.get(), new Item.Properties().stacksTo(8).tab(DinnerPlate.tab)));
		BLUE_PLATE_ITEM = register("blue_plate", () -> new PlateItem(BlockRegistry.BLUE_PLATE_BLOCK.get(), new Item.Properties().stacksTo(8).tab(DinnerPlate.tab)));
		BROWN_PLATE_ITEM = register("brown_plate", () -> new PlateItem(BlockRegistry.BROWN_PLATE_BLOCK.get(), new Item.Properties().stacksTo(8).tab(DinnerPlate.tab)));
		GREEN_PLATE_ITEM = register("green_plate", () -> new PlateItem(BlockRegistry.GREEN_PLATE_BLOCK.get(), new Item.Properties().stacksTo(8).tab(DinnerPlate.tab)));
		RED_PLATE_ITEM = register("red_plate", () -> new PlateItem(BlockRegistry.RED_PLATE_BLOCK.get(), new Item.Properties().stacksTo(8).tab(DinnerPlate.tab)));
		BLACK_PLATE_ITEM = register("black_plate", () -> new PlateItem(BlockRegistry.BLACK_PLATE_BLOCK.get(), new Item.Properties().stacksTo(8).tab(DinnerPlate.tab)));
		
		MIXING_BOWL_ITEM = register("mixing_bowl", () -> new RenderableNBTBlockItem(
																(stack, tag) -> new MixedCapability.Item(stack, new MixedInventory(9)), 
																new MixingBowlItemRenderer(),
																BlockRegistry.MIXING_BOWL_BLOCK.get(),
																new Item.Properties().stacksTo(1).tab(DinnerPlate.tab)));
		MEASURING_CUP_ITEM = register("measuring_cup", () -> new RenderableNBTBlockItem(
																(stack, tag) -> new MeasuredFluidCapability(stack, 1000, FluidHelper.PILE),
																new MeasuringCupItemRenderer(),
																BlockRegistry.MEASURING_CUP_BLOCK.get(),
																new Item.Properties().stacksTo(1).tab(DinnerPlate.tab)));

		MOCK_FOOD_ITEM = register("mock_food", MockFoodItem::new);
		BITABLE_ITEM = register("bitable_item", () -> new BitableItem(new BitableProperties.Builder().build(), new Item.Properties()));

		CUT_CARROT_ITEM = registerCompostable("cut_carrot", () -> new BitableItem(new BitableProperties.Builder()
																		.addSimpleBite(1, 0.25f)
																		.build(),
																	new Item.Properties().tab(DinnerPlate.tab)),
															0.1f);

		MUSHROOM_STEW_BUCKET = register("mushroom_stew_bucket", () -> simpleBucket(FluidRegistry.MUSHROOM_STEW_FLUID));
		BEETROOT_SOUP_BUCKET = register("beetroot_soup_bucket", () -> simpleBucket(FluidRegistry.BEETROOT_SOUP_FLUID));
		RABBIT_STEW_BUCKET = register("rabbit_stew_bucket", () -> simpleBucket(FluidRegistry.RABBIT_STEW_FLUID));
	}

	//TODO: ensure this event is a good place to add compostable items
	public static void registerCompostables(FMLCommonSetupEvent event) {
		event.enqueueWork(() -> COMPOSTABLES.forEach(ItemRegistry::addToComposter));
	}
	
	private static void addToComposter(Pair<Supplier<Item>, Float> compostPair) {
		ComposterBlock.COMPOSTABLES.put(compostPair.getFirst().get(), (float)compostPair.getSecond());
	}

	private static Item simpleBucket(Supplier<? extends Fluid> fluid) {
		return new BucketItem(fluid, new Item.Properties().stacksTo(1).tab(DinnerPlate.tab));

	}
}
