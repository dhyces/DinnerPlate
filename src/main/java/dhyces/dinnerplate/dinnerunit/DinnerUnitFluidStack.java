package dhyces.dinnerplate.dinnerunit;

import dhyces.dinnerplate.dinnerunit.api.AbstractDinnerUnit;
import net.minecraftforge.fluids.FluidStack;

public class DinnerUnitFluidStack extends AbstractDinnerUnit<FluidStack> {

    public DinnerUnitFluidStack(FluidStack wrappedStack) {
        super(wrappedStack);
    }

    @Override
    public int getAmount() {
        return unit.getAmount();
    }

}
