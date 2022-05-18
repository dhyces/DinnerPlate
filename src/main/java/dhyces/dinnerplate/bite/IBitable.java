package dhyces.dinnerplate.bite;

import com.mojang.datafixers.util.Pair;
import dhyces.dinnerplate.util.FoodHelper;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;

public interface IBitable<T> extends IEdible<T> {

	public ParticleOptions getParticle(ItemStack stack);

	public int getBiteCount(T bitable);

	int getMaxBites(T bitable, LivingEntity entity);

	default int getMaxBiteCount(T bitable, LivingEntity entity) {
		return isFast(bitable, entity) ? 1 : getMaxBites(bitable, entity);
	}

	public boolean incrementBiteCount(T bitable, LivingEntity entity);

	public void setBiteCount(T bitable, LivingEntity entity, int count);

	public IBite getBite(T bitable, LivingEntity entity, int chew);

	boolean canBeFast(T bitable, LivingEntity entity);

	@Override
	public default boolean isFast(T bitable, LivingEntity entity) {
		return getMaxBites(bitable, entity) == 1 && canBeFast(bitable, entity);
	}

	public SoundEvent getEatingSound(T bitable);

	/** Override this to do an operation on the last bite and return an item that results from finishing the food.*/
	public T finish(T bitable, Level level, LivingEntity livingEntity);

	public default T eat(T bitable, Player player, Level level) {
		var returnStack = bitable;
		// TODO: ~~this breaks MockFood~~ Haven't noticed anything breaking, I must have fixed whatever was breaking a while ago. Left note here though, just in case.
		var bite = getBite(bitable, player, getBiteCount(bitable));
		player.getFoodData().eat(bite.getNutrition(), bite.getSaturationModifier());
		level.gameEvent(player, GameEvent.EAT, player.eyeBlockPosition());
		level.playSound((Player)null, player.getX(), player.getY(), player.getZ(), getEatingSound(bitable), SoundSource.NEUTRAL, 1.0F, 1.0F + (level.random.nextFloat() - level.random.nextFloat()) * 0.4F);
		for(Pair<MobEffectInstance, Float> pair : bite.getEffects()) {
			if (!level.isClientSide && pair.getFirst() != null && level.random.nextFloat() < pair.getSecond()) {
				FoodHelper.addEffect(player, new MobEffectInstance(pair.getFirst()));
			}
		}

		if (incrementBiteCount(bitable, player)) {
			returnStack = finish(bitable, level, player);
			level.playSound((Player)null, player.getX(), player.getY(), player.getZ(), SoundEvents.PLAYER_BURP, SoundSource.PLAYERS, 0.5F, level.random.nextFloat() * 0.1F + 0.9F);
		}
		setBiteCount(bitable, player, getBiteCount(bitable) % getMaxBiteCount(bitable, player));
		return returnStack;
	}
}
