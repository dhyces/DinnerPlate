package dhyces.dinnerplate.client.render.block;

import java.util.Random;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Quaternion;

import dhyces.dinnerplate.blockentity.MixingBowlBlockEntity;
import dhyces.dinnerplate.client.render.IMixingBowlRenderer;
import dhyces.dinnerplate.client.render.util.IFluidRenderer;
import dhyces.dinnerplate.client.render.util.RenderTypes;
import dhyces.dinnerplate.dinnerunit.FlutemStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemTransforms.TransformType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider.Context;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.RenderProperties;
import net.minecraftforge.fluids.FluidStack;

// TODO: Revise this renderer, especially the fluids
@OnlyIn(Dist.CLIENT)
public class MixingBowlBlockRenderer extends SimpleBlockRenderer<MixingBowlBlockEntity> implements IMixingBowlRenderer, IFluidRenderer {

	public MixingBowlBlockRenderer(Context context) {
		super(context);
	}

	@Override
	public void render(MixingBowlBlockEntity bEntity, float pPartialTick, PoseStack poseStack,
			MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay) {
		if (!bEntity.hasLevel() || bEntity.getBlockPos() == null)
			return;
		var fluidHeight = ((bEntity.updateRenderable("", 0.05f)/100) * 0.4375f);
		if (bEntity.hasItem()) {
			renderItems(poseStack, pBufferSource, fluidHeight, pPackedLight, pPackedOverlay, bEntity.getItems().toArray(ItemStack[]::new));
		}
		if (bEntity.hasFluid())
			renderFluids(bEntity.getBlockPos(), pBufferSource, poseStack, fluidHeight, pPartialTick, pPackedLight, bEntity.mixedStream().filter(FlutemStack::isFluid).map(c -> c.getFluidStack()).toArray(FluidStack[]::new));
	}
}
