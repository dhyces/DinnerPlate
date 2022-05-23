package dhyces.dinnerplate.util;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

// Obj wrapper for either a fluid or an item
public record FlutemStack(Object o) {

    public FlutemStack(Object o) {
        this.o = o;
        if (!(isItem() | isFluid()))
            throw new IllegalStateException("Object: " + o.toString() + " is not an itemstack or a fluidstack.");
    }

    /**
     * Matches with the wrapped stack
     *
     * @param oIn An itemstack or a fluidstack.
     */
    public boolean match(Object oIn) {
        return isItem() ? getItemStack().sameItem((ItemStack) oIn) : getFluidStack().isFluidEqual((FluidStack) oIn);
    }

    public int getAmount() {
        return isItem() ? getItemStack().getCount() : getFluidStack().getAmount();
    }

    public boolean isEmpty() {
        return isItem() ? getItemStack().isEmpty() : getFluidStack().isEmpty();
    }

    public ItemStack getItemStackSafe() {
        if (o instanceof ItemStack stack)
            return stack;
        return ItemStack.EMPTY;
    }

    public FluidStack getFluidStackSafe() {
        if (o instanceof FluidStack stack)
            return stack;
        return FluidStack.EMPTY;
    }

    public ItemStack getItemStack() {
        return (ItemStack) o;
    }

    public FluidStack getFluidStack() {
        return (FluidStack) o;
    }

    public boolean isItem() {
        return o instanceof ItemStack;
    }

    public boolean isFluid() {
        return o instanceof FluidStack;
    }

    @Override
    public String toString() {
        return o.toString();
    }
}
