package dhyces.dinnerplate.fluid.crafting;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.core.Registry;
import net.minecraft.nbt.NbtIo;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.TagParser;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.NBTIngredient;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Predicate;
import java.util.stream.Stream;

public class FluidStackIngredient implements Predicate<FluidStack> {

    public static final FluidStackIngredient EMPTY = new FluidStackIngredient();

    private FluidStack[] stacks;

    public static JsonElement toJson(FluidStackIngredient ing) {
        if (ing.stacks == null || ing.stacks.length == 0)
            throw new IllegalStateException("Fluid ingredient is invalid. Either stacks is null or it contains no FluidStacks");
        if (ing.stacks.length == 1) {
            var stack = ing.stacks[0];
            var jsonOBJ = new JsonObject();
            jsonOBJ.addProperty("fluid", ForgeRegistries.FLUIDS.getKey(stack.getFluid()).toString());
            jsonOBJ.addProperty("amount", stack.getAmount());
            if (stack.hasTag())
                jsonOBJ.add("nbt", JsonParser.parseString(stack.getTag().getAsString()));
            return jsonOBJ;
        }
        var jsonArray = new JsonArray(ing.stacks.length);
        for (FluidStack stack : ing.stacks) {
            var jsonOBJ = new JsonObject();
            jsonOBJ.addProperty("fluid", ForgeRegistries.FLUIDS.getKey(stack.getFluid()).toString());
            jsonOBJ.addProperty("amount", stack.getAmount());
            if (stack.hasTag())
                jsonOBJ.add("nbt", JsonParser.parseString(stack.getTag().getAsString()));
            jsonArray.add(jsonOBJ);
        }
        return jsonArray;
    }

    public static FluidStackIngredient fromJson(JsonElement e) {
        if (e instanceof JsonObject obj) {
            var ing = new FluidStackIngredient();
            ing.stacks = new FluidStack[1];
            var fluid = ForgeRegistries.FLUIDS.getValue(ResourceLocation.tryParse(obj.get("fluid").getAsString()));
            var amount = obj.get("amount").getAsInt();
            var nbtElement = obj.get("nbt");
            var nbt = nbtElement != null ? CraftingHelper.getNBT(nbtElement) : null;
            ing.stacks[0] = new FluidStack(fluid, amount, nbt);
            return ing;
        }
        if (e instanceof JsonArray arr) {
            var ing = new FluidStackIngredient();
            ing.stacks = new FluidStack[arr.size()];
            for (int i = 0; i < arr.size(); i++) {
                var obj = arr.get(i).getAsJsonObject();
                var fluid = ForgeRegistries.FLUIDS.getValue(ResourceLocation.tryParse(obj.get("fluid").getAsString()));
                var amount = obj.get("amount").getAsInt();
                var nbtElement = obj.get("nbt");
                var nbt = nbtElement != null ? CraftingHelper.getNBT(nbtElement) : null;
                ing.stacks[i] = new FluidStack(fluid, amount, nbt);
            }
            return ing;
        }
        return EMPTY;
    }

    public static FluidStack fluidStackfromJson(JsonObject obj) {
        var fluid = ForgeRegistries.FLUIDS.getValue(ResourceLocation.tryParse(obj.get("fluid").getAsString()));
        var amount = obj.get("amount").getAsInt();
        var nbtElement = obj.get("nbt");
        var nbt = nbtElement != null ? CraftingHelper.getNBT(nbtElement) : null;
        return new FluidStack(fluid, amount, nbt);
    }

    @Override
    public boolean test(FluidStack stack) {
        if (stack == null || stack.isEmpty())
            return false;
        return Stream.of(stacks).anyMatch(c -> c.getFluid() == stack.getFluid());
    }
}
