package dhyces.dinnerplate.client.render.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Quaternion;

import dhyces.dinnerplate.blockentity.PlateBlockEntity;
import dhyces.dinnerplate.client.render.util.IRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms.TransformType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider.Context;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class PlateBlockRenderer extends SimpleBlockRenderer<PlateBlockEntity> implements IRenderer {

	public PlateBlockRenderer(Context context) {
		super(context);
	}

	@Override
	public void render(PlateBlockEntity pBlockEntity, float pPartialTick, PoseStack poseStack,
			MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay) {
		if (!pBlockEntity.hasItem())
			return;
		var item = pBlockEntity.getLastItem();
		var model = getResolvedItemModel(item);
		poseStack.pushPose();
		poseStack.scale(.5F, .5F, .5F);
		poseStack.translate(1, 0.1563, 1);
		if (!model.isGui3d()) {
			poseStack.mulPose(new Quaternion(90, 180, -pBlockEntity.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING).toYRot(), true));
		} else {
			poseStack.mulPose(new Quaternion(0, -pBlockEntity.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING).toYRot(), 0, true));
			poseStack.scale(0.5f, 0.5f, 0.5f);
			poseStack.translate(0, .45, 0);
			poseStack.mulPose(new Quaternion(0, 180, 0, true));
		}
		Minecraft.getInstance().getItemRenderer().render(item, TransformType.NONE, false, poseStack, pBufferSource, pPackedLight, pPackedOverlay, model);
		poseStack.popPose();
	}

}
