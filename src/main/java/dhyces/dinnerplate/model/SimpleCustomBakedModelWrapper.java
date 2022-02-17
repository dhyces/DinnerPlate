package dhyces.dinnerplate.model;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.renderer.block.model.ItemTransforms.TransformType;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.model.BakedModelWrapper;

@OnlyIn(Dist.CLIENT)
public class SimpleCustomBakedModelWrapper extends BakedModelWrapper<BakedModel>{

	public SimpleCustomBakedModelWrapper(BakedModel originalModel) {
		super(originalModel);
	}
	
//	@Override
//	public boolean doesHandlePerspectives() {
//		return false;
//	}

	@Override
	public boolean isCustomRenderer() {
		return true;
	}
	
	@Override
	public BakedModel handlePerspective(TransformType cameraTransformType, PoseStack poseStack) {
		super.handlePerspective(cameraTransformType, poseStack);
		return this;
	}
}
