package dhyces.dinnerplate.blockentity.api;

import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;

public interface IFluidHolder {

	/** Returns whether or not the workstation contains any fluids */
	public boolean hasFluid();

	/** Returns whether or not the workstation contains the specific stack */
	public boolean containsFluidStack(FluidStack stack);

	/** Returns whether or not the workstation contains the given fluid */
	public boolean containsFluid(Fluid fluid);

	/** Should return the last inserted fluidstack */
	public FluidStack getLastFluid();

	/** Should return the fluidstack in the given index. Is negligible if the object holds only a single instance */
	public FluidStack getFluidStack(int index);

	/** Should return last fluid up to the capacity. If the fluid amount is less than the capacity, remove the fluid. */
	public FluidStack removeLastFluid(int capacity);

	/** Inserts as much of the given fluid as possible and returns what's left */
	public FluidStack insertFluid(FluidStack stack);

	/** Sets the fluidstack into the given index*/
	public FluidStack setFluid(int index, FluidStack stack);
	
	/** Returns the amount of fluid this holder contains in millibuckets*/
	public int getFluidAmount();
}
