package dhyces.dinnerplate.render.item;

import com.mojang.blaze3d.platform.GlStateManager.DestFactor;
import com.mojang.blaze3d.platform.GlStateManager.SourceFactor;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import dhyces.dinnerplate.render.util.IFluidRenderer;
import dhyces.dinnerplate.render.util.RectPrism;
import dhyces.dinnerplate.render.util.RenderTypes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemTransforms.TransformType;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

public class MeasuringCupItemRenderer extends SimpleItemRenderer implements IFluidRenderer {

	@Override
	public void render(ItemStack pStack, TransformType pTransformType, PoseStack pPoseStack, MultiBufferSource pBuffer,
			int pPackedLight, int pPackedOverlay) {
		pPoseStack.pushPose();
		RenderSystem.enableBlend();
		RenderSystem.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);

		var model = Minecraft.getInstance().getItemRenderer().getItemModelShaper().getItemModel(pStack);
		model = model.getOverrides().resolve(model, pStack, clientLevel(), clientPlayer(), pPackedOverlay);
		var buffer = pBuffer.getBuffer(RenderType.entityTranslucentCull(TextureAtlas.LOCATION_BLOCKS));

		for (BakedQuad q : model.getQuads(null, null, null, null)) {
			buffer.putBulkData(pPoseStack.last(), q, 1f, 1f, 1f, pPackedLight, pPackedOverlay);
		}
		pPoseStack.popPose();

		var cap = pStack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY);
		if (!cap.isPresent())
			return;

		var fluid = fluidArray(cap.resolve().get().getFluidInTank(0));
		var fluidHeight = ((fluid[0].getAmount()/100) * 0.4375f);
		var prism = RectPrism.fromPixel(6, 1, 6).toPixel(10, 1.1 + fluidHeight, 10);
		tessalateItemFluids(fluid, prism, pBuffer.getBuffer(RenderTypes.getFluid()), pPoseStack, pPackedOverlay, pPackedLight, Direction.values());
	}
}
