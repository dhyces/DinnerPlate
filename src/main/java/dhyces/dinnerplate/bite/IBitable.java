package dhyces.dinnerplate.bite;

import com.mojang.datafixers.util.Pair;

import dhyces.dinnerplate.util.FoodHelper;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;

public interface IBitable<T> extends IEdible<T> {

	public int getBiteCount(T bitable);

	abstract int getMaxBites(T bitable);

	public default int getMaxBiteCount(T bitable) {
		return isFast(bitable) ? 1 : getMaxBites(bitable);
	}

	public boolean incrementBiteCount(T bitable);

	public void setBiteCount(T bitable, int count);

	public IBite getBite(T bitable, int chew);

	boolean canBeFast(T bitable);

	@Override
	public default boolean isFast(T bitable) {
		return getMaxBites(bitable) == 1 && canBeFast(bitable);
	}

	public SoundEvent getEatingSound(T bitable);

	/** Override this if your food is contained by something else, ie a bowl, a bottle, etc*/
	public T finish(T bitable, Level level, LivingEntity livingEntity);

	public default T eat(T bitable, Player player, Level level) {
		var returnStack = bitable;
		// TODO: this breaks MockFood
		var bite = getBite(bitable, getBiteCount(bitable));
		player.getFoodData().eat(bite.getNutrition(), bite.getSaturationModifier());
		level.gameEvent(player, GameEvent.EAT, player.eyeBlockPosition());
		level.playSound((Player)null, player.getX(), player.getY(), player.getZ(), getEatingSound(bitable), SoundSource.NEUTRAL, 1.0F, 1.0F + (level.random.nextFloat() - level.random.nextFloat()) * 0.4F);
		for(Pair<MobEffectInstance, Float> pair : bite.getEffects()) {
			if (!level.isClientSide && pair.getFirst() != null && level.random.nextFloat() < pair.getSecond()) {
				FoodHelper.addEffect(player, new MobEffectInstance(pair.getFirst()));
			}
		}

		if (incrementBiteCount(bitable)) {
			returnStack = finish(bitable, level, player);
			level.playSound((Player)null, player.getX(), player.getY(), player.getZ(), SoundEvents.PLAYER_BURP, SoundSource.PLAYERS, 0.5F, level.random.nextFloat() * 0.1F + 0.9F);
		}
		setBiteCount(bitable, getBiteCount(bitable) % getMaxBiteCount(bitable));
		return returnStack;
	}
}
