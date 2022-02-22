package dhyces.dinnerplate.render.item;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Quaternion;

import dhyces.dinnerplate.DinnerPlate;
import dhyces.dinnerplate.Constants;
import dhyces.dinnerplate.block.PlateBlock;
import dhyces.dinnerplate.util.BlockHelper;
import dhyces.dinnerplate.util.ResourceHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms.TransformType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.RenderProperties;

@OnlyIn(Dist.CLIENT)
public class PlateItemRenderer extends SimpleItemRenderer {

	@Override
	public void renderByItem(ItemStack pStack, TransformType pTransformType, PoseStack pPoseStack,
			MultiBufferSource pBuffer, int pPackedLight, int pPackedOverlay) {
		var model = Minecraft.getInstance().getItemRenderer().getModel(pStack, (Level)null, (LivingEntity)null, 0);
		var hasItem = !BlockHelper.getBlockEntityTag(pStack).getCompound(Constants.TAG_SINGLE_ITEM).isEmpty();
		
		pPoseStack.pushPose();
		var vertexConsumer = ItemRenderer.getFoilBufferDirect(pBuffer, ItemBlockRenderTypes.getRenderType(pStack, true), true, false);
		pPoseStack.pushPose();
		Minecraft.getInstance().getItemRenderer().renderModelLists(model, pStack, pPackedLight, pPackedOverlay, pPoseStack, vertexConsumer);
		pPoseStack.popPose();
		if (hasItem) {
			var platedItem = ItemStack.of(BlockHelper.getBlockEntityTag(pStack).getCompound(Constants.TAG_SINGLE_ITEM));
			var platedItemModel = Minecraft.getInstance().getModelManager().getModel(ResourceHelper.inventoryModel(platedItem.getItem().getRegistryName()));
			var vertexConsumer1 = ItemRenderer.getFoilBufferDirect(pBuffer, ItemBlockRenderTypes.getRenderType(platedItem, true), true, false);
			
			pPoseStack.pushPose();
			if (!platedItemModel.isGui3d()) {
				pPoseStack.scale(0.5F, 0.5F, 0.5F);
				pPoseStack.mulPose(new Quaternion(90, 0, 180, true));
				pPoseStack.translate(-1.5F, -1.5F, -0.65F);
			} else {
				pPoseStack.scale(0.25F, 0.25F, 0.25F);
				pPoseStack.translate(1.5F, 0.25F, 1.5F);
			}
			if (!platedItemModel.isCustomRenderer())
				Minecraft.getInstance().getItemRenderer().renderModelLists(platedItemModel, platedItem, pPackedLight, pPackedOverlay, pPoseStack, vertexConsumer1);
			else
				RenderProperties.get(platedItem).getItemStackRenderer().renderByItem(platedItem, TransformType.FIXED, pPoseStack, pBuffer, pPackedLight, pPackedOverlay);
			pPoseStack.popPose();
		}
		pPoseStack.popPose();
	}
}