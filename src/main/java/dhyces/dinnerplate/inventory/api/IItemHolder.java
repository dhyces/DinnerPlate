package dhyces.dinnerplate.inventory.api;

import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public interface IItemHolder extends Container {

	/** General query as to whether the object contains any items whatsoever */
	public boolean hasItem();

	/** Returns whether or not the object contains the specific itemstack */
	public boolean containsItemStack(ItemStack stack);

	/** Returns whether or not the object contains any of the given item */
	public boolean containsItem(Item item);

	/** Should return the last item inserted */
	public ItemStack getLastItem();

	/** Should return the last stack and remove it from the object */
	public ItemStack removeLastItem();

	/** Inserts as much of the stack as possible and returns what's left */
	public ItemStack insertItem(ItemStack stack);
	
	/** Inserts as much of the stack as possible at the given slot and returns what's left */
	public ItemStack insertItemAt(ItemStack stack, int slot);

	/** Returns the number of holders, similar to the size of a list*/
	public int getItemSize();
}
