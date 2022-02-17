package dhyces.dinnerplate.bite;

import java.util.Optional;

import net.minecraft.world.item.ItemStack;

public interface IBitable {

	public int getBiteCount(ItemStack stack);
	
	public int getMaxBiteCount(ItemStack stack);
	
	public boolean incrementBiteCount(ItemStack stack);
	
	public void setBiteCount(ItemStack stack, int count);
	
	public IBite getBite(ItemStack stack, int chew);
	
	public boolean isFast(ItemStack stack);
	
	public boolean isMeat(ItemStack stack);
	
	public boolean canAlwaysEat(ItemStack stack);
	
	public static Optional<IBitable> bitable(ItemStack stack) {
		return stack.getItem() instanceof IBitable bitable ? Optional.of(bitable) : Optional.empty();
	}
	
}
