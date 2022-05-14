package dhyces.dinnerplate.client.render.item;

import com.mojang.blaze3d.vertex.PoseStack;
import dhyces.dinnerplate.client.render.IMixingBowlRenderer;
import dhyces.dinnerplate.client.render.util.IFluidRenderer;
import dhyces.dinnerplate.util.Util;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemTransforms.TransformType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.items.CapabilityItemHandler;

public class MixingBowlItemRenderer extends SimpleItemRenderer implements IMixingBowlRenderer, IFluidRenderer {

	@Override
	public void render(ItemStack pStack, TransformType pTransformType, PoseStack pPoseStack, MultiBufferSource pBuffer,
			int pPackedLight, int pPackedOverlay) {
		pPoseStack.pushPose();
		renderItem(pStack, pPoseStack, pBuffer.getBuffer(RenderType.entityTranslucentCull(atlas())), pPackedLight, pPackedOverlay);
		
		var fluidCap = FluidUtil.getFluidHandler(pStack);
		float fluidHeight = fluidCap.isPresent() ? fluidCap.map(c -> c.getTanks() * 0.4375f).get() : 0;
		var itemCap = pStack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY);
		
		if (itemCap.isPresent()) {
			var itemHandler = itemCap.resolve().get();
			renderItems(pPoseStack, pBuffer, fluidHeight, pPackedLight, pPackedOverlay, Util.streamOf(itemHandler::getStackInSlot, itemHandler.getSlots()).toArray(ItemStack[]::new));
		}
		if (fluidCap.isPresent()) {
			var fluidHandler = fluidCap.resolve().get();
			renderFluids(BlockPos.ZERO, pBuffer, pPoseStack, fluidHeight, pPackedOverlay, pPackedLight, Util.streamOf(fluidHandler::getFluidInTank, fluidHandler.getTanks()).toArray(FluidStack[]::new));
		}
		pPoseStack.popPose();
	}
}
