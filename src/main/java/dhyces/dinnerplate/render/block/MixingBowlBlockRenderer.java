package dhyces.dinnerplate.render.block;

import java.util.Random;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Quaternion;

import dhyces.dinnerplate.blockentity.MixingBowlBlockEntity;
import dhyces.dinnerplate.render.util.QuadFace;
import dhyces.dinnerplate.render.util.RectPrism;
import dhyces.dinnerplate.render.util.RenderTypes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms.TransformType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.FastColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.fluids.FluidStack;

// TODO: Revise this renderer, especially the fluids
@OnlyIn(Dist.CLIENT)
public class MixingBowlBlockRenderer implements BlockEntityRenderer<MixingBowlBlockEntity> {

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
			poseStack.translate(.5, .1875 + fluidHeight, .5);
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
				Minecraft.getInstance().getItemRenderer().render(item, TransformType.FIXED, false, poseStack, pBufferSource, pPackedLight, pPackedOverlay, Minecraft.getInstance().getItemRenderer().getModel(item, bEntity.getLevel(), Minecraft.getInstance().player, 0));
				poseStack.popPose();
			}
			poseStack.popPose();
		}
	}

	private float tessalateFluids(MixingBowlBlockEntity bEntity, VertexConsumer pConsumer, PoseStack poseStack, float partial, int packedLight, Direction... faces) {
		if (!bEntity.hasFluid())
			return 0;
		FluidStack stack = bEntity.getFluidStack(0);
		Fluid fluid = stack.getFluid();
		var fluidLevel = bEntity.getFluidSizeScaled();
		Level level = bEntity.getLevel();
		BlockPos pos = bEntity.getBlockPos();
		TextureAtlasSprite stillSprite = ForgeHooksClient.getFluidSprites(level, pos, fluid.defaultFluidState())[0];
		int packedColor = fluid.getAttributes().getColor(level, pos);
		float fluidHeight = (fluidLevel * .2625f) / 100;
		RectPrism p = RectPrism.fromPixel(3, 2, 3).toPixel(13, 2.1f + fluidHeight, 13);
		for (Direction side : faces) {
			var verts = p.getVertices(side);
			var positive = side.getAxisDirection().equals(Direction.AxisDirection.POSITIVE);
			var horizontal = side.getAxis().getPlane().equals(Direction.Plane.HORIZONTAL);
			var xAxis = side.getAxis().equals(Direction.Axis.X);

			var u1 = stillSprite.getU0();
			var u2 = stillSprite.getU1();
			var v1 = stillSprite.getV0();
			var v2 = stillSprite.getV1();

			if (positive && horizontal || side.equals(Direction.DOWN)) {
				verts.rotate(2);
			}
			if (xAxis) {
				verts.swap(0, 1);
				verts.rotate(1);
				verts.swap(2, 3);
				verts.rotate(2);
			}
			poseStack.pushPose();
			renderFace(pConsumer, poseStack, verts, packedColor, u1, v1, u2, v2, packedLight, side);
			poseStack.popPose();
		}
		return fluidHeight;
	}

	private void renderFace(VertexConsumer pConsumer, PoseStack poseStack, QuadFace face, int color, float u1, float v1, float u2, float v2,
			int packedLight, Direction side) {
		var r = FastColor.ARGB32.red(color);
		var g = FastColor.ARGB32.green(color);
		var b = FastColor.ARGB32.blue(color);
		var a = FastColor.ARGB32.alpha(color);
		vertex(pConsumer, poseStack, face.bottomLeft(), r, g, b, a, u2, v2, packedLight, side);
		vertex(pConsumer, poseStack, face.topLeft(), r, g, b, a, u2, v1, packedLight, side);
		vertex(pConsumer, poseStack, face.topRight(), r, g, b, a, u1, v1, packedLight, side);
		vertex(pConsumer, poseStack, face.bottomRight(), r, g, b, a, u1, v2, packedLight, side);
	}

	private void vertex(VertexConsumer pConsumer, PoseStack poseStack, Vec3 vert, int r, int g, int b, int a, float pU, float pV, int pPackedLight, Direction face) {
		//System.out.println("side: " + face + " " + vert.x + " " + vert.y + " " + vert.z);
		var last = poseStack.last();
		var normal = face.getNormal();
		pConsumer.vertex(last.pose(), (float)vert.x, (float)vert.y, (float)vert.z)
				 .color(r, g, b, a)
				 .uv(pU, pV)
				 .overlayCoords(OverlayTexture.NO_OVERLAY)
				 .uv2(pPackedLight)
				 .normal(last.normal(), normal.getX(), normal.getY(), normal.getZ())
				 .endVertex();
	}

	private void vertex(VertexConsumer pConsumer, PoseStack poseStack, Vec3 vert, int color, float pU, float pV, int pPackedLight, Direction face) {
		//System.out.println("side: " + face + " " + vert.x + " " + vert.y + " " + vert.z);
		var last = poseStack.last();
		var normal = face.getNormal();
		pConsumer.vertex(last.pose(), (float)vert.x, (float)vert.y, (float)vert.z)
				 .color(color)
				 .uv(pU, pV)
				 .overlayCoords(OverlayTexture.NO_OVERLAY)
				 .uv2(pPackedLight)
				 .normal(last.normal(), normal.getX(), normal.getY(), normal.getZ())
				 .endVertex();
	}

	private void vertex(VertexConsumer pConsumer, PoseStack poseStack, double x, double y, double z, float pRed, float pGreen, float pBlue, float alpha, float pU, float pV, int pPackedLight, Direction face) {
		System.out.println("side: " + face + " " + x + " " + y + " " + z);
		pConsumer.vertex(poseStack.last().pose(), (float)x, (float)y, (float)z).color(pRed, pGreen, pBlue, alpha).uv(pU, pV).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(pPackedLight).normal(poseStack.last().normal(), face.getNormal().getX(), face.getNormal().getY(), face.getNormal().getZ()).endVertex();
	}
}
