package dhyces.dinnerplate.util;

import com.mojang.datafixers.util.Pair;

import dhyces.dinnerplate.bite.IBite;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;

public class FoodHelper {

	public static void addEffect(LivingEntity entity, MobEffectInstance instance) {
		var activeEffect = entity.getActiveEffectsMap().get(instance.getEffect());
		if (activeEffect != null)
			instance.update(combineEffects(instance, activeEffect));
		entity.addEffect(instance);
	}

	public static MobEffectInstance combineEffects(MobEffectInstance instance, MobEffectInstance otherInstance) {
		if (instance.getEffect().equals(otherInstance.getEffect()) && instance.getAmplifier() == otherInstance.getAmplifier()) {
			return new MobEffectInstance(instance.getEffect(), instance.getDuration() + otherInstance.getDuration(), instance.getAmplifier());
		}
		return instance;
	}
}
