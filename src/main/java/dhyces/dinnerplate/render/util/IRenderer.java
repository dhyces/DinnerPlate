package dhyces.dinnerplate.render.util;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat.Mode;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
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
		var r = FastColor.ARGB32.red(color);
		var g = FastColor.ARGB32.green(color);
		var b = FastColor.ARGB32.blue(color);
		var a = FastColor.ARGB32.alpha(color);
		vertex(pConsumer, poseStack, vert, a, r, g, b, pU, pV, pPackedLight, face);
	}

	default void tessalatorVertex(PoseStack poseStack, Vec3 vert, int r, int g, int b, int a, float pU, float pV, int pPackedLight, Direction face) {
		var last = poseStack.last();
		var normal = face.getNormal();

		Tesselator.getInstance().getBuilder().begin(Mode.QUADS, DefaultVertexFormat.NEW_ENTITY);
		Tesselator.getInstance().getBuilder().vertex(last.pose(), (float)vert.x, (float)vert.y, (float)vert.z)
				 .color(r, g, b, a)
				 .uv(pU, pV)
				 .overlayCoords(OverlayTexture.NO_OVERLAY)
				 .uv2(pPackedLight)
				 .normal(last.normal(), normal.getX(), normal.getY(), normal.getZ())
				 .endVertex();
		Tesselator.getInstance().end();
	}

	default void vertex(VertexConsumer pConsumer, PoseStack poseStack, Vec3 vert, int r, int g, int b, int a, float pU, float pV, int pPackedLight, Direction face) {
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

	public default BakedModel getResolvedItemModel(ItemStack stack) {
		var base = Minecraft.getInstance().getItemRenderer().getItemModelShaper().getItemModel(stack);
		return base.getOverrides().resolve(base, stack, clientLevel(), clientPlayer(), clientLevel().random.nextInt());
	}

	public default ClientLevel clientLevel() {
		return Minecraft.getInstance().level;
	}

	public default LocalPlayer clientPlayer() {
		return Minecraft.getInstance().player;
	}

	public default ResourceLocation atlas() {
		return TextureAtlas.LOCATION_BLOCKS;
	}
}
