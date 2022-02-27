package dhyces.dinnerplate.capability;

import dhyces.dinnerplate.bite.IBitable;
import dhyces.dinnerplate.bite.IBite;
import net.minecraft.world.item.ItemStack;

public interface IMockFoodProvider extends IBitable {

	public ItemStack getRealStack();

	public void initialize(ItemStack stack, int chewCount);

	public int getBiteCount();

	public int getMaxBites();

	public boolean incrementBiteCount();

	public void setBiteCount(int count);

	public IBite getBite(int chew);

	public boolean isFast();

	public boolean isMeat();

	public boolean canAlwaysEat();

	@Override
	default int getBiteCount(ItemStack stack) {
		return getBiteCount();
	}

	@Override
	default int getMaxBites(ItemStack stack) {
		return getMaxBites();
	}

	@Override
	default boolean incrementBiteCount(ItemStack stack) {
		return incrementBiteCount();
	}

	@Override
	default void setBiteCount(ItemStack stack, int count) {
		setBiteCount(count);
	}

	@Override
	default IBite getBite(ItemStack stack, int chew) {
		return getBite(chew);
	}

	@Override
	default boolean isFast(ItemStack stack) {
		return isFast();
	}

	@Override
	default boolean isMeat(ItemStack stack) {
		return isMeat();
	}

	@Override
	default boolean canAlwaysEat(ItemStack stack) {
		return canAlwaysEat();
	}
}
