package dhyces.dinnerplate.render.item;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms.TransformType;
import net.minecraft.world.item.ItemStack;

public abstract class SimpleItemRenderer extends BlockEntityWithoutLevelRenderer {

	public SimpleItemRenderer() {
		super(Minecraft.getInstance() == null ? null : Minecraft.getInstance().getBlockEntityRenderDispatcher(),
				Minecraft.getInstance() == null ?  null : Minecraft.getInstance().getEntityModels());
	}
	
	@Override
	public void renderByItem(ItemStack pStack, TransformType pTransformType, PoseStack pPoseStack,
			MultiBufferSource pBuffer, int pPackedLight, int pPackedOverlay) {
		render(pStack, pTransformType, pPoseStack, pBuffer, pPackedLight, pPackedOverlay);
	}

	public abstract void render(ItemStack pStack, TransformType pTransformType, PoseStack pPoseStack,
			MultiBufferSource pBuffer, int pPackedLight, int pPackedOverlay);
}
