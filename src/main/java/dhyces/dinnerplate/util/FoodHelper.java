package dhyces.dinnerplate.util;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;

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
