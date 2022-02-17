package dhyces.dinnerplate.datagen;

import dhyces.dinnerplate.registry.FluidRegistry;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.loaders.DynamicBucketModelBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;

public class BucketModelGenerator extends ItemModelProvider {

	public BucketModelGenerator(DataGenerator generator, String modid, ExistingFileHelper existingFileHelper) {
		super(generator, modid, existingFileHelper);
	}

	@Override
	protected void registerModels() {
		bucket("mushroom_stew", FluidRegistry.MUSHROOM_STEW_FLUID.get(), true, true, false, false);
	}
	
	public ItemModelBuilder bucket(String name, Fluid fluid, boolean tint, boolean mask, boolean flip, boolean luminosity) {
		return DynamicBucketModelBuilder.begin(this.getBuilder(name), existingFileHelper)
										.applyTint(tint)
										.coverIsMask(mask)
										.flipGas(flip)
										.applyFluidLuminosity(luminosity)
										.end();
	}

}
