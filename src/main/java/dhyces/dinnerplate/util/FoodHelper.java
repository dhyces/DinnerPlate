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

	public static void playerStaticEatBite(Level level, Player player, ItemStack stack, IBite bite) {
		player.getFoodData().eat(bite.getNutrition(), bite.getSaturationModifier());
		level.gameEvent(player, GameEvent.EAT, player.eyeBlockPosition());
		level.playSound((Player)null, player.getX(), player.getY(), player.getZ(), player.getEatingSound(stack), SoundSource.NEUTRAL, 1.0F, 1.0F + (level.random.nextFloat() - level.random.nextFloat()) * 0.4F);
		for(Pair<MobEffectInstance, Float> pair : bite.getEffects()) {
			if (!level.isClientSide && pair.getFirst() != null && level.random.nextFloat() < pair.getSecond()) {
				addEffect(player, new MobEffectInstance(pair.getFirst()));
			}
		}
	}

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

	/** This doesn't actually do anything with replenishing hunger, saturation, or effects, it just plays the sound, awards a stat for the item, and triggers criteria*/
	public static void playerStaticEat(Level level, Player player, ItemStack stack) {
		player.awardStat(Stats.ITEM_USED.get(stack.getItem()));
		level.playSound((Player)null, player.getX(), player.getY(), player.getZ(), SoundEvents.PLAYER_BURP, SoundSource.PLAYERS, 0.5F, level.random.nextFloat() * 0.1F + 0.9F);
		if (player instanceof ServerPlayer serverPlayer) {
			CriteriaTriggers.CONSUME_ITEM.trigger(serverPlayer, stack);
		}
	}
}
