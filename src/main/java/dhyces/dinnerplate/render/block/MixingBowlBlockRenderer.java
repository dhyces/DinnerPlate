package dhyces.dinnerplate.render.block;

import java.util.Random;

import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.GlStateManager.DestFactor;
import com.mojang.blaze3d.platform.GlStateManager.SourceFactor;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Quaternion;

import dhyces.dinnerplate.blockentity.MixingBowlBlockEntity;
import dhyces.dinnerplate.render.util.IFluidRenderer;
import dhyces.dinnerplate.render.util.RenderTypes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemTransforms.TransformType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider.Context;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.RenderProperties;

// TODO: Revise this renderer, especially the fluids
@OnlyIn(Dist.CLIENT)
public class MixingBowlBlockRenderer extends SimpleBlockRenderer<MixingBowlBlockEntity> implements IFluidRenderer {

	public MixingBowlBlockRenderer(Context context) {
		super(context);
	}

	@Override
	public void render(MixingBowlBlockEntity bEntity, float pPartialTick, PoseStack poseStack,
			MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay) {
		if (!bEntity.hasLevel() || bEntity.getBlockPos() == null)
			return;
		var fluidHeight = ((bEntity.updateRenderable("", 0.05f)/100) * 0.4375f);
		var buffer = pBufferSource.getBuffer(RenderType.itemEntityTranslucentCull(TextureAtlas.LOCATION_BLOCKS));
		if (bEntity.hasItem()) {
			poseStack.pushPose();
			poseStack.translate(fromPixel(6), fromPixel(fluidHeight), fromPixel(6));
			poseStack.scale(.25f, .25f, .25f);
			for (int i = 0; i < bEntity.getItemSize(); i++) {
				Random r = new Random(i);
				r.nextDouble();
				double nd = r.nextDouble(1);
				double nd1 = r.nextDouble(1);
				boolean nb = r.nextBoolean();
				boolean nb1 = r.nextBoolean();
				nd *= Boolean.compare(nb, !nb);
				nd1 *= Boolean.compare(nb1, !nb1);
				var item = bEntity.getItem(i);
				var itemModel = Minecraft.getInstance().getItemRenderer().getModel(item, bEntity.getLevel(), Minecraft.getInstance().player, 0);
				poseStack.pushPose();
				poseStack.translate(nd, 0, nd1);
				if (itemModel.isCustomRenderer())
					RenderProperties.get(item).getItemStackRenderer().renderByItem(item, TransformType.NONE, poseStack, pBufferSource, pPackedLight, pPackedOverlay);
				else
					Minecraft.getInstance().getItemRenderer().renderModelLists(itemModel, item, pPackedLight, pPackedOverlay, poseStack, buffer);
				poseStack.popPose();
			}
			poseStack.popPose();
		}
		if (bEntity.hasFluid())
			tessalateFluids(fluidArray(bEntity.getLastFluid()), bEntity.getBlockPos(), new Vec3(3, 2, 3),
				new Vec3(13, 2.1 + fluidHeight, 13), pBufferSource.getBuffer(RenderTypes.getFluid()), poseStack, 
				pPartialTick, pPackedLight, Direction.UP);
		
	}
	
	private Quaternion doubleQuaternion(double i, double j, double k, boolean bool) {
		return new Quaternion((float)i, (float)j, (float)k, bool);
	}

	private float fromPixel(float px) {
		return px / 16f;
	}
}
