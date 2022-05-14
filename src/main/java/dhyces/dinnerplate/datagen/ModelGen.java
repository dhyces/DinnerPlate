package dhyces.dinnerplate.datagen;

import dhyces.dinnerplate.DinnerPlate;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModelGen extends ItemModelProvider {

	private final ModelFile BUILT_IN_GENERATED;

	public ModelGen(DataGenerator generator, String modid, ExistingFileHelper existingFileHelper) {
		super(generator, modid, existingFileHelper);
		BUILT_IN_GENERATED = getExistingFile(new ResourceLocation("item/generated"));
	}

	@Override
	protected void registerModels() {
		registerBitten();
		registerPlates();
	}
	
	private void registerPlates() {
		plateBatch(modLoc("block/white_plate"));
		plateBatch(modLoc("block/orange_plate"));
		plateBatch(modLoc("block/magenta_plate"));
		plateBatch(modLoc("block/light_blue_plate"));
		plateBatch(modLoc("block/yellow_plate"));
		plateBatch(modLoc("block/lime_plate"));
		plateBatch(modLoc("block/pink_plate"));
		plateBatch(modLoc("block/gray_plate"));
		plateBatch(modLoc("block/light_gray_plate"));
		plateBatch(modLoc("block/cyan_plate"));
		plateBatch(modLoc("block/purple_plate"));
		plateBatch(modLoc("block/blue_plate"));
		plateBatch(modLoc("block/brown_plate"));
		plateBatch(modLoc("block/green_plate"));
		plateBatch(modLoc("block/red_plate"));
		plateBatch(modLoc("block/black_plate"));
		
	}
	
	private void plateBatch(ResourceLocation location) {
		put(plateBlock(location, location, modLoc("block/plate")));
		put(plateBlock(modLoc(location.getPath() + "_stacked_1"), location, modLoc("block/plate_stacked_1")));
		put(plateBlock(modLoc(location.getPath() + "_stacked_2"), location, modLoc("block/plate_stacked_2")));
		put(plateBlock(modLoc(location.getPath() + "_stacked_3"), location, modLoc("block/plate_stacked_3")));
		put(plateBlock(modLoc(location.getPath() + "_stacked_4"), location, modLoc("block/plate_stacked_4")));
		put(plateBlock(modLoc(location.getPath() + "_stacked_5"), location, modLoc("block/plate_stacked_5")));
		put(plateBlock(modLoc(location.getPath() + "_stacked_6"), location, modLoc("block/plate_stacked_6")));
		put(plateBlock(modLoc(location.getPath() + "_stacked_7"), location, modLoc("block/plate_stacked_7")));
	}
	
	private void registerBitten() {
		put(threeBite(bittenRL("cooked_beef")));
		put(threeBite(bittenRL("baked_potato")));
		put(threeBite(bittenRL("apple")));
		put(threeBite(bittenRL("carrot")));
		put(threeBite(bittenRL("cookie")));
		put(threeBite(bittenRL("bread")));
		put(threeBite(bittenRL("pumpkin_pie")));
		put(threeBite(bittenRL("honey_bottle")));
		put(threeBite(bittenRL("beef")));
		put(threeBite(bittenRL("beetroot_soup")));
		put(threeBite(bittenRL("beetroot")));
		put(threeBite(bittenRL("chicken")));
		put(threeBite(bittenRL("potato")));
		put(threeBite(bittenRL("melon_slice")));
		put(threeBite(bittenRL("glow_berries")));
		put(threeBite(bittenRL("sweet_berries")));
		put(threeBite(bittenRL("mushroom_stew")));
		put(threeBite(bittenRL("suspicious_stew")));
		put(threeBite(bittenRL("golden_carrot")));
		put(threeBite(bittenRL("golden_apple")));
		put(threeBite(bittenRL("mutton")));
		put(threeBite(bittenRL("cooked_mutton")));

		put(withExistingParent("item/bitten/bitten_dried_kelp", "dried_kelp"));

		put(withExistingParent("item/bitten/bitten_mock_food", "air"));
	}

	public void put(ItemModelBuilder builder) {
		generatedModels.put(builder.getLocation(), builder);
	}

	public ItemModelBuilder plateBlock(ResourceLocation location, ResourceLocation texture, ResourceLocation parent) {
		return withExistingParent(location.toString(), parent)
				.texture("0", texture)
				.texture("particle", texture);
	}
	
	public ResourceLocation bittenRL(String item) {
		return new ResourceLocation(modid, "bitten_" + item);
	}

	public ItemModelBuilder threeBite(ResourceLocation location) {
		return bittenModel(location, 0.5f);
	}

	public ItemModelBuilder bittenModel(ResourceLocation location, float predicateStep) {
		var builder = new ItemModelBuilder(new ResourceLocation(location.getNamespace(), "item/bitten/" + location.getPath()), existingFileHelper)
				.parent(BUILT_IN_GENERATED);
		for (float f = 0, i = 0; f < 1.0f; f += predicateStep, i += 1) {
			builder.override()
			.predicate(new ResourceLocation(DinnerPlate.MODID, "bites"), f)
			.model(predicateBittenModel(location, (int)i))
			.end();
		}
		return builder;
	}

	public ItemModelBuilder predicateBittenModel(ResourceLocation location, int biteCount) {
		return singleTexture("item/bitten/" + location.getPath() + "_" + biteCount,
							 BUILT_IN_GENERATED.getLocation(),
							 "layer0",
							 modLoc("item/" + location.getPath() + "_" + biteCount));
	}
}
