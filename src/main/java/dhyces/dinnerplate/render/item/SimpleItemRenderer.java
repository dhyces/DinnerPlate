package dhyces.dinnerplate.render.item;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;

public class SimpleItemRenderer extends BlockEntityWithoutLevelRenderer {

	public SimpleItemRenderer() {
		super(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
	}

}