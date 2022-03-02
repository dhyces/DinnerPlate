package dhyces.dinnerplate.render.util;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import dhyces.dinnerplate.blockentity.MixingBowlBlockEntity;
import dhyces.dinnerplate.blockentity.api.IFluidHolder;
import dhyces.dinnerplate.blockentity.api.IRenderableTracker;
import dhyces.dinnerplate.util.FluidHelper;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.fluids.FluidStack;

public interface IFluidRenderer extends IRenderer {

	default <T extends IFluidHolder & IRenderableTracker> float tessalateFluids(T fluidRenderer, Level level, BlockPos pos, Vec3 start, Vec3 end,
			VertexConsumer pConsumer, PoseStack poseStack, float partial, int packedLight, Direction... faces) {
		if (fluidRenderer.hasFluid())
			return 0;
		FluidStack stack = fluidRenderer.getFluidStack(0);
		Fluid fluid = stack.getFluid();
		var fluidLevel = fluidRenderer.getFluidAmount() / FluidHelper.PILE;
		TextureAtlasSprite stillSprite = ForgeHooksClient.getFluidSprites(level, pos, fluid.defaultFluidState())[0];
		int packedColor = fluid.getAttributes().getColor(level, pos);
		float fluidHeight = (fluidLevel * .4375f) + (fluidRenderer.updateRenderable("", 0.05f) * .4375f);
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
			//TODO: These values are not quite right
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
}
