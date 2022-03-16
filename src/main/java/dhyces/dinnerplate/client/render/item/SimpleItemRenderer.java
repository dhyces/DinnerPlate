package dhyces.dinnerplate.client.render.item;

import java.util.Random;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import dhyces.dinnerplate.client.render.util.IRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms.TransformType;
import net.minecraft.world.item.ItemStack;

public abstract class SimpleItemRenderer extends BlockEntityWithoutLevelRenderer implements IRenderer {

	public SimpleItemRenderer() {
		super(Minecraft.getInstance() == null ? null : Minecraft.getInstance().getBlockEntityRenderDispatcher(),
				Minecraft.getInstance() == null ?  null : Minecraft.getInstance().getEntityModels());
	}

	@Override
	public void renderByItem(ItemStack pStack, TransformType pTransformType, PoseStack pPoseStack,
			MultiBufferSource pBuffer, int pPackedLight, int pPackedOverlay) {
		render(pStack, pTransformType, pPoseStack, pBuffer, pPackedLight, pPackedOverlay);
	}

	public abstract void render(ItemStack stack, TransformType transformType, PoseStack poseStack,
			MultiBufferSource bufferSource, int packedLight, int packedOverlay);
}
