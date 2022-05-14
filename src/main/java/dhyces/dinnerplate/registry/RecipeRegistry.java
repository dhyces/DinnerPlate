package dhyces.dinnerplate.registry;

import dhyces.dinnerplate.DinnerPlate;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class RecipeRegistry {

	private static final DeferredRegister<RecipeSerializer<?>> RECIPE_REGISTRY = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, DinnerPlate.MODID);

	//public static final RegistryObject<RecipeSerializer<?>> MIXING_BOWL_RECIPE;

	public static void register(IEventBus bus) {
		RECIPE_REGISTRY.register(bus);
	}

	private static RegistryObject<RecipeSerializer<?>> register(String id, Supplier<RecipeSerializer<?>> sup) {
		return RECIPE_REGISTRY.register(id, sup);
	}

	static {
		//MIXING_BOWL_RECIPE = register("mixing_recipe", () -> new MixingRecipe.Serializer());
	}
}
