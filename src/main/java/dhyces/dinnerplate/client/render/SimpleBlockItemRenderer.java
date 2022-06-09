package dhyces.dinnerplate.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import dhyces.dinnerplate.client.render.util.IRenderer;
import net.minecraft.client.GraphicsStatus;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public abstract class SimpleBlockItemRenderer<T extends BlockEntity> extends BlockEntityWithoutLevelRenderer implements BlockEntityRenderer<T>, IRenderer {

    public SimpleBlockItemRenderer() {
        this(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
    }

    public SimpleBlockItemRenderer(BlockEntityRendererProvider.Context context) {
        this(context.getBlockEntityRenderDispatcher(), context.getModelSet());
    }

    public SimpleBlockItemRenderer(BlockEntityRenderDispatcher blockEntityRenderDispatcher, EntityModelSet entityModelSet) {
        super(blockEntityRenderDispatcher, entityModelSet);
    }

    @Override
    public void renderByItem(@NotNull ItemStack pStack, @NotNull ItemTransforms.TransformType pTransformType, @NotNull PoseStack pPoseStack,
                             @NotNull MultiBufferSource pBuffer, int pPackedLight, int pPackedOverlay) {
        render(pStack, pTransformType, pPoseStack, pBuffer, pPackedLight, pPackedOverlay);
    }

    public abstract void render(ItemStack pStack, ItemTransforms.TransformType pTransformType, PoseStack pPoseStack,
                                MultiBufferSource pBuffer, int pPackedLight, int pPackedOverlay);

    public void renderItem(ItemStack pStack, PoseStack pMatrixStack, MultiBufferSource pBuffer, int pCombinedLight, int pCombinedOverlay, BakedModel pModel) {
        Minecraft.getInstance().getItemRenderer().render(pStack, ItemTransforms.TransformType.NONE, false, pMatrixStack, pBuffer, pCombinedLight, pCombinedOverlay, pModel);
    }

    public boolean shouldRenderItems(T pBlockEntity, Vec3 pPlayerEyePos) {
        return closerThan(pBlockEntity, pPlayerEyePos, getViewDistance());
    }

    public boolean shouldRenderFluids(T pBlockEntity, Vec3 pCameraPos) {
        return closerThan(pBlockEntity, pCameraPos, getViewDistance());
    }

    public boolean playerCloserThan(T pBlockEntity, int viewDistance) {
        return closerThan(pBlockEntity, clientPlayer().getEyePosition(), viewDistance);
    }

    public boolean closerThan(T pBlockEntity, Vec3 pCameraPos, int viewDistance) {
        return Vec3.atCenterOf(pBlockEntity.getBlockPos()).closerThan(pCameraPos, viewDistance);
    }

    // TODO: possibly make this a config option, fast and fancy graphics settings for the view distance of items and fluids
    @Override
    public int getViewDistance() {
        return isGraphicsMode(GraphicsStatus.FAST) ? 32 : BlockEntityRenderer.super.getViewDistance();
    }

    public boolean isGraphicsMode(GraphicsStatus graphicsStatus) {
        return Minecraft.getInstance().options.graphicsMode().get().equals(graphicsStatus);
    }
}
