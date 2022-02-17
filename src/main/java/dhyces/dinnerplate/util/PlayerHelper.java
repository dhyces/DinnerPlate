package dhyces.dinnerplate.util;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.gameevent.GameEvent;

public class PlayerHelper {

	public static void staticEquipEventAndSound(Player player, ItemStack stack) {
		SoundEvent soundevent = stack.getEquipSound();
		if (soundevent == null)
			soundevent = SoundEvents.ITEM_PICKUP;
		if (!stack.isEmpty() && !player.isSpectator()) {
			player.gameEvent(GameEvent.EQUIP);
			player.playSound(soundevent, 1.0F, 1.0F);
		}
	}
}
