package dhyces.dinnerplate.item;

import dhyces.dinnerplate.block.api.IForkedInteractAdapter;
import dhyces.dinnerplate.capability.fluid.MeasuredFluidCapability;
import dhyces.dinnerplate.registry.BlockRegistry;
import dhyces.dinnerplate.render.item.MeasuringCupItemRenderer;
import dhyces.dinnerplate.util.FluidHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

public class MeasuringCupItem extends RenderableNBTBlockItem implements IForkedInteractAdapter<BlockEntity> {

	public MeasuringCupItem(Properties pProperties) {
		super(new MeasuringCupItemRenderer(), BlockRegistry.MEASURING_CUP_BLOCK.get(), pProperties);
	}
	
	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, CompoundTag nbt) {
		return new MeasuredFluidCapability(stack, 1000, FluidHelper.PILE);
	}
}