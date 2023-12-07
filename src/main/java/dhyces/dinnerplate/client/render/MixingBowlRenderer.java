package dhyces.dinnerplate.client.render;

import com.mojang.blaze3d.Blaze3D;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dhyces.dinnerplate.blockentity.MixingBowlBlockEntity;
import dhyces.dinnerplate.client.render.util.IFluidRenderer;
import dhyces.dinnerplate.client.render.util.RectPrism;
import dhyces.dinnerplate.util.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.items.IItemHandler;

import java.util.Random;

@OnlyIn(Dist.CLIENT)
public class MixingBowlRenderer extends SimpleBlockItemRenderer<MixingBowlBlockEntity> implements IFluidRenderer {

    public static final MixingBowlRenderer INSTANCE = new MixingBowlRenderer();

    public MixingBowlRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    public MixingBowlRenderer() {
        super();
    }

    @Override
    public void renderItem(ItemStack pStack, ItemDisplayContext displayContext, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, int pPackedOverlay) {
        pPoseStack.pushPose();
        renderItem(pStack, pPoseStack, pBuffer.getBuffer(RenderType.entityTranslucentCull(atlas())), pPackedLight, pPackedOverlay);

        LazyOptional<IFluidHandlerItem> fluidCap = FluidUtil.getFluidHandler(pStack);
        float fluidHeight = fluidCap.isPresent() ? fluidCap.map(c -> c.getTanks() * 0.4375f).get() : 0;
        LazyOptional<IItemHandler> itemCap = pStack.getCapability(ForgeCapabilities.ITEM_HANDLER);

        if (fluidCap.isPresent()) {
            IFluidHandlerItem fluidHandler = fluidCap.resolve().get();
            renderFluids(BlockPos.ZERO, pBuffer, pPoseStack, fluidHeight, pPackedOverlay, pPackedLight, Util.streamOf(fluidHandler::getFluidInTank, fluidHandler.getTanks()).toArray(FluidStack[]::new));
        }
        if (itemCap.isPresent()) {
            IItemHandler itemHandler = itemCap.resolve().get();
            renderItems(pPoseStack, pBuffer, fluidHeight, pPackedLight, pPackedOverlay, false, Util.streamOf(itemHandler::getStackInSlot, itemHandler.getSlots()).toArray(ItemStack[]::new));
        }
        pPoseStack.popPose();
    }

    @Override
    public void render(MixingBowlBlockEntity pBlockEntity, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay) {
        if (!pBlockEntity.hasLevel() || pBlockEntity.getBlockPos() == null)
            return;
        float fluidHeight = ((pBlockEntity.updateRenderable("", pPartialTick / 10) / 100) * 0.4375f);
        if (pBlockEntity.hasFluid() && shouldRenderFluids(pBlockEntity, clientPlayer().getEyePosition()))
            renderFluids(pBlockEntity.getBlockPos(), pBufferSource, pPoseStack, fluidHeight, pPartialTick, pPackedLight, pBlockEntity.getFluids().toArray(FluidStack[]::new));
        if (pBlockEntity.hasItem() && shouldRenderItems(pBlockEntity, clientPlayer().getEyePosition())) {
            // TODO: I want to do more work on when the wave is enabled
            boolean flag = !Mth.equal(fluidHeight, 0) && playerCloserThan(pBlockEntity, 24);
            renderItems(pPoseStack, pBufferSource, fluidHeight, pPackedLight, pPackedOverlay, flag, pBlockEntity.getItems().toArray(ItemStack[]::new));
        }
    }

    public void renderItems(PoseStack poseStack, MultiBufferSource source, float fluidHeight, int packedLight, int packedOverlay, boolean shouldWave, ItemStack... stacks) {
        poseStack.pushPose();
        poseStack.translate(fromPixel(8), fromPixel(fluidHeight + 2.5F), fromPixel(8));
        poseStack.scale(.25f, .25f, .25f);
        for (int i = 0; i < stacks.length; i++) {
            Random r = new Random(i);
            r.nextDouble();
            double nd = r.nextDouble(0.8175);
            double nd1 = r.nextDouble(0.8175);
            boolean nb = r.nextBoolean();
            boolean nb1 = r.nextBoolean();
            nd *= Boolean.compare(nb, !nb);
            nd1 *= Boolean.compare(nb1, !nb1);
            var item = stacks[i];
            var itemModel = Minecraft.getInstance().getItemRenderer().getModel(item, clientLevel(), clientPlayer(), 42);
            poseStack.pushPose();
            // TODO: rotations
            poseStack.translate(nd, shouldWave ? Math.sin(Blaze3D.getTime() + nd + nd1) / 16 : 0, nd1);
            poseStack.mulPose(Axis.XP.rotationDegrees((float) nd * 50));
            poseStack.mulPose(Axis.YP.rotationDegrees((float) nd * 50));
            poseStack.mulPose(Axis.ZP.rotationDegrees((float) nd * 50));
            Minecraft.getInstance().getItemRenderer().render(item, ItemDisplayContext.NONE, false, poseStack, source, packedLight, packedOverlay, itemModel);
            poseStack.popPose();
        }
        poseStack.popPose();
    }

    public void renderFluids(BlockPos pos, MultiBufferSource source, PoseStack poseStack, float fluidHeight, float partialTick, int packedLight, FluidStack... fluids) {
        tessalateFluids(fluids, pos, RectPrism.fromPixel(3, 2, 3).toPixel(13, 2.1 + fluidHeight, 13),
                source.getBuffer(RenderType.translucentNoCrumbling()), poseStack, partialTick, packedLight, Direction.UP); //This cannot be Sheets.translucentCullBlock. it causes faces to not render
    }
}
