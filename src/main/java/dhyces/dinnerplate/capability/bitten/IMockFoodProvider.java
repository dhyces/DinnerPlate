package dhyces.dinnerplate.capability.bitten;

import dhyces.dinnerplate.bite.IBitableItem;
import dhyces.dinnerplate.bite.IBite;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public interface IMockFoodProvider extends IBitableItem {

	public ItemStack getRealStack();

	public void initialize(ItemStack stack, LivingEntity entity, int chewCount);

	public int getBiteCount();

	public int getMaxBites();

	public boolean incrementBiteCount();

	public void setBiteCount(int count);

	public IBite getBite(int chew);

	public boolean canBeFast();

	public boolean isMeat();

	public boolean canAlwaysEat();

	@Override
	default int getBiteCount(ItemStack stack) {
		return getBiteCount();
	}

	@Override
	default int getMaxBites(ItemStack stack, LivingEntity entity) {
		return getMaxBites();
	}

	@Override
	default boolean incrementBiteCount(ItemStack stack, LivingEntity entity) {
		return incrementBiteCount();
	}

	@Override
	default void setBiteCount(ItemStack stack, LivingEntity entity, int count) {
		setBiteCount(count);
	}

	@Override
	default IBite getBite(ItemStack stack, LivingEntity entity, int chew) {
		return getBite(chew);
	}

	@Override
	default boolean canBeFast(ItemStack stack, LivingEntity entity) {
		return canBeFast();
	}

	@Override
	default boolean isMeat(ItemStack stack, LivingEntity entity) {
		return isMeat();
	}

	@Override
	default boolean canAlwaysEat(ItemStack stack, LivingEntity entity) {
		return canAlwaysEat();
	}
}
