package dhyces.dinnerplate.render.block;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Quaternion;

import dhyces.dinnerplate.block.PlateBlock;
import dhyces.dinnerplate.blockentity.PlateBlockEntity;
import dhyces.dinnerplate.util.ResourceHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms.TransformType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class PlateBlockRenderer implements BlockEntityRenderer<PlateBlockEntity> {

	public PlateBlockRenderer(BlockEntityRendererProvider.Context pContext) {
	}
	
	@Override
	public void render(PlateBlockEntity pBlockEntity, float pPartialTick, PoseStack poseStack,
			MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay) {
		if (!pBlockEntity.hasItem())
			return;
		var item = pBlockEntity.getLastItem();
		poseStack.pushPose();
		poseStack.scale(.5F, .5F, .5F);
		poseStack.translate(1, 0.1563, 1);
		if (!Minecraft.getInstance().getItemRenderer().getModel(item, (Level)null, (LivingEntity)null, 0).isGui3d()) {
			poseStack.mulPose(new Quaternion(90, 0, pBlockEntity.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING).toYRot(), true));
		} else {
			poseStack.mulPose(new Quaternion(0, -pBlockEntity.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING).toYRot(), 0, true));
			poseStack.translate(0, .25, 0);
		}
		var model = Minecraft.getInstance().getModelManager().getModel(ResourceHelper.inventoryModel(item.getItem().getRegistryName()));
		Minecraft.getInstance().getItemRenderer().render(item, TransformType.NONE, false, poseStack, pBufferSource, pPackedLight, pPackedOverlay, model);
		//Minecraft.getInstance().getItemRenderer().renderStatic((LivingEntity)null, item, TransformType.FIXED, false, poseStack, pBufferSource, pBlockEntity.getLevel(), pPackedLight, pPackedOverlay, 0);
		poseStack.popPose();
	}

}
