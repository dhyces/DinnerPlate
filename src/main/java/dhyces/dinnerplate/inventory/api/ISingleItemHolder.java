package dhyces.dinnerplate.inventory.api;

import dhyces.dinnerplate.util.ItemHelper;
import net.minecraft.world.item.ItemStack;

public interface ISingleItemHolder extends IItemHolder {

	public void setItem(ItemStack stack);

	@Override
	default ItemStack insertItem(ItemStack stack) {
		if (containsItemStack(stack)) {
			return ItemHelper.mergeStacks(getLastItem(), stack);
		}
		if (!hasItem()) {
			setItem(stack);
			return ItemStack.EMPTY;
		}
		return stack;
	}
	
	@Override
	default ItemStack insertItemAt(ItemStack stack, int slot) {
		return insertItem(stack);
	}
}
