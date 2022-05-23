package dhyces.dinnerplate.inventory;

import com.google.common.collect.ContiguousSet;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Range;
import dhyces.dinnerplate.Constants;
import dhyces.dinnerplate.util.*;
import dhyces.dinnerplate.inventory.api.IMixedInventory;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.items.ItemHandlerHelper;

import java.util.List;
import java.util.Optional;
import java.util.Stack;
import java.util.concurrent.Callable;
import java.util.function.Supplier;
import java.util.stream.Stream;

// TODO: name could be changed since it's somewhat ambiguous. "what is mixed?", well in this case its both item and fluid stacks
public class MixedInventory implements IMixedInventory {

    private final int size;
    private int usedSize = 0;
    private final Stack<ItemStack> itemInventory;
    private final Stack<FluidStack> fluidInventory;

    Runnable onChanged = () -> {};

    public MixedInventory(int sizeIn) {
        size = sizeIn;
        itemInventory = makeStackWithCapacity(sizeIn);
        fluidInventory = makeStackWithCapacity(sizeIn);
    }

    private <T> Stack<T> makeStackWithCapacity(int cap) {
        var stack = new Stack<T>();
        stack.ensureCapacity(cap);
        return stack;
    }

    @Override
    public Stream<FlutemStack> mixedStream() {
        Stream.Builder<FlutemStack> builder = Stream.builder();
        itemInventory.stream().map(FlutemStack::new).forEach(builder::add);
        fluidInventory.stream().map(FlutemStack::new).forEach(builder::add);
        return builder.build();
    }

    @Override
    public int getItemSize() {
        return itemInventory.size();
    }

    @Override
    public boolean hasItem() {
        return itemInventory.size() > 0;
    }

    @Override
    public boolean containsItemStack(ItemStack stack) {
        return itemInventory.stream().anyMatch(p -> ItemStack.isSame(p, stack));
    }

    @Override
    public boolean containsItem(Item item) {
        return itemInventory.stream().anyMatch(p -> p.is(item));
    }

    @Override
    public ItemStack getLastItem() {
        return itemInventory.peek();
    }

    public Optional<ItemStack> getMatching(ItemStack stack) {
        for (ItemStack invStack : itemInventory) {
            if (ItemStack.isSame(invStack, stack))
                return Optional.of(invStack);
        }
        return Optional.empty();
    }

    @Override
    public ItemStack removeLastItem() {
        if (itemInventory.empty())
            return ItemStack.EMPTY;
        usedSize--;
        var peekedItem = itemInventory.peek();
        setChanged();
        if (peekedItem.getCount() == 1)
            return itemInventory.pop();
        return peekedItem.split(1);
    }

    @Override
    public ItemStack insertItem(ItemStack stack) {
        if (usedSize < size && !stack.isEmpty()) {
            var amount = stack.getCount();
            var safeAmount = Math.min(remaining(), amount);
            var copy = stack.copy();
            copy.shrink(safeAmount);
            for (int i : ContiguousSet.closed(1, safeAmount))
                itemInventory.push(ItemHandlerHelper.copyStackWithSize(stack, 1));
            usedSize += safeAmount;
            setChanged();
            return copy;
        }
        return stack;
    }

    @Override
    public ItemStack insertItemAt(ItemStack stack, int slot) {
        if (usedSize < size && !stack.isEmpty()) {
            var amount = stack.getCount();
            var safeAmount = Math.min(remaining(), amount);
            var copy = stack.copy();
            copy.shrink(safeAmount);
            for (int i : ContiguousSet.closed(1, safeAmount))
                itemInventory.insertElementAt(ItemHandlerHelper.copyStackWithSize(stack, 1), slot);
            usedSize += safeAmount;
            setChanged();
            return copy;
        }
        return stack;
    }

    @Override
    public int getFluidSize() {
        return usedSize - itemInventory.size();
    }

    @Override
    public int getFluidAmount() {
        return getFluidSize() * 100;
    }

    @Override
    public int getFluidCapacity() {
        return 900;
    }

    @Override
    public boolean hasFluid() {
        return fluidInventory.size() > 0;
    }

    @Override
    public boolean containsFluidStack(FluidStack stack) {
        return fluidInventory.stream().anyMatch(p -> p.isFluidEqual(stack));
    }

    @Override
    public boolean containsFluid(Fluid fluid) {
        return fluidInventory.stream().anyMatch(p -> p.getFluid() == fluid);
    }

    @Override
    public FluidStack getLastFluid() {
        if (fluidInventory.isEmpty())
            return FluidStack.EMPTY;
        return fluidInventory.peek();
    }

    @Override
    public FluidStack getFluidStack(int index) {
        if (fluidInventory.isEmpty() || index < 0 || index >= fluidInventory.size())
            return FluidStack.EMPTY;
        return fluidInventory.get(index);
    }

    public Optional<FluidStack> getMatching(FluidStack stack) {
        for (FluidStack invStack : fluidInventory) {
            if (invStack.isFluidEqual(stack))
                return Optional.of(invStack);
        }
        return Optional.empty();
    }

    @Override
    public FluidStack removeLastFluid(int capacity) {
        if (fluidInventory.empty())
            return FluidStack.EMPTY;
        setChanged();
        usedSize--;
        return fluidInventory.pop();
    }

    @Override
    public FluidStack removeFluid(int index) {
        if (fluidInventory.empty() || index < 0 || index > fluidInventory.size())
            return FluidStack.EMPTY;
        setChanged();
        usedSize--;
        return fluidInventory.remove(index);
    }

    @Override
    public FluidStack insertFluid(FluidStack stack) {
        if (usedSize < size && !stack.isEmpty()) {
            var fluidAmount = stack.getAmount();
            var safeFluidAmount = Math.min(remaining() * 100, fluidAmount);
            var amount = fluidAmount / 100;
            var safeAmount = Math.min(remaining(), amount);
            for (int i : ContiguousSet.closed(1, safeAmount))
                fluidInventory.push(FluidHelper.copyStackWithSize(stack, 100));
            usedSize += safeAmount;
            var retStack = stack.copy();
            retStack.shrink(safeFluidAmount);
            setChanged();
            return retStack;
        }
        return stack;
    }

    @Override
    public FluidStack insertFluidAt(FluidStack stack, int slot) {
        if (usedSize < size && !stack.isEmpty()) {
            var fluidAmount = stack.getAmount();
            var safeFluidAmount = Math.min(remaining() * 100, fluidAmount);
            var amount = fluidAmount / 100;
            var safeAmount = Math.min(remaining(), amount);
            for (int i : ContiguousSet.closed(1, safeAmount))
                fluidInventory.insertElementAt(FluidHelper.copyStackWithSize(stack, 100), slot);
            usedSize += safeAmount;
            var retStack = stack.copy();
            retStack.shrink(safeFluidAmount);
            setChanged();
            return retStack;
        }
        return stack;
    }

    @Override
    public FluidStack setFluid(int index, FluidStack stack) {
        var ret = fluidInventory.get(index) == null ? FluidStack.EMPTY : fluidInventory.get(index);
        fluidInventory.set(index, stack);
        setChanged();
        return ret;
    }

    @Override
    public int getUsed() {
        return usedSize;
    }

    public List<FluidStack> getFluids() {
        if (fluidInventory.isEmpty())
            return List.of();
        return List.copyOf(fluidInventory);
    }

    public List<ItemStack> getItems() {
        if (itemInventory.isEmpty())
            return List.of();
        return List.copyOf(itemInventory);
    }

    @Override
    public int getContainerSize() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public ItemStack getItem(int pIndex) {
        return itemInventory.get(pIndex);
    }

    @Override
    public ItemStack removeItem(int pIndex, int pCount) {
        if (pIndex > itemInventory.size() || pIndex < 0) {
            throw new ArrayIndexOutOfBoundsException("asked for index " + pIndex + ", but index is out of bounds");
        }
        if (pCount == 0) {
            throw new IllegalStateException("count cannot equal zero");
        }
        usedSize--;
        var stack = itemInventory.get(pIndex);
        var count = Math.min(stack.getCount(), pCount);
        var copy = stack.copy();
        copy.setCount(count);
        stack.shrink(count);
        setChanged();
        return copy;
    }

    @Override
    public ItemStack removeItemNoUpdate(int pIndex) {
        var stackCopy = itemInventory.get(pIndex).copy();
        itemInventory.remove(pIndex);
        return stackCopy;
    }

    @Override
    public void setItem(int pIndex, ItemStack pStack) {
        itemInventory.set(pIndex, pStack);
        setChanged();
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
        itemInventory.removeAllElements();
        fluidInventory.removeAllElements();
        usedSize = 0;
        setChanged();
    }

    @Override
    public CompoundTag serializeNBT() {
        var tag = new CompoundTag();
        if (hasFluid()) {
            var fluidsTag = new ListTag();
            var slot = 0;
            for (FluidStack stack : fluidInventory) {
                var nbt = new CompoundTag();
                nbt.putInt(Constants.TAG_SLOT, slot++);
                nbt.put(Constants.TAG_SINGLE_FLUID, stack.writeToNBT(new CompoundTag()));
                fluidsTag.add(nbt);
            }
            tag.put(Constants.TAG_FLUIDS, fluidsTag);
        }
        if (hasItem()) {
            var itemsTag = new ListTag();
            var slot = 0;
            for (ItemStack stack : itemInventory) {
                var nbt = new CompoundTag();
                nbt.putInt(Constants.TAG_SLOT, slot++);
                nbt.put(Constants.TAG_SINGLE_ITEM, stack.serializeNBT());
                itemsTag.add(nbt);
            }
            tag.put(Constants.TAG_ITEMS, itemsTag);
        }
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag tag) {
        clearContent();
        var fluidsTag = tag.getList(Constants.TAG_FLUIDS, Tag.TAG_COMPOUND);
        if (!fluidsTag.isEmpty()) {
            for (Tag compoundTag : fluidsTag) {
                var slot = ((CompoundTag) compoundTag).getInt(Constants.TAG_SLOT);
                var fluid = FluidStack.loadFluidStackFromNBT(((CompoundTag) compoundTag).getCompound(Constants.TAG_SINGLE_FLUID));
                if (fluidInventory.size() <= slot)
                    fluidInventory.push(fluid);
                else
                    fluidInventory.set(slot, fluid);
            }
        }
        var itemsTag = tag.getList(Constants.TAG_ITEMS, Tag.TAG_COMPOUND);
        if (!itemsTag.isEmpty()) {
            for (Tag compoundTag : itemsTag) {
                var slot = ((CompoundTag) compoundTag).getInt(Constants.TAG_SLOT);
                var item = ItemStack.of(((CompoundTag) compoundTag).getCompound(Constants.TAG_SINGLE_ITEM));
                if (itemInventory.size() <= slot)
                    itemInventory.push(item);
                else
                    itemInventory.set(slot, item);
            }
        }
        this.usedSize = itemInventory.stream().mapToInt(c -> c.getCount()).sum() + fluidInventory.stream().mapToInt(c -> c.getAmount() / 100).sum();
    }

    @Override
    public void setOnChanged(Runnable methodToCall) {
        this.onChanged = methodToCall;
    }
}
