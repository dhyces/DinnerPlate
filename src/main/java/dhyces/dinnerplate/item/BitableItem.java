package dhyces.dinnerplate.item;

import dhyces.dinnerplate.Constants;
import dhyces.dinnerplate.bite.BitableProperties;
import dhyces.dinnerplate.bite.IBitable;
import dhyces.dinnerplate.bite.IBite;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/** TODO: this would be an item that can take an array of bites from the constructor. acts like a singleton form of the mockfooditem behavior.*/
public class BitableItem extends Item implements IBitable {

	protected final BitableProperties biteProperties;

	public BitableItem(BitableProperties biteProperties, Properties pProperties) {
		super(pProperties.food(biteProperties.toFoodProperties()));
		this.biteProperties = biteProperties;
	}

	@Override
	public ItemStack finishUsingItem(ItemStack pStack, Level pLevel, LivingEntity pLivingEntity) {
		return pStack;
	}
	
	@Override
	public ItemStack getReturnedItem(ItemStack stack) {
		return ItemStack.EMPTY;
	}
	
	@Override
		public SoundEvent getEatingSound(ItemStack stack) {
			return getEatingSound();
		}
	
	@Override
	public void setBiteCount(ItemStack stack, int count) {
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
	public boolean isFast(ItemStack stack) {
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
