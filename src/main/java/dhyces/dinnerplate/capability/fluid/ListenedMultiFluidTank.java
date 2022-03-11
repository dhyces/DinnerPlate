package dhyces.dinnerplate.capability.fluid;

import java.util.Optional;
import java.util.function.Function;

import com.google.common.base.Supplier;

import dhyces.dinnerplate.inventory.api.IFluidHolder;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

@Deprecated(forRemoval = true) /** Unused in the current iteration, if it is unused in the next few iterations, remove*/
public class ListenedMultiFluidTank implements IFluidHandler {

	private final Supplier<Integer> getTanks;
	private final Supplier<Integer> getMaxTanks;
	private final Function<Integer, FluidStack> getFluidInTank;
	private final Function<FluidStack, FluidStack> fill;
	private final Function<Integer, FluidStack> drain;
	
	public ListenedMultiFluidTank(IFluidHolder holder) {
		getTanks = () -> {return holder.getFluidSize();};
		getMaxTanks = () -> {return holder.getFluidCapacity();};
		getFluidInTank = tank -> {return holder.getFluidStack(tank);};
		fill = fluid -> {return holder.insertFluid(fluid);};
		drain = index -> {return holder.removeFluid(index);};
	}
	
	@Override
	public int getTanks() {
		return getTanks.get();
	}

	@Override
	public FluidStack getFluidInTank(int tank) {
		return getFluidInTank.apply(tank);
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
		var maxFill = Math.min((getMaxTanks.get() - getTanks()) * 100, resource.getAmount());
		if (action.execute() && maxFill > 0) {
			fill.apply(resource);
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
				ret = drain.apply(i);
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
		var stack = getFluidInTank(getTanks()-1);
		if (stack.isEmpty())
			return FluidStack.EMPTY;
		var trueMax = Math.min(stack.getAmount(), maxDrain);
		if (action.execute()) {
			if (trueMax == stack.getAmount())
				stack = drain.apply(getTanks()-1);
			else
				stack.setAmount(trueMax);
		} else {
			stack = stack.copy();
			stack.setAmount(trueMax);
		}
		return stack;
	}

}
