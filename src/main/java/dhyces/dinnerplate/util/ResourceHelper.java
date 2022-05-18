package dhyces.dinnerplate.util;

import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;

public class ResourceHelper {

    public static ModelResourceLocation inventoryModel(ResourceLocation location) {
        return new ModelResourceLocation(location, "inventory");
    }

    public static ResourceLocation atlasitemTextureRL(ResourceLocation registryRL) {
        return new ResourceLocation(registryRL.getNamespace(), "item/" + registryRL.getPath());
    }


    public static ResourceLocation resourceItemTextureRL(ResourceLocation atlasRL) {
        return new ResourceLocation(atlasRL.getNamespace(), "textures/" + atlasRL.getPath() + ".png");
    }
}
