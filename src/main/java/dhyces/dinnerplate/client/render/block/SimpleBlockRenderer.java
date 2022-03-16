package dhyces.dinnerplate.client.render.block;

import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.entity.BlockEntity;

public abstract class SimpleBlockRenderer<T extends BlockEntity> implements BlockEntityRenderer<T>{

	public SimpleBlockRenderer(BlockEntityRendererProvider.Context context) {
	}
}
