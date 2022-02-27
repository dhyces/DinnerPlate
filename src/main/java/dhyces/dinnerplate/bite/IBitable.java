package dhyces.dinnerplate.bite;

import java.util.Optional;

import com.mojang.datafixers.util.Pair;

import dhyces.dinnerplate.util.FoodHelper;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;

public interface IBitable {
	
	public int getBiteCount(ItemStack stack);
	
	abstract int getMaxBites(ItemStack stack);

	default int getMaxBiteCount(ItemStack stack) {
		return isFast(stack) ? 1 : getMaxBites(stack);
	}

	public boolean incrementBiteCount(ItemStack stack);

	public void setBiteCount(ItemStack stack, int count);

	public IBite getBite(ItemStack stack, int chew);

	public boolean isFast(ItemStack stack);

	public boolean isMeat(ItemStack stack);

	public boolean canAlwaysEat(ItemStack stack);
	
	public SoundEvent getEatingSound(ItemStack stack);
	
	/** Override this if your food is contained by something else, ie a bowl, a bottle, etc*/
	public ItemStack getReturnedItem(ItemStack stack);
	
	public default ItemStack eat(ItemStack stack, Player player, Level level) {
		var returnStack = stack;
		incrementBiteCount(stack);
		var bite = getBite(stack, getBiteCount(stack));
		player.getFoodData().eat(bite.getNutrition(), bite.getSaturationModifier());
		level.gameEvent(player, GameEvent.EAT, player.eyeBlockPosition());
		level.playSound((Player)null, player.getX(), player.getY(), player.getZ(), getEatingSound(stack), SoundSource.NEUTRAL, 1.0F, 1.0F + (level.random.nextFloat() - level.random.nextFloat()) * 0.4F);
		for(Pair<MobEffectInstance, Float> pair : bite.getEffects()) {
			if (!level.isClientSide && pair.getFirst() != null && level.random.nextFloat() < pair.getSecond()) {
				FoodHelper.addEffect(player, new MobEffectInstance(pair.getFirst()));
			}
		}
		if (getBiteCount(stack) >= getMaxBiteCount(stack)) {
			returnStack = stack.finishUsingItem(level, player);
			player.awardStat(Stats.ITEM_USED.get(stack.getItem()));
			level.playSound((Player)null, player.getX(), player.getY(), player.getZ(), SoundEvents.PLAYER_BURP, SoundSource.PLAYERS, 0.5F, level.random.nextFloat() * 0.1F + 0.9F);
			if (player instanceof ServerPlayer serverPlayer) {
				CriteriaTriggers.CONSUME_ITEM.trigger(serverPlayer, stack);
			}
		}
		return returnStack;
	}

	public static Optional<IBitable> bitable(ItemStack stack) {
		return stack.getItem() instanceof IBitable bitable ? Optional.of(bitable) : Optional.empty();
	}

}
