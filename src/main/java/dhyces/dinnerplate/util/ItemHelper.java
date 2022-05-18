package dhyces.dinnerplate.util;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BowlFoodItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.PotionItem;

import java.util.Optional;

public class ItemHelper {

    // The first stack is the one that is merged into and otherStack is the stack that is returned with the amount left over;
    public static ItemStack mergeStacks(ItemStack stack, ItemStack otherStack) {
        if (!ItemStack.isSame(stack, otherStack))
            return otherStack;
        var count1 = stack.getCount();
        var count2 = otherStack.getCount();
        var finalCount = count1 + count2;
        var overFlow = MathHelper.additionOverflow(finalCount, stack.getMaxStackSize());
        stack.setCount(finalCount - overFlow);
        otherStack.setCount(overFlow);
        return otherStack;
    }

    public static ItemStack shrinkIfNotCreative(Player player, ItemStack stack) {
        if (!player.getAbilities().instabuild)
            stack.shrink(1);
        return stack;
    }

    /**
     * Hard coded helper method to get an item that should be returned when the given itemstack is used
     */
    public static ItemStack returnedItem(ItemStack stack) {
        var compItem = stack.getItem();
        var container = getContainerItem(stack);
        if (container.isPresent())
            return container.get();
        if (compItem instanceof BowlFoodItem) {
            return Items.BOWL.getDefaultInstance().copy();
        } else if (compItem instanceof PotionItem) {
            return Items.GLASS_BOTTLE.getDefaultInstance().copy();
        }
        return ItemStack.EMPTY;
    }

    private static Optional<ItemStack> getContainerItem(ItemStack stack) {
        if (stack.hasContainerItem())
            return Optional.of(stack.getContainerItem());
        return Optional.empty();
    }

}
