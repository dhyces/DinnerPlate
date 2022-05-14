package dhyces.dinnerplate.item;

import dhyces.dinnerplate.client.render.item.PlateItemRenderer;
import dhyces.dinnerplate.sound.DinnerSoundTypes;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.IItemRenderProperties;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import java.util.function.Consumer;

public class PlateItem extends CapabilityNBTBlockItem {

	public PlateItem(Block block, Properties pProperties) {
		super((stack, tag) -> {
			return new ICapabilityProvider() {
				@Override
				public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
					return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.orEmpty(cap, LazyOptional.of(() -> new ItemStackHandler(1)));
				}};
		}, block, pProperties);
	}

	@Override
	public void initializeClient(Consumer<IItemRenderProperties> consumer) {
		consumer.accept(new IItemRenderProperties() {
			@Override
			public BlockEntityWithoutLevelRenderer getItemStackRenderer() {
				return new PlateItemRenderer();
			}
		});
	}

	@Override
	public SoundEvent getEquipSound() {
		return DinnerSoundTypes.PLATE_SOUND_TYPE.getPlaceSound();
	}
}
