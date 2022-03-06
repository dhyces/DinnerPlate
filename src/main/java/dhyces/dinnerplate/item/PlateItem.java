package dhyces.dinnerplate.item;

import dhyces.dinnerplate.registry.BlockRegistry;
import dhyces.dinnerplate.render.item.PlateItemRenderer;
import dhyces.dinnerplate.sound.DinnerSoundTypes;
import net.minecraft.sounds.SoundEvent;

public class PlateItem extends RenderableNBTBlockItem {


	public PlateItem(Properties pProperties) {
		super(new PlateItemRenderer(), BlockRegistry.PLATE_BLOCK.get(), pProperties);
	}

	@Override
	public SoundEvent getEquipSound() {
		return DinnerSoundTypes.PLATE_SOUND_TYPE.getPlaceSound();
	}
}
