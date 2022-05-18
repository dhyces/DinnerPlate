package dhyces.dinnerplate.capability.mixed;

import dhyces.dinnerplate.inventory.MixedInventory;
import dhyces.dinnerplate.inventory.api.IMixedInventory;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import java.util.Optional;

public class MixedCapability implements ICapabilitySerializable<CompoundTag>, IItemHandler, IFluidHandler {

    IMixedInventory inv;
    ItemStack container;

    public MixedCapability(IMixedInventory mixedInv) {
        this.inv = mixedInv;
    }

    @Override
    public int getSlots() {
        return inv.getItemSize();
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return inv.getItem(slot);
    }

    @Override
    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
        if (slot < 0 || slot >= getSlots())
            return stack;
        if (simulate)
            return stack.copy().split(inv.remaining());
        return inv.insertItemAt(stack, slot);
    }

    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        if (slot < 0 || slot >= getSlots())
            return ItemStack.EMPTY;
        if (simulate)
            return getStackInSlot(slot).copy().split(amount);
        return inv.removeItem(slot, amount);
    }

    @Override
    public int getSlotLimit(int slot) {
        return 1;
    }

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
        return true;
    }

    @Override
    public int getTanks() {
        return inv.getFluidSize();
    }

    @Override
    public FluidStack getFluidInTank(int tank) {
        return inv.getFluidStack(tank);
    }

    @Override
    public int getTankCapacity(int tank) {
        return 100;
    }

    @Override
    public boolean isFluidValid(int tank, FluidStack stack) {
        return true;
    }

    @Override
    public int fill(FluidStack resource, FluidAction action) {
        if (resource.isEmpty())
            return 0;
        var maxFill = Math.min(inv.remaining() * 100, resource.getAmount());
        if (action.execute() && maxFill > 0) {
            inv.insertFluid(resource);
        }
        return maxFill;
    }

    @Override
    public FluidStack drain(FluidStack resource, FluidAction action) {
        if (resource.isEmpty())
            return resource;
        int i = 0;
        Optional<FluidStack> stack = Optional.empty();
        while (i < getTanks()) {
            var next = getFluidInTank(i);
            if (next.isFluidEqual(resource)) {
                stack = Optional.of(next);
                break;
            }
            i++;
        }
        var ret = stack.orElse(FluidStack.EMPTY);
        var maxDrain = Math.min(ret.getAmount(), resource.getAmount());
        if (action.execute()) {
            if (maxDrain == ret.getAmount())
                ret = inv.removeFluid(i);
            else
                ret.setAmount(maxDrain);
        } else {
            ret = ret.copy();
            ret.setAmount(maxDrain);
        }
        return ret;
    }

    @Override
    public FluidStack drain(int maxDrain, FluidAction action) {
        if (maxDrain == 0)
            return FluidStack.EMPTY;
        var stack = inv.getLastFluid();
        if (stack.isEmpty())
            return FluidStack.EMPTY;
        var trueMax = Math.min(stack.getAmount(), maxDrain);
        if (action.execute()) {
            if (trueMax == stack.getAmount())
                stack = inv.removeLastFluid(trueMax);
            else
                stack.setAmount(trueMax);
        } else {
            stack = stack.copy();
            stack.setAmount(trueMax);
        }
        return stack;
    }

    @Override
    public CompoundTag serializeNBT() {
        if (inv instanceof MixedInventory impl)
            return impl.serializeNBT();
        return new CompoundTag();
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        if (inv instanceof MixedInventory impl)
            impl.deserializeNBT(nbt);
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        var lazy = CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.orEmpty(cap, LazyOptional.of(() -> this));
        if (lazy.isPresent())
            return lazy;
        return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.orEmpty(cap, LazyOptional.of(() -> this));
    }

    public static class Item extends MixedCapability implements IFluidHandlerItem {

        public Item(ItemStack container, IMixedInventory mixedInv) {
            super(mixedInv);
            this.container = container;
            deserializeNBT(this.container.getOrCreateTag());
        }

        @Override
        public ItemStack getContainer() {
            return this.container;
        }

        @Override
        public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
            var lazy = CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY.orEmpty(cap, LazyOptional.of(() -> this));
            return lazy.isPresent() ? lazy : super.getCapability(cap, side);
        }
    }
}
