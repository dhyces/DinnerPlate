package dhyces.dinnerplate.item;

import java.util.function.Consumer;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.IItemRenderProperties;

public class RenderableNBTBlockItem extends NBTBlockItem {

	private final BlockEntityWithoutLevelRenderer renderer;
	
	public RenderableNBTBlockItem(BlockEntityWithoutLevelRenderer renderer, Block pBlock, Properties pProperties) {
		super(pBlock, pProperties);
		this.renderer = renderer;
	}

	@Override
	public void initializeClient(Consumer<IItemRenderProperties> consumer) {
		consumer.accept(new IItemRenderProperties() {
			@Override
			public BlockEntityWithoutLevelRenderer getItemStackRenderer() {
				return renderer;
			}
		});
	}
}
