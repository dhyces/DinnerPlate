package dhyces.dinnerplate.client.render;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import dhyces.dinnerplate.blockentity.MeasuringCupBlockEntity;
import dhyces.dinnerplate.client.render.util.IFluidRenderer;
import dhyces.dinnerplate.client.render.util.RectPrism;
import dhyces.dinnerplate.client.render.util.RenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

public class MeasuringCupRenderer extends SimpleBlockItemRenderer<MeasuringCupBlockEntity> implements IFluidRenderer {

    public MeasuringCupRenderer() {
        super();
    }

    public MeasuringCupRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void renderItem(ItemStack pStack, ItemTransforms.TransformType pTransformType, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, int pPackedOverlay) {
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);

        var model = getResolvedItemModel(pStack);
        var buffer = pBuffer.getBuffer(RenderType.entityTranslucentCull(atlas()));

        pPoseStack.pushPose();
        for (BakedQuad q : model.getQuads(null, null, RandomSource.create(42L), ModelData.EMPTY, null)) {
            buffer.putBulkData(pPoseStack.last(), q, 1f, 1f, 1f, pPackedLight, pPackedOverlay);
        }
        pPoseStack.popPose();

        var cap = pStack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY);
        if (!cap.isPresent())
            return;

        var fluid = fluidArray(cap.resolve().get().getFluidInTank(0));
        if (fluid[0].isEmpty())
            return;
        var fluidHeight = ((fluid[0].getAmount() / 100) * 0.4375f);
        var prism = RectPrism.fromPixel(6, 1, 6).toPixel(10, 1.1 + fluidHeight, 10);
        pPoseStack.pushPose();
        tessalateItemFluids(fluid, prism, pBuffer.getBuffer(RenderTypes.fluid()), pPoseStack, pPackedOverlay, pPackedLight, Direction.values());
        pPoseStack.popPose();
    }

    @Override
    public void render(MeasuringCupBlockEntity pBlockEntity, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay) {
        if (pBlockEntity.hasFluid())
            tessalateFluids(fluidArray(pBlockEntity.getLastFluid()), pBlockEntity.getBlockPos(), new Vec3(6, 1, 6),
                    new Vec3(10, 1.1 + ((pBlockEntity.updateRenderable("", pPartialTick / 10) / 100) * 0.4375f), 10), pBufferSource.getBuffer(RenderType.translucent()), pPoseStack, pPartialTick, pPackedLight,
                    Direction.values());
    }
}
