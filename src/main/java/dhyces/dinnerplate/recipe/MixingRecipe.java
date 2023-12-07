package dhyces.dinnerplate.recipe;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dhyces.dinnerplate.DinnerPlate;
import dhyces.dinnerplate.blockentity.MixingBowlBlockEntity;
import dhyces.dinnerplate.fluid.crafting.FluidStackIngredient;
import dhyces.dinnerplate.inventory.MixedInventory;
import dhyces.dinnerplate.registry.RecipeRegistry;
import dhyces.dinnerplate.util.FlutemStack;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

public class MixingRecipe implements Recipe<MixedInventory> {

    public static final RecipeType<MixingRecipe> MIXING_TYPE = RecipeType.simple(DinnerPlate.modLoc("mixing"));

    final ResourceLocation resourceLocation;
    final Predicate<Object>[] ingredients;
    final FlutemStack result;
    final ResultState type;

    public MixingRecipe(ResourceLocation resourceLocation, Predicate<Object>[] ingredients, FlutemStack result) {
        this.resourceLocation = resourceLocation;
        this.ingredients = ingredients;
        this.result = result;
        this.type = result.isItem() ? ResultState.ITEM : ResultState.FLUID;
    }

    @Override
    public boolean matches(MixedInventory pContainer, Level pLevel) {
        List<FlutemStack> mixed = pContainer.mixedStream().toList();
        List<Predicate<Object>> ingredients = Arrays.asList(this.ingredients).stream().collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
        for (int i = 0; i < ingredients.size();) {
            var finalI = i;
            var matched = mixed.stream().anyMatch(c -> {
                var p = ingredients.get(finalI);
                if (c.isItem() && p.getClass() == Ingredient.class)
                    return p.test(c.getItemStack());
                else if (c.isFluid() && p.getClass() == FluidStackIngredient.class)
                    return p.test(c.getFluidStack());
                return false;
            });
            if (matched)
                ingredients.remove(i);
            else
                i++;
        }
        return ingredients.isEmpty();
    }

    @Override
    public ItemStack assemble(MixedInventory pContainer, RegistryAccess registryAccess) {
        return result.getItemStack().copy();
    }

    public FluidStack assembleFluid(MixedInventory pContainer) {
        return result.getFluidStack().copy();
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess registryAccess) {
        return result.getItemStackSafe().copy();
    }

    @Override
    public ResourceLocation getId() {
        return resourceLocation;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RecipeRegistry.MIXING_BOWL_RECIPE.get();
    }

    public ResultState getResultState() {
        return this.type;
    }

    @Override
    public RecipeType<?> getType() {
        return MIXING_TYPE;
    }

    public static class Serializer implements RecipeSerializer<MixingRecipe> {

        @Override
        public MixingRecipe fromJson(ResourceLocation pRecipeId, JsonObject pSerializedRecipe) {
            List<Predicate<?>> ings = new ArrayList<>(10);
            var jsonIngs = pSerializedRecipe.getAsJsonArray("ingredients");
            for (JsonElement element : jsonIngs) {
                if (element instanceof JsonObject obj) {
                    if (obj.has("fluid")) {
                        ings.add(FluidStackIngredient.fromJson(obj));
                    } else {
                        ings.add(Ingredient.fromJson(obj));
                    }
                }
            }
            var jsonResult = pSerializedRecipe.getAsJsonObject("result");
            FlutemStack stack;
            if (jsonResult.has("fluid")) {
                stack = new FlutemStack(FluidStackIngredient.fluidStackfromJson(jsonResult));
            } else {
                stack = new FlutemStack(CraftingHelper.getItemStack(jsonResult, true));
            }
            return new MixingRecipe(pRecipeId, ings.toArray(Predicate[]::new), stack);
        }

        @Nullable
        @Override
        public MixingRecipe fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
            return null;
        }

        @Override
        public void toNetwork(FriendlyByteBuf pBuffer, MixingRecipe pRecipe) {

        }
    }
}
