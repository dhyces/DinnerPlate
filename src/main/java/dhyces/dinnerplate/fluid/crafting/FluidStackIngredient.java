package dhyces.dinnerplate.fluid.crafting;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.core.Registry;
import net.minecraft.nbt.TagParser;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraftforge.fluids.FluidStack;

import java.util.function.Predicate;
import java.util.stream.Stream;

public class FluidStackIngredient implements Predicate<FluidStack> {

    public static final FluidStackIngredient EMPTY = new FluidStackIngredient();

    private FluidStack[] stacks;

    public static JsonElement toJson(FluidStackIngredient ing) {
        var jsonArray = new JsonArray(ing.stacks.length);
        for (FluidStack stack : ing.stacks) {
            var jsonOBJ = new JsonObject();
            jsonOBJ.addProperty("Fluid", stack.getFluid().getRegistryName().toString());
            jsonOBJ.addProperty("Amount", stack.getAmount());
            if (stack.hasTag())
                jsonOBJ.add("nbt", JsonParser.parseString(stack.getTag().getAsString()));
            jsonArray.add(jsonOBJ);
        }
        return jsonArray;
    }

    public static FluidStackIngredient fromJson(JsonElement e) {
        if (e instanceof JsonArray arr) {
            var ing = new FluidStackIngredient();
            ing.stacks = new FluidStack[arr.size()];
            for (int i = 0; i < arr.size(); i++) {
                var obj = arr.get(i).getAsJsonObject();
                var fluid = Registry.FLUID.get(ResourceLocation.tryParse(obj.get("Fluid").getAsString()));
                var amount = obj.get("Amount").getAsInt();
                var nbtElement = obj.get("nbt");
                if (nbtElement != null) {
                    try {
                        var nbt = TagParser.parseTag(GsonHelper.getAsString(obj, "nbt"));
                        ing.stacks[i] = new FluidStack(fluid, amount, nbt);
                    } catch (CommandSyntaxException e1) {
                        e1.printStackTrace();
                    }
                } else {
                    ing.stacks[i] = new FluidStack(fluid, amount);
                }
            }
            return ing;
        }
        return EMPTY;
    }

    @Override
    public boolean test(FluidStack stack) {
        if (stack == null || stack.isEmpty())
            return false;
        return Stream.of(stacks).anyMatch(c -> c.getFluid() == stack.getFluid());
    }
}
