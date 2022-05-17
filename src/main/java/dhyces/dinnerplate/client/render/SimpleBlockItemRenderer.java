package dhyces.dinnerplate.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public abstract class SimpleBlockItemRenderer<T extends BlockEntity> extends BlockEntityWithoutLevelRenderer implements BlockEntityRenderer<T> {

    public SimpleBlockItemRenderer() {
        this(Minecraft.getInstance() != null ? Minecraft.getInstance().getBlockEntityRenderDispatcher() : null,
                Minecraft.getInstance() != null ? Minecraft.getInstance().getEntityModels() : null);
    }

    public SimpleBlockItemRenderer(BlockEntityRendererProvider.Context context) {
        this(context.getBlockEntityRenderDispatcher(), context.getModelSet());
    }

    public SimpleBlockItemRenderer(BlockEntityRenderDispatcher blockEntityRenderDispatcher, EntityModelSet entityModelSet) {
        super(blockEntityRenderDispatcher, entityModelSet);
    }

    @Override
    public void renderByItem(ItemStack pStack, ItemTransforms.TransformType pTransformType, PoseStack pPoseStack,
                             MultiBufferSource pBuffer, int pPackedLight, int pPackedOverlay) {
        render(pStack, pTransformType, pPoseStack, pBuffer, pPackedLight, pPackedOverlay);
    }

    public abstract void render(ItemStack pStack, ItemTransforms.TransformType pTransformType, PoseStack pPoseStack,
                                MultiBufferSource pBuffer, int pPackedLight, int pPackedOverlay);
}
