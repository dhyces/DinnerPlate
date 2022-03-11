package dhyces.dinnerplate.item;

import dhyces.dinnerplate.Constants;
import dhyces.dinnerplate.bite.BitableProperties;
import dhyces.dinnerplate.bite.IBitableItem;
import dhyces.dinnerplate.bite.IBite;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/** TODO: this would be an item that can take an array of bites from the constructor. acts like a singleton form of the mockfooditem behavior.*/
public class BitableItem extends Item implements IBitableItem {

	protected final BitableProperties biteProperties;

	public BitableItem(BitableProperties biteProperties, Properties pProperties) {
		super(pProperties.food(biteProperties.toFoodProperties()));
		this.biteProperties = biteProperties;
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
		var stack = pPlayer.getItemInHand(pUsedHand);
		if (pPlayer.canEat(canAlwaysEat(stack))) {
			//if (!pLevel.isClientSide)
				eat(stack, pPlayer, pLevel);
			if (!pPlayer.getAbilities().instabuild)
				stack.shrink(1);
			return InteractionResultHolder.consume(stack);
		}
		return InteractionResultHolder.pass(stack);
	}

	@Override
	public ItemStack finish(ItemStack stack, Level level, LivingEntity livingEntity) {
		return getContainerItem(stack);
	}

	@Override
	public ParticleOptions getParticle(ItemStack stack) {
		return new ItemParticleOption(ParticleTypes.ITEM, stack);
	}

	@Override
	public SoundEvent getEatingSound(ItemStack stack) {
		return getEatingSound();
	}

	@Override
	public void setBiteCount(ItemStack stack, int count) {
		if (count == 0)
			stack.getOrCreateTag().remove(Constants.TAG_BITE_COUNT);
		else
			stack.getOrCreateTag().putInt(Constants.TAG_BITE_COUNT, count);
	}

	@Override
	public int getBiteCount(ItemStack stack) {
		return stack.getOrCreateTag().getInt(Constants.TAG_BITE_COUNT);
	}

	@Override
	public int getMaxBites(ItemStack stack) {
		return biteProperties.getBiteSize();
	}

	@Override
	public boolean incrementBiteCount(ItemStack stack) {
		setBiteCount(stack, getBiteCount(stack) + 1);
		return getBiteCount(stack) >= getMaxBiteCount(stack);
	}

	@Override
	public IBite getBite(ItemStack stack, int chew) {
		return biteProperties.getBite(getBiteCount(stack));
	}

	@Override
		public boolean canBeFast(ItemStack stack) {
			return biteProperties.isFast();
		}

	@Override
	public boolean isMeat(ItemStack stack) {
		return biteProperties.isMeat();
	}

	@Override
	public boolean canAlwaysEat(ItemStack stack) {
		return biteProperties.canAlwaysEat();
	}
}
