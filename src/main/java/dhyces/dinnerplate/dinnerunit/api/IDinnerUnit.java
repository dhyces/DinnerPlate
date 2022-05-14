package dhyces.dinnerplate.dinnerunit.api;

import dhyces.dinnerplate.dinnerunit.DinnerUnitFluidStack;
import dhyces.dinnerplate.dinnerunit.DinnerUnitItemStack;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.function.Predicate;

public interface IDinnerUnit<T> extends Predicate<Class<T>> {

	public T getUnderlyingStack();

	public int getAmount();

	public default <E> E castOrDefault(Class<E> castTo, E orElse) {
		if (getUnderlyingStack().getClass() == castTo) {
			return castTo.cast(this.getUnderlyingStack());
		}
		return orElse;
	}

	@Override
	default boolean test(Class<T> t) {
		return getUnderlyingStack().getClass() == t;
	}

	public static <E> IDinnerUnit<?> of(E validUnit) {
		if (validUnit instanceof ItemStack e) {
			return new DinnerUnitItemStack(e);
		} else if (validUnit instanceof FluidStack e) {
			return new DinnerUnitFluidStack(e);
		}
		return null;
	}
}
