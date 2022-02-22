package dhyces.dinnerplate.bite;

import java.util.List;

import com.mojang.datafixers.util.Pair;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.food.FoodProperties;
import net.minecraftforge.common.util.INBTSerializable;

public interface IBite extends INBTSerializable<CompoundTag> {

	public int getNutrition();

	public float getSaturationModifier();

	public List<Pair<MobEffectInstance, Float>> getEffects();

	public List<IBite> splitInto(FoodProperties propertiesToSplit, int amount);

}
