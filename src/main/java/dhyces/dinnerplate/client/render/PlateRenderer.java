package dhyces.dinnerplate.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dhyces.dinnerplate.Constants;
import dhyces.dinnerplate.blockentity.PlateBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class PlateRenderer extends SimpleBlockItemRenderer<PlateBlockEntity> {

    public PlateRenderer() {
        super();
    }

    public PlateRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void renderItem(ItemStack pStack, ItemDisplayContext displayContext, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, int pPackedOverlay) {
        var model = Minecraft.getInstance().getItemRenderer().getModel(pStack, null, null, 0);

        var vertexConsumer = ItemRenderer.getFoilBufferDirect(pBuffer, ItemBlockRenderTypes.getRenderType(pStack, true), true, pStack.hasFoil());
        pPoseStack.pushPose();
        Minecraft.getInstance().getItemRenderer().renderModelLists(model, pStack, pPackedLight, pPackedOverlay, pPoseStack, vertexConsumer);
        pPoseStack.popPose();
        var itemTag = pStack.getTagElement(Constants.SINGLE_ITEM_TAG);
        if (itemTag != null) {
            var platedItem = ItemStack.of(itemTag);
            var platedItemModel = getResolvedItemModel(platedItem);

            pPoseStack.pushPose();
            if (!platedItemModel.usesBlockLight()) {
                pPoseStack.scale(0.5F, 0.5F, 0.5F);
                pPoseStack.translate(1.0F, 0.15F, 1.0F);
                pPoseStack.mulPose(Axis.XN.rotationDegrees(90));
            } else {
                pPoseStack.scale(0.25F, 0.25F, 0.25F);
                pPoseStack.mulPose(Axis.YP.rotationDegrees(180));
                pPoseStack.translate(-2.0F, 0.75F, -2.0F);
            }

            Minecraft.getInstance().getItemRenderer().render(platedItem, ItemDisplayContext.NONE, false, pPoseStack, pBuffer, pPackedLight, pPackedOverlay, platedItemModel);
            pPoseStack.popPose();
        }
    }

    @Override
    public void render(PlateBlockEntity pBlockEntity, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay) {
        if (!pBlockEntity.hasItem() || !shouldRenderItems(pBlockEntity, clientPlayer().getEyePosition()))
            return;
        var item = pBlockEntity.getLastItem();
        var model = getResolvedItemModel(item);
        pPoseStack.pushPose();
        pPoseStack.scale(.5F, .5F, .5F);
        var facingDir = pBlockEntity.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING);
        var flag = facingDir == Direction.NORTH || facingDir == Direction.EAST;
        var flag2 = facingDir == Direction.NORTH || facingDir == Direction.WEST;
        if (model.usesBlockLight()) {
            pPoseStack.scale(.5F, .5F, .5F);
            pPoseStack.mulPose(Axis.YP.rotationDegrees(facingDir.toYRot()));
            if (facingDir.getAxis() == Direction.Axis.X) {
                pPoseStack.mulPose(Axis.YP.rotationDegrees(180));
            }
            pPoseStack.translate(flag ? -2 : 2, 0.75, flag2 ? -2 : 2);
        } else {
            pPoseStack.mulPose(Axis.XN.rotationDegrees(90));
            pPoseStack.mulPose(Axis.ZP.rotationDegrees(facingDir.toYRot()));
            if (facingDir.getAxis() == Direction.Axis.Z) {
                pPoseStack.mulPose(Axis.ZP.rotationDegrees(180));
            }
            pPoseStack.translate(flag ? 1 : -1, flag2 ? -1 : 1, 0.1563);
        }
        // TODO: possibly use the funnyLightLevel for ambient occlusion, so we avoid buried plates exposed to the sky looking odd
        //  if funnyLightLevel > pPackedLight then pPackedLight else funnyLightLevel
        renderItemStack(item, pPoseStack, pBufferSource, pPackedLight, pPackedOverlay, model);
        pPoseStack.popPose();
    }

    public int funnyLightLevel(BlockPos pos) {
        var lightLevel = 15;
        var level = clientLevel();
        if (!level.getBlockState(pos.north()).isAir())
            lightLevel -= 2;
        if (!level.getBlockState(pos.west()).isAir())
            lightLevel -= 2;
        if (!level.getBlockState(pos.east()).isAir())
            lightLevel -= 2;
        if (!level.getBlockState(pos.south()).isAir())
            lightLevel -= 2;
        return lightLevel << 20;
    }
}
