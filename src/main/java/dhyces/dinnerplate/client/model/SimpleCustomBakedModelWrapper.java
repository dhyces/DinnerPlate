package dhyces.dinnerplate.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraftforge.client.model.BakedModelWrapper;

public class SimpleCustomBakedModelWrapper extends BakedModelWrapper<BakedModel> {

    public SimpleCustomBakedModelWrapper(BakedModel originalModel) {
        super(originalModel);
    }

    @Override
    public boolean isCustomRenderer() {
        return true;
    }

    @Override
    public BakedModel applyTransform(ItemDisplayContext displayContext, PoseStack poseStack, boolean applyLeftHandTransform) {
        super.applyTransform(displayContext, poseStack, applyLeftHandTransform);
        return this;
    }
}
