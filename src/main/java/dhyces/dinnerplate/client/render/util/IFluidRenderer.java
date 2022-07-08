package dhyces.dinnerplate.client.render.util;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidStack;

public interface IFluidRenderer extends IRenderer {

    default void tessalateItemFluids(FluidStack[] fluids, Vec3 startPixels, Vec3 endPixels, VertexConsumer pConsumer, PoseStack poseStack,
                                     float partial, int packedLight, Direction... faces) {
        tessalateFluids(fluids, BlockPos.ZERO, startPixels, endPixels, pConsumer, poseStack, partial, packedLight, faces);
    }

    default void tessalateItemFluids(FluidStack[] fluids, RectPrism prism, VertexConsumer pConsumer, PoseStack poseStack,
                                     float partial, int packedLight, Direction... faces) {
        tessalateFluids(fluids, BlockPos.ZERO, prism, pConsumer, poseStack, partial, packedLight, faces);
    }

    default void tessalateFluids(FluidStack[] fluids, BlockPos pos, Vec3 startPixels, Vec3 endPixels, VertexConsumer pConsumer,
                                 PoseStack poseStack, float partial, int packedLight, Direction... faces) {

        var prism = RectPrism.fromPixel(startPixels.x, startPixels.y, startPixels.z).toPixel(endPixels.x, endPixels.y, endPixels.z);
        tessalateFluids(fluids, pos, prism, pConsumer, poseStack, partial, packedLight, faces);
    }

    default void tessalateFluids(FluidStack[] fluids, BlockPos pos, RectPrism prism, VertexConsumer pConsumer, PoseStack poseStack,
                                 float partial, int packedLight, Direction... faces) {
        if (fluids.length == 0)
            return;
        FluidStack stack = fluids[0];
        Fluid fluid = stack.getFluid();
        Level level = clientLevel();
        TextureAtlasSprite stillSprite = ForgeHooksClient.getFluidSprites(level, pos, fluid.defaultFluidState())[0];
        int packedColor = IClientFluidTypeExtensions.of(fluid).getTintColor(fluid.defaultFluidState(), level, pos);
        for (Direction side : faces) {
            var verts = prism.getVertices(side);
            var positive = side.getAxisDirection().equals(Direction.AxisDirection.POSITIVE);
            var horizontal = side.getAxis().getPlane().equals(Direction.Plane.HORIZONTAL);
            var xAxis = side.getAxis().equals(Direction.Axis.X);

            var u1 = stillSprite.getU0();
            var u2 = stillSprite.getU1();
            var v1 = stillSprite.getV0();
            var v2 = stillSprite.getV1();

            // TODO: these values may still not be right, but they look right.
            if (positive && horizontal || side.equals(Direction.DOWN)) {
                verts.swap(1, 3);
                verts.rotate(1);
            }
            if (xAxis) {
                verts.rotate(1);
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
