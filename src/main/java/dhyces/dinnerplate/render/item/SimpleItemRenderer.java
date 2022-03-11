package dhyces.dinnerplate.render.item;

import java.util.Random;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import dhyces.dinnerplate.render.util.IRenderer;
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

	public abstract void render(ItemStack pStack, TransformType pTransformType, PoseStack pPoseStack,
			MultiBufferSource pBuffer, int pPackedLight, int pPackedOverlay);

	public void renderItem(ItemStack stack, PoseStack poseStack, VertexConsumer consumer, int packedLight, int packedOverlay) {
		var model = Minecraft.getInstance().getItemRenderer().getItemModelShaper().getItemModel(stack);
		model = model.getOverrides().resolve(model, stack, clientLevel(), clientPlayer(), new Random(42L).nextInt());
		Minecraft.getInstance().getItemRenderer().renderModelLists(model, stack, packedLight, packedOverlay, poseStack, consumer);
	}
}
