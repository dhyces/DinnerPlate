package dhyces.dinnerplate.render.item;

import org.lwjgl.opengl.GL20;

import com.mojang.blaze3d.platform.GlStateManager.DestFactor;
import com.mojang.blaze3d.platform.GlStateManager.SourceFactor;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import dhyces.dinnerplate.render.util.IFluidRenderer;
import dhyces.dinnerplate.render.util.RectPrism;
import dhyces.dinnerplate.render.util.RenderTypes;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemTransforms.TransformType;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;

public class MixingBowlItemRenderer extends SimpleItemRenderer implements IFluidRenderer {

	@Override
	public void render(ItemStack pStack, TransformType pTransformType, PoseStack pPoseStack, MultiBufferSource pBuffer,
			int pPackedLight, int pPackedOverlay) {
		pPoseStack.pushPose();
		renderItem(pStack, pPoseStack, pBuffer.getBuffer(ItemBlockRenderTypes.getRenderType(pStack, true)), pPackedLight, pPackedOverlay);
		var cap = FluidUtil.getFluidHandler(pStack);
		if (cap.isPresent()) {
			var fluidHandler = cap.resolve().get();
			System.out.println(fluidHandler.getTanks());
			tessalateItemFluids(fluidArray(fluidHandler.getFluidInTank(0)),
					RectPrism.fromPixel(3, 2, 3).toPixel(13, 2.1 + (fluidHandler.getTanks() * 0.4375f), 13),
					pBuffer.getBuffer(RenderType.entityTranslucentCull(atlas())), pPoseStack, pPackedOverlay, pPackedLight, Direction.UP);
		}
		pPoseStack.popPose();
	}
}
