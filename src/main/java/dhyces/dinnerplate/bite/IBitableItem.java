package dhyces.dinnerplate.bite;

import java.util.Optional;

import com.mojang.datafixers.util.Pair;

import dhyces.dinnerplate.util.FoodHelper;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;

public interface IBitableItem extends IBitable<ItemStack> {

	public ParticleOptions getParticle(ItemStack stack);

	@Override
	public default ItemStack eat(ItemStack stack, Player player, Level level) {
		var returnStack = stack;
		var bite = getBite(stack, getBiteCount(stack));
		player.getFoodData().eat(bite.getNutrition(), bite.getSaturationModifier());
		level.gameEvent(player, GameEvent.EAT, player.eyeBlockPosition());
		level.playSound((Player)null, player.getX(), player.getY(), player.getZ(), getEatingSound(stack), SoundSource.NEUTRAL, 1.0F, 1.0F + (level.random.nextFloat() - level.random.nextFloat()) * 0.4F);
		var pos = getPlayerLocalPos(player, new Vec3(0, player.getEyeY() - player.getY(), 0.05));
		spawnParticles(level, pos, stack);
		for(Pair<MobEffectInstance, Float> pair : bite.getEffects()) {
			if (!level.isClientSide && pair.getFirst() != null && level.random.nextFloat() < pair.getSecond()) {
				FoodHelper.addEffect(player, new MobEffectInstance(pair.getFirst()));
			}
		}

		if (incrementBiteCount(stack)) {
			returnStack = finish(stack, level, player);
			player.awardStat(Stats.ITEM_USED.get(stack.getItem()));
			level.playSound((Player)null, player.getX(), player.getY(), player.getZ(), SoundEvents.PLAYER_BURP, SoundSource.PLAYERS, 0.5F, level.random.nextFloat() * 0.1F + 0.9F);
			if (player instanceof ServerPlayer serverPlayer) {
				CriteriaTriggers.CONSUME_ITEM.trigger(serverPlayer, stack);
			}
		}
		setBiteCount(stack, getBiteCount(stack) % getMaxBiteCount(stack));
		return returnStack;
	}

	public default Vec3 getPlayerLocalPos(Player player, Vec3 offset) {
		var playerPos = offset;
		playerPos = playerPos.xRot(-player.getXRot() * (float)(Math.PI / 180));
		playerPos = playerPos.yRot(-player.getYRot() * (float)(Math.PI / 180));
		return playerPos.add(player.position().x, player.position().y, player.position().z);
	}

	public default void spawnParticles(Level level, Vec3 pos, ItemStack stack) {
		if (level instanceof ServerLevel sLevel)
            sLevel.sendParticles(getParticle(stack), pos.x, pos.y, pos.z, 1, 0, 0.05D, 0, 0.0D);
        else
        	level.addParticle(getParticle(stack), pos.x, pos.y, pos.z, 0, 0.05D, 0);
	}

	public static Optional<IBitableItem> bitable(ItemStack stack) {
		return stack.getItem() instanceof IBitableItem bitable ? Optional.of(bitable) : Optional.empty();
	}
}
