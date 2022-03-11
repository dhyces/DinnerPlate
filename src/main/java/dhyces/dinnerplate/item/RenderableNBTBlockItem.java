package dhyces.dinnerplate.item;

import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Supplier;

import javax.annotation.Nullable;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.IItemRenderProperties;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

public class RenderableNBTBlockItem extends NBTBlockItem {

	private final BlockEntityWithoutLevelRenderer renderer;
	protected final BiFunction<ItemStack, CompoundTag, ICapabilityProvider> cap;
	
	public RenderableNBTBlockItem(BlockEntityWithoutLevelRenderer renderer, Block pBlock, Properties pProperties) {
		this((i, c) -> null, renderer, pBlock, pProperties);
	}
	
	public RenderableNBTBlockItem(BiFunction<ItemStack, CompoundTag, ICapabilityProvider> capability, BlockEntityWithoutLevelRenderer renderer, Block pBlock, Properties pProperties) {
		super(pBlock, pProperties);
		this.renderer = renderer;
		this.cap = capability;
	}
	
	@Nullable
	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, CompoundTag nbt) {
		return cap.apply(stack, nbt);
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
