package dhyces.dinnerplate.render.util;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.util.FastColor;
import net.minecraft.world.phys.Vec3;

public interface IRenderer {

	default void renderFace(VertexConsumer pConsumer, PoseStack poseStack, QuadFace face, int color, float u1, float v1, float u2, float v2,
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

	default void vertex(VertexConsumer pConsumer, PoseStack poseStack, Vec3 vert, int color, float pU, float pV, int pPackedLight, Direction face) {
		//System.out.println("side: " + face + " " + vert.x + " " + vert.y + " " + vert.z);
		var r = FastColor.ARGB32.red(color);
		var g = FastColor.ARGB32.green(color);
		var b = FastColor.ARGB32.blue(color);
		var a = FastColor.ARGB32.alpha(color);
		vertex(pConsumer, poseStack, vert, a, r, g, b, pU, pV, pPackedLight, face);
	}
	
	default void vertex(VertexConsumer pConsumer, PoseStack poseStack, Vec3 vert, int r, int g, int b, int a, float pU, float pV, int pPackedLight, Direction face) {
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
}
