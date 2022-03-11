package dhyces.dinnerplate.render.item;

import com.mojang.blaze3d.vertex.PoseStack;

import dhyces.dinnerplate.render.util.IFluidRenderer;
import dhyces.dinnerplate.render.util.RectPrism;
import dhyces.dinnerplate.render.util.RenderTypes;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms.TransformType;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidUtil;

public class MixingBowlItemRenderer extends SimpleItemRenderer implements IFluidRenderer {

	@Override
	public void render(ItemStack pStack, TransformType pTransformType, PoseStack pPoseStack, MultiBufferSource pBuffer,
			int pPackedLight, int pPackedOverlay) {
		pPoseStack.pushPose();
		renderItem(pStack, pPoseStack, pBuffer.getBuffer(ItemBlockRenderTypes.getRenderType(pStack, true)), pPackedLight, pPackedOverlay);
		var cap = FluidUtil.getFluidHandler(pStack);
		if (cap.isPresent())
			tessalateItemFluids(fluidArray(cap.resolve().get().getFluidInTank(0)),
					RectPrism.fromPixel(3, 2, 3).toPixel(13, 2.1 + cap.resolve().get().getTanks() * 0.4375f, 13),
					pBuffer.getBuffer(RenderTypes.getFluid()), pPoseStack, pPackedOverlay, pPackedLight, Direction.UP);
		pPoseStack.popPose();
	}
}
