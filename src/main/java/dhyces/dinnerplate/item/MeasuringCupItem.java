package dhyces.dinnerplate.item;

import dhyces.dinnerplate.block.api.IForkedInteractAdapter;
import dhyces.dinnerplate.capability.fluid.MeasuredFluidCapability;
import dhyces.dinnerplate.client.render.item.MeasuringCupItemRenderer;
import dhyces.dinnerplate.registry.BlockRegistry;
import dhyces.dinnerplate.util.FluidHelper;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.client.IItemRenderProperties;

import java.util.function.Consumer;

public class MeasuringCupItem extends CapabilityNBTBlockItem {

	public MeasuringCupItem(Block block, Properties pProperties) {
		super((stack, tag) -> new MeasuredFluidCapability(stack, 1000, FluidHelper.PILE), block, pProperties);
	}

	@Override
	public void initializeClient(Consumer<IItemRenderProperties> consumer) {
		consumer.accept(new IItemRenderProperties() {
			@Override
			public BlockEntityWithoutLevelRenderer getItemStackRenderer() {
				return new MeasuringCupItemRenderer();
			}
		});
	}
}