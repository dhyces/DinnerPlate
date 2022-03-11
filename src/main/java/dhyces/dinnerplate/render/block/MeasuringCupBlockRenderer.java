package dhyces.dinnerplate.render.block;

import com.mojang.blaze3d.vertex.PoseStack;

import dhyces.dinnerplate.blockentity.MeasuringCupBlockEntity;
import dhyces.dinnerplate.render.util.IFluidRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider.Context;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;

public class MeasuringCupBlockRenderer extends SimpleBlockRenderer<MeasuringCupBlockEntity> implements IFluidRenderer {

	public MeasuringCupBlockRenderer(Context context) {
		super(context);
	}

	@Override
	public void render(MeasuringCupBlockEntity pBlockEntity, float pPartialTick, PoseStack pPoseStack,
			MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay) {
		if (pBlockEntity.hasFluid())
			tessalateFluids(fluidArray(pBlockEntity.getLastFluid()), pBlockEntity.getBlockPos(), new Vec3(6, 1, 6),
					new Vec3(10, 1.1 + ((pBlockEntity.getFluidAmount()/100) * 0.4375f), 10), pBufferSource.getBuffer(RenderType.translucent()), pPoseStack, pPartialTick, pPackedLight,
					Direction.values());

	}

}
