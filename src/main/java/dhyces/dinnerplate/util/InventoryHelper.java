package dhyces.dinnerplate.util;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

public class InventoryHelper {

    /**
     * Tries to insert the given itemstack into an empty slot in the given inventory, starting with selected hand. If it can't, returns false.
     */
    public static boolean insertItemNewSlot(Inventory inv, ItemStack stack) {
        var slot = inv.getSelected().isEmpty() ? inv.selected : inv.getFreeSlot();
        if (slot == -1)
            return false;
        inv.add(slot, stack);
        return true;
    }

    /**
     * We prefer slots with the same item, but if we can't insert into any of those or if one is not found, we prefer the selected slot if
     * it doesn't have an item, otherwise we want to insert into any free slot
     */
    public static int getPreferredSlot(Inventory inv, ItemStack stack) {
        var slot = inv.getSlotWithRemainingSpace(stack);
        if (slot == -1 && inv.getSelected().isEmpty())
            slot = inv.selected;
        return slot == -1 ? inv.getFreeSlot() : slot;
    }

    public static boolean insertItemHeld(Inventory inv, ItemStack stack) {
        if (Inventory.isHotbarSlot(inv.selected) && inv.getSelected().isEmpty()) {
            inv.setItem(inv.selected, stack);
            return true;
        }
        return false;
    }
}
