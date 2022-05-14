package dhyces.dinnerplate.item;

import java.util.function.BiFunction;
import java.util.function.Consumer;

import javax.annotation.Nullable;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.IItemRenderProperties;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

public class CapabilityNBTBlockItem extends NBTBlockItem {

	protected final BiFunction<ItemStack, CompoundTag, ICapabilityProvider> cap;

	public CapabilityNBTBlockItem(Block pBlock, Properties pProperties) {
		this((i, c) -> null, pBlock, pProperties);
	}

	public CapabilityNBTBlockItem(BiFunction<ItemStack, CompoundTag, ICapabilityProvider> capability, Block pBlock, Properties pProperties) {
		super(pBlock, pProperties);
		this.cap = capability;
	}

	@Nullable
	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, CompoundTag nbt) {
		return cap.apply(stack, nbt);
	}
}
