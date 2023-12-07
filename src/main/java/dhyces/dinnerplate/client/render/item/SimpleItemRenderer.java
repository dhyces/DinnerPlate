package dhyces.dinnerplate.client.render.item;

import com.mojang.blaze3d.vertex.PoseStack;
import dhyces.dinnerplate.client.render.util.IRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public abstract class SimpleItemRenderer extends BlockEntityWithoutLevelRenderer implements IRenderer {

    public SimpleItemRenderer() {
        super(Minecraft.getInstance() == null ? null : Minecraft.getInstance().getBlockEntityRenderDispatcher(),
                Minecraft.getInstance() == null ? null : Minecraft.getInstance().getEntityModels());
    }

    @Override
    public void renderByItem(ItemStack pStack, ItemDisplayContext displayContext, PoseStack pPoseStack,
                             MultiBufferSource pBuffer, int pPackedLight, int pPackedOverlay) {
        render(pStack, displayContext, pPoseStack, pBuffer, pPackedLight, pPackedOverlay);
    }

    public abstract void render(ItemStack stack, ItemDisplayContext displayContext, PoseStack poseStack,
                                MultiBufferSource bufferSource, int packedLight, int packedOverlay);
}
