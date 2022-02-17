package dhyces.dinnerplate.item;

import java.util.function.Consumer;

import dhyces.dinnerplate.registry.BlockRegistry;
import dhyces.dinnerplate.registry.SoundRegistry;
import dhyces.dinnerplate.render.item.PlateItemRenderer;
import dhyces.dinnerplate.sound.DinnerSoundTypes;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.BlockItem;
import net.minecraftforge.client.IItemRenderProperties;

public class PlateItem extends BlockItem {

	private final PlateItemRenderer renderer = new PlateItemRenderer();
	
	public PlateItem(Properties pProperties) {
		super(BlockRegistry.PLATE_BLOCK.get(), pProperties);
	}
	
	@Override
	public SoundEvent getEquipSound() {
		return DinnerSoundTypes.PLATE_SOUND_TYPE.getPlaceSound();
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
