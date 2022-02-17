package dhyces.dinnerplate.fluid.crafting;

import java.util.function.Predicate;
import java.util.stream.Stream;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.util.GsonHelper;
import net.minecraftforge.fluids.FluidStack;

public class FluidStackIngredient implements Predicate<FluidStack> {

	private FluidStack[] stacks;
	
	@Override
	public boolean test(FluidStack stack) {
		if (stack == null || stack.isEmpty())
			return false;
		return Stream.of(stacks).anyMatch(c -> c.getFluid() == stack.getFluid());
	}

	public static JsonElement toJson(FluidStackIngredient ing) {
		return null;
	}
	
	public static FluidStackIngredient fromJson(JsonElement e) {
		return null;
	}
}
