package dhyces.dinnerplate.util;

import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;

public class ModelHelper {
	
	public static ModelResourceLocation inventoryModel(ResourceLocation location) {
		return new ModelResourceLocation(location, "inventory");
	}

}
