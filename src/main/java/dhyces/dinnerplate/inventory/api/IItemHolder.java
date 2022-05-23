package dhyces.dinnerplate.inventory.api;

import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public interface IItemHolder extends Container, IInventory {

    /**
     * General query as to whether the object contains any items whatsoever
     */
    boolean hasItem();

    /**
     * Returns whether or not the object contains the specific itemstack
     */
    boolean containsItemStack(ItemStack stack);

    /**
     * Returns whether or not the object contains any of the given item
     */
    boolean containsItem(Item item);

    /**
     * Should return the last item inserted
     */
    ItemStack getLastItem();

    /**
     * Should return the last stack and remove it from the object
     */
    ItemStack removeLastItem();

    /**
     * Inserts as much of the stack as possible and returns what's left
     */
    ItemStack insertItem(ItemStack stack);

    /**
     * Inserts as much of the stack as possible at the given slot and returns what's left
     */
    ItemStack insertItemAt(ItemStack stack, int slot);

    /**
     * Returns the number of holders, similar to the size of a list
     */
    int getItemSize();
}
