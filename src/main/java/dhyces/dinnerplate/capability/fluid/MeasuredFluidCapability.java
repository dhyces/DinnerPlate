package dhyces.dinnerplate.capability.fluid;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStack;

public class MeasuredFluidCapability extends FluidHandlerItemStack {

	protected final int drainFillPreferred;
	public MeasuredFluidCapability(ItemStack container, int capacity, int preferredDrainAndFill) {
		super(container, capacity);
		this.drainFillPreferred = preferredDrainAndFill;
	}

	@Override
	public FluidStack drain(int maxDrain, FluidAction action) {
		var drain = maxDrain > drainFillPreferred ? drainFillPreferred : maxDrain;
		return super.drain(drain, action);
	}
	
	@Override
	public int fill(FluidStack resource, FluidAction doFill) {
		if (container.getCount() != 1 || resource.isEmpty() || !canFillFluidType(resource))
        {
            return 0;
        }

        FluidStack contained = getFluid();
        int preferred = resource.getAmount() > drainFillPreferred ? drainFillPreferred : resource.getAmount();
        
        if (contained.isEmpty())
        {
            int fillAmount = Math.min(capacity, preferred);

            if (doFill.execute())
            {
                FluidStack filled = resource.copy();
                filled.setAmount(fillAmount);
                setFluid(filled);
            }

            return fillAmount;
        }
        else
        {
            if (contained.isFluidEqual(resource))
            {
                int fillAmount = Math.min(capacity - contained.getAmount(), preferred);

                if (doFill.execute() && fillAmount > 0) {
                    contained.grow(fillAmount);
                    setFluid(contained);
                }

                return fillAmount;
            }

            return 0;
        }
	}
}
