package dhyces.dinnerplate.item;

import dhyces.dinnerplate.block.api.IForkedInteractAdapter;
import dhyces.dinnerplate.registry.BlockRegistry;
import dhyces.dinnerplate.client.render.item.MeasuringCupItemRenderer;
import net.minecraft.world.level.block.entity.BlockEntity;

@Deprecated(forRemoval = true) /** Marked for removal due to not having any changes in logic from the superclass*/
public class MeasuringCupItem extends RenderableNBTBlockItem implements IForkedInteractAdapter<BlockEntity> {

	public MeasuringCupItem(Properties pProperties) {
		super(new MeasuringCupItemRenderer(), BlockRegistry.MEASURING_CUP_BLOCK.get(), pProperties);
	}
}