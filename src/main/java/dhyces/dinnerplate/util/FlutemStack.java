package dhyces.dinnerplate.util;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

// Obj wrapper for either a fluid or an item
public class FlutemStack {

    private final Object o;

    public FlutemStack(Object o) {
        this.o = o;
        if (!(isItem() | isFluid()))
            throw new IllegalStateException("Object: " + o.toString() + " is not an itemstack or a fluidstack.");
    }

    /**
     * Matches with the wrapped stack
     *
     * @param o An itemstack or a fluidstack.
     */
    public boolean match(Object oIn) {
        return isItem() ? getItemStack().sameItem((ItemStack) oIn) : getFluidStack().isFluidEqual((FluidStack) oIn);
    }

    public boolean isEmpty() {
        return isItem() ? getItemStack().isEmpty() : getFluidStack().isEmpty();
    }

    public ItemStack getItemStack() {
        if (isItem())
            return (ItemStack) o;
        return ItemStack.EMPTY;
    }

    public FluidStack getFluidStack() {
        if (isFluid())
            return (FluidStack) o;
        return FluidStack.EMPTY;
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
