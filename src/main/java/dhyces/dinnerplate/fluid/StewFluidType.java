package dhyces.dinnerplate.fluid;

import net.minecraftforge.fluids.FluidType;

public class StewFluidType extends FluidType {

    public StewFluidType() {
        super(FluidType.Properties.create()
                .canExtinguish(true)
                .canSwim(true)
                .canPushEntity(true)
                .density(1000)
                .viscosity(1100)
                .temperature(400)
                .motionScale(0.007)
                .fallDistanceModifier(0.25F));
    }
}
