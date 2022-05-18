package dhyces.dinnerplate.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ItemTransforms.TransformType;
import net.minecraft.client.resources.model.BakedModel;
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
    public boolean doesHandlePerspectives() {
        return false;
    }

    @Override
    public boolean isCustomRenderer() {
        return true;
    }

    @Override
    public BakedModel handlePerspective(TransformType cameraTransformType, PoseStack poseStack) {
        return this;
    }
}
