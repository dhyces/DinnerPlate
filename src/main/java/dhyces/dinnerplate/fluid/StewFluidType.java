package dhyces.dinnerplate.fluid;

import net.minecraftforge.client.IFluidTypeRenderProperties;
import net.minecraftforge.fluids.FluidType;

import java.util.function.Consumer;

public class StewFluidType extends FluidType {

    public StewFluidType() {
        super(FluidType.Properties.create()
                .canExtinguish(true)
                .canSwim(true)
                .canPushEntity(true)
                .density(1000)
                .viscosity(1100)
                .fallDistanceModifier(0.25F)
                .temperature(400));
    }

    @Override
    public void initializeClient(Consumer<IFluidTypeRenderProperties> consumer) {
        consumer.accept(new IFluidTypeRenderProperties() {
            @Override
            public int getColorTint() {
                return IFluidTypeRenderProperties.super.getColorTint();
            }
        });
    }
}
