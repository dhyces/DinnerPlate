package dhyces.dinnerplate.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.model.BakedModelWrapper;

@OnlyIn(Dist.CLIENT)
public class EmptyCustomModel extends BakedModelWrapper<BakedModel> {

    public EmptyCustomModel() {
        super(Minecraft.getInstance().getModelManager().getMissingModel());
    }

    @Override
    public boolean isGui3d() {
        return false;
    }


    @Override
    public boolean isCustomRenderer() {
        return true;
    }

    @Override
    public BakedModel applyTransform(ItemDisplayContext displayContext, PoseStack poseStack, boolean applyLeftHandTransform) {
        return this;
    }
}
