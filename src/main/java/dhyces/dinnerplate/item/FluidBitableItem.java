package dhyces.dinnerplate.item;

import dhyces.dinnerplate.bite.BitableProperties;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;

import java.util.function.Supplier;

public class FluidBitableItem extends BitableItem implements IFluidItem {

    final Supplier<? extends Fluid> fluid;
    final int amount;

    public <T extends Fluid> FluidBitableItem(Supplier<T> fluid, BitableProperties biteProperties, Properties pProperties) {
        this(fluid, 100, biteProperties, pProperties);
    }
    public <T extends Fluid> FluidBitableItem(Supplier<T> fluid, int amount, BitableProperties biteProperties, Properties pProperties) {
        super(biteProperties, pProperties);
        this.fluid = fluid;
        this.amount = amount;
    }

    @Override
    public Fluid getFluid() {
        return this.fluid.get();
    }

    @Override
    public FluidStack createStack(ItemStack stack, LivingEntity entity) {
        return new FluidStack(this.fluid.get(), amount);
    }
}
