package dhyces.dinnerplate.render.item;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;

public class SimpleItemRenderer extends BlockEntityWithoutLevelRenderer {

	public SimpleItemRenderer() {
		super(Minecraft.getInstance() == null ? null : Minecraft.getInstance().getBlockEntityRenderDispatcher(),
				Minecraft.getInstance() == null ?  null : Minecraft.getInstance().getEntityModels());
	}

}
