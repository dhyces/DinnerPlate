package dhyces.dinnerplate.render.block;

import java.util.Random;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Quaternion;

import dhyces.dinnerplate.blockentity.MixingBowlBlockEntity;
import dhyces.dinnerplate.render.util.IFluidRenderer;
import dhyces.dinnerplate.render.util.RenderTypes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms.TransformType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

// TODO: Revise this renderer, especially the fluids
@OnlyIn(Dist.CLIENT)
public class MixingBowlBlockRenderer implements BlockEntityRenderer<MixingBowlBlockEntity>, IFluidRenderer {

	public MixingBowlBlockRenderer(BlockEntityRendererProvider.Context context) {
	}

	@Override
	public void render(MixingBowlBlockEntity bEntity, float pPartialTick, PoseStack poseStack,
			MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay) {
		if (!bEntity.hasLevel() || bEntity.getBlockPos() == null)
			return;
		float fluidHeight = tessalateFluids(bEntity, pBufferSource.getBuffer(RenderTypes.getFluid()), poseStack, pPartialTick, pPackedLight, Direction.UP);
		if (bEntity.hasItem()) {
			poseStack.pushPose();
			poseStack.translate(fromPixel(8), fromPixel(2) + fromPixel(fluidHeight), fromPixel(8));
			poseStack.scale(.25f, .25f, .25f);
			for (int i = 0; i < bEntity.getItemSize(); i++) {
				Random r = new Random(i);
				r.nextDouble();
				double nd = r.nextDouble(0.8125);
				double nd1 = r.nextDouble(0.8125);
				boolean nb = r.nextBoolean();
				boolean nb1 = r.nextBoolean();
				nd *= Boolean.compare(nb, !nb);
				nd1 *= Boolean.compare(nb1, !nb1);
				var item = bEntity.getItem(i); 
				poseStack.pushPose();
				poseStack.translate(nd, 0, nd1);
				poseStack.mulPose(new Quaternion((float)nd1 * 100, (float)nd1 * 100, (float)nd * 100, false));
				Minecraft.getInstance().getItemRenderer().render(item, TransformType.NONE, false, poseStack, pBufferSource, pPackedLight, pPackedOverlay, Minecraft.getInstance().getItemRenderer().getModel(item, bEntity.getLevel(), Minecraft.getInstance().player, 0));
				poseStack.popPose();
			}
			poseStack.popPose();
		}
	}
	
	private float fromPixel(float px) {
		return px / 16f;
	}
}
