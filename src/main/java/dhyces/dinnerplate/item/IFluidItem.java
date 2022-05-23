package dhyces.dinnerplate.item;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;

public interface IFluidItem {

    Fluid getFluid();

    FluidStack createStack(ItemStack stack, LivingEntity entity);
}
