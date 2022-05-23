package dhyces.dinnerplate.inventory.api;

import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;

public interface IFluidHolder extends IInventory {

    /**
     * Returns whether or not the workstation contains any fluids
     */
    boolean hasFluid();

    /**
     * Returns whether or not the workstation contains the specific stack
     */
    boolean containsFluidStack(FluidStack stack);

    /**
     * Returns whether or not the workstation contains the given fluid
     */
    boolean containsFluid(Fluid fluid);

    /**
     * Should return the last inserted fluidstack
     */
    FluidStack getLastFluid();

    /**
     * Should return the fluidstack in the given index. Is negligible if the object holds only a single instance
     */
    FluidStack getFluidStack(int index);

    /**
     * Should return last fluid up to the capacity. If the fluid amount is less than the capacity, remove the fluid.
     */
    FluidStack removeLastFluid(int capacity);

    /**
     * Inserts as much of the given fluid as possible and returns what's left
     */
    FluidStack insertFluid(FluidStack stack);

    /**
     * Inserts as much of the given fluid as possible at the given slot and returns what's left
     */
    FluidStack insertFluidAt(FluidStack stack, int slot);

    /**
     * Sets the fluidstack into the given index
     */
    FluidStack setFluid(int index, FluidStack stack);

    /**
     * Removes the fluid from the given index and returns it
     */
    FluidStack removeFluid(int index);

    /**
     * Returns the amount of fluid this holder contains in millibuckets
     */
    int getFluidAmount();

    /**
     * Returns the number of fluids
     */
    int getFluidSize();

    /**
     * Returns the max number of fluids
     */
    int getFluidCapacity();
}
