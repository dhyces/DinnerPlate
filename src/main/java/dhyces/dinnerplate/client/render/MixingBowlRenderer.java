package dhyces.dinnerplate.client.render;

import com.mojang.blaze3d.Blaze3D;
import com.mojang.blaze3d.vertex.PoseStack;
import dhyces.dinnerplate.blockentity.MixingBowlBlockEntity;
import dhyces.dinnerplate.client.render.util.IFluidRenderer;
import dhyces.dinnerplate.client.render.util.RectPrism;
import dhyces.dinnerplate.client.render.util.RenderTypes;
import dhyces.dinnerplate.dinnerunit.FlutemStack;
import dhyces.dinnerplate.util.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.items.CapabilityItemHandler;

import java.util.Random;
import java.util.function.Function;

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
    public void render(ItemStack pStack, ItemTransforms.TransformType pTransformType, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, int pPackedOverlay) {
        pPoseStack.pushPose();
        renderItem(pStack, pPoseStack, pBuffer.getBuffer(RenderType.entityTranslucentCull(atlas())), pPackedLight, pPackedOverlay);

        var fluidCap = FluidUtil.getFluidHandler(pStack);
        float fluidHeight = fluidCap.isPresent() ? fluidCap.map(c -> c.getTanks() * 0.4375f).get() : 0;
        var itemCap = pStack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY);

        if (itemCap.isPresent()) {
            var itemHandler = itemCap.resolve().get();
            renderItems(pPoseStack, pBuffer, fluidHeight, pPackedLight, pPackedOverlay, Util.streamOf(itemHandler::getStackInSlot, itemHandler.getSlots()).toArray(ItemStack[]::new));
        }
        if (fluidCap.isPresent()) {
            var fluidHandler = fluidCap.resolve().get();
            renderFluids(BlockPos.ZERO, pBuffer, pPoseStack, fluidHeight, pPackedOverlay, pPackedLight, Util.streamOf(fluidHandler::getFluidInTank, fluidHandler.getTanks()).toArray(FluidStack[]::new));
        }
        pPoseStack.popPose();
    }

    @Override
    public void render(MixingBowlBlockEntity pBlockEntity, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay) {
        if (!pBlockEntity.hasLevel() || pBlockEntity.getBlockPos() == null)
            return;
        var fluidHeight = ((pBlockEntity.updateRenderable("fluid", 0.05f)/100) * 0.4375f);
        if (pBlockEntity.hasFluid())
            renderFluids(pBlockEntity.getBlockPos(), pBufferSource, pPoseStack, fluidHeight, pPartialTick, pPackedLight, pBlockEntity.getFluids().toArray(FluidStack[]::new));
        if (pBlockEntity.hasItem()) {
            renderItems(pPoseStack, pBufferSource, fluidHeight, pPackedLight, pPackedOverlay, pBlockEntity.getItems().toArray(ItemStack[]::new));
        }
    }

    public void renderItems(PoseStack poseStack, MultiBufferSource source, float fluidHeight, int packedLight, int packedOverlay, ItemStack... stacks) {
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
            var itemModel = Minecraft.getInstance().getItemRenderer().getModel(item, clientLevel(), clientPlayer(), 0);
            poseStack.pushPose();
            // TODO: rotations
            poseStack.translate(nd, !Mth.equal(fluidHeight, 0) ? Math.sin(Blaze3D.getTime() + nd + nd1) / 16 : 0, nd1);
            poseStack.mulPose(doubleQuaternion(nd * 50, nd * 50, nd * 50, true));
            Minecraft.getInstance().getItemRenderer().render(item, ItemTransforms.TransformType.NONE, false, poseStack, source, packedLight, packedOverlay, itemModel);
            poseStack.popPose();
        }
        poseStack.popPose();
    }

    public void renderFluids(BlockPos pos, MultiBufferSource source, PoseStack poseStack, float fluidHeight, float partialTick, int packedLight, FluidStack... fluids) {
        tessalateFluids(fluids, pos, RectPrism.fromPixel(3, 2, 3).toPixel(13, 2.1 + fluidHeight, 13),
                source.getBuffer(RenderType.translucentNoCrumbling()), poseStack, partialTick, packedLight, Direction.UP); //This cannot be Sheets.translucentCullBlock. it causes faces to not render
    }
}
