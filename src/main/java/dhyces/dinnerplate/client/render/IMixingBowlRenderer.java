package dhyces.dinnerplate.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import dhyces.dinnerplate.client.render.util.IFluidRenderer;
import dhyces.dinnerplate.client.render.util.RectPrism;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemTransforms.TransformType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.RenderProperties;
import net.minecraftforge.fluids.FluidStack;

import java.util.Random;

public interface IMixingBowlRenderer extends IFluidRenderer {

	public default void renderItems(PoseStack poseStack, MultiBufferSource source, float fluidHeight, int packedLight, int packedOverlay, ItemStack... stacks) {
		poseStack.pushPose();
		poseStack.translate(fromPixel(7), fromPixel(fluidHeight) + 0.075, fromPixel(7));
		poseStack.scale(.25f, .25f, .25f);
		for (int i = 0; i < stacks.length; i++) {
			Random r = new Random(i);
			r.nextDouble();
			double nd = r.nextDouble(0.8175);
			double nd1 = r.nextDouble(0.8175);
			boolean nb = r.nextBoolean();
			boolean nb1 = r.nextBoolean();
			nd *= Boolean.compare(nb, !nb);
			nd1 *= Boolean.compare(nb1, !nb1);
			var item = stacks[i];
			var itemModel = Minecraft.getInstance().getItemRenderer().getModel(item, clientLevel(), clientPlayer(), 0);
			poseStack.pushPose();
			// TODO: rotations
			poseStack.translate(nd, 0, nd1);
			poseStack.mulPose(doubleQuaternion(nd * 50, nd * 50, nd * 50, true));
			if (itemModel.isCustomRenderer()) {
				RenderProperties.get(item).getItemStackRenderer().renderByItem(item, TransformType.NONE, poseStack, source, packedLight, packedOverlay);
			} else {
				Minecraft.getInstance().getItemRenderer().renderModelLists(itemModel, item, packedLight, packedOverlay, poseStack, source.getBuffer(RenderType.itemEntityTranslucentCull(atlas())));
			}
			poseStack.popPose();
		}
		poseStack.popPose();
	}
	
	public default void renderFluids(BlockPos pos, MultiBufferSource source, PoseStack poseStack, float fluidHeight, float partialTick, int packedLight, FluidStack... fluids) {
		tessalateFluids(fluids, pos, RectPrism.fromPixel(3, 2, 3).toPixel(13, 2.1 + fluidHeight, 13),
				source.getBuffer(RenderType.itemEntityTranslucentCull(atlas())), poseStack, partialTick, packedLight, Direction.UP);
	}
	
}
