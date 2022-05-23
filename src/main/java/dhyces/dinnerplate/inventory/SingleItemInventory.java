package dhyces.dinnerplate.inventory;

import dhyces.dinnerplate.Constants;
import dhyces.dinnerplate.inventory.api.ISingleItemHolder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class SingleItemInventory implements ISingleItemHolder {

    Runnable onChanged = () -> {};
    ItemStack item = ItemStack.EMPTY;

    public SingleItemInventory() {

    }

    @Override
    public void setOnChanged(Runnable methodToCall) {
        this.onChanged = methodToCall;
    }

    @Override
    public boolean hasItem() {
        return !item.isEmpty();
    }

    @Override
    public boolean containsItemStack(ItemStack stack) {
        return ItemStack.matches(item, stack);
    }

    @Override
    public boolean containsItem(Item item) {
        return this.item.is(item);
    }

    @Override
    public ItemStack getLastItem() {
        return item;
    }

    @Override
    public ItemStack removeLastItem() {
        var item = this.item.copy();
        setItem(ItemStack.EMPTY);
        return item;
    }

    @Override
    public int getItemSize() {
        return 1;
    }

    @Override
    public void setItem(ItemStack stack) {
        this.item = stack;
        setChanged();
    }

    @Override
    public int getContainerSize() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return item.isEmpty();
    }

    @Override
    public ItemStack getItem(int pIndex) {
        return item;
    }

    @Override
    public ItemStack removeItem(int pIndex, int pCount) {
        var copy = item.copy();
        var count = Math.min(copy.getCount(), pCount);
        copy.setCount(count);
        item.shrink(count);
        setChanged();
        return copy;
    }

    @Override
    public ItemStack removeItemNoUpdate(int pIndex) {
        var item = this.item.copy();
        this.item = ItemStack.EMPTY;
        return item;
    }

    @Override
    public void setItem(int pIndex, ItemStack pStack) {
        setItem(pStack);
    }

    @Override
    public void setChanged() {
        onChanged.run();
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return true;
    }

    @Override
    public void clearContent() {
        this.item = ItemStack.EMPTY;
        setChanged();
    }

    @Override
    public CompoundTag serializeNBT() {
        var tag = new CompoundTag();
        tag.put(Constants.TAG_SINGLE_ITEM, this.item.serializeNBT());
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.item = ItemStack.of(nbt.getCompound(Constants.TAG_SINGLE_ITEM));
    }
}
