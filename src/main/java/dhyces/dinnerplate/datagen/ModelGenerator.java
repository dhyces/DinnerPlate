package dhyces.dinnerplate.datagen;

import dhyces.dinnerplate.DinnerPlate;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModelGenerator extends ItemModelProvider {

	private final ModelFile BUILT_IN_GENERATED;
	
	public ModelGenerator(DataGenerator generator, String modid, ExistingFileHelper existingFileHelper) {
		super(generator, modid, existingFileHelper);
		BUILT_IN_GENERATED = getExistingFile(new ResourceLocation("item/generated"));
		
	}

	@Override
	protected void registerModels() {
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
		
		put(withExistingParent("item/bitten/bitten_dried_kelp", "dried_kelp"));
		
		put(withExistingParent("item/bitten/bitten_mock_food", "air"));
	}
	
	public void put(ItemModelBuilder builder) {
		generatedModels.put(builder.getLocation(), builder);
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
