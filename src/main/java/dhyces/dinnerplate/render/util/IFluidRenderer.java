package dhyces.dinnerplate.render.util;

import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.fluids.FluidStack;

public interface IFluidRenderer extends IRenderer {

	default void tessalateFluids(FluidStack[] fluids, Level level, BlockPos pos, Vec3 start, Vec3 end,
			VertexConsumer pConsumer, PoseStack poseStack, float partial, int packedLight, Direction... faces) {
		if (fluids.length == 0)
			return;
		FluidStack stack = fluids[0];
		Fluid fluid = stack.getFluid();
		TextureAtlasSprite stillSprite = ForgeHooksClient.getFluidSprites(level, pos, fluid.defaultFluidState())[0];
		int packedColor = fluid.getAttributes().getColor(level, pos);
		RectPrism p = RectPrism.fromPixel(start.x, start.y, start.z).toPixel(end.x, end.y, end.z);
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
	}
	
	default FluidStack[] fluidArray(FluidStack... fluidStacks) {
		return fluidStacks;
	}
}
