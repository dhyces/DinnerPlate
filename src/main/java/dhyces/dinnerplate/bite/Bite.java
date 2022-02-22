package dhyces.dinnerplate.bite;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.mojang.datafixers.util.Pair;

import dhyces.dinnerplate.Constants;
import dhyces.dinnerplate.util.Couple;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.food.FoodProperties;

/** Not to be confused with {@link java.lang.Byte}*/
public class Bite implements IBite {

	private int nutrition;
	private float saturationMod;
	private List<Pair<MobEffectInstance, Float>> effectsList;

	Bite(int nutritionIn, float saturationModIn, List<Pair<MobEffectInstance, Float>> effectsListIn) {
		this.nutrition = nutritionIn;
		this.saturationMod = saturationModIn;
		this.effectsList = effectsListIn;
	}

	@Override
	public int getNutrition() {
		return nutrition;
	}

	@Override
	public float getSaturationModifier() {
		return saturationMod;
	}

	@Override
	public List<Pair<MobEffectInstance, Float>> getEffects() {
		return effectsList;
	}

	@Override
	public CompoundTag serializeNBT() {
		var tag = new CompoundTag();
		tag.putInt(Constants.TAG_NUTRITION, nutrition);
		tag.putFloat(Constants.TAG_SATURATION_MOD, saturationMod);
		ListTag effectsTag = new ListTag();
		effectsList.forEach(c -> effectsTag.add(combineEffectAndChance(c.getFirst(), c.getSecond())));
		tag.put(Constants.TAG_EFFECTS_LIST, effectsTag);
		return tag;
	}

	private CompoundTag combineEffectAndChance(MobEffectInstance effect, float chance) {
		var tag = effect.save(new CompoundTag());
		tag.putFloat(Constants.TAG_EFFECT_CHANCE, chance);
		return tag;
	}

	@Override
	public void deserializeNBT(CompoundTag nbt) {
		this.nutrition = nbt.getInt(Constants.TAG_NUTRITION);
		this.saturationMod = nbt.getFloat(Constants.TAG_SATURATION_MOD);
		var effectsTag = nbt.getList(Constants.TAG_EFFECTS_LIST, 10);
		effectsTag.forEach(c -> {
			CompoundTag castedTag = (CompoundTag) c;
			effectsList.add(Pair.of(MobEffectInstance.load(castedTag), castedTag.getFloat(Constants.TAG_EFFECT_CHANCE)));
		});
	}

	@Override
	public String toString() {
		var string = "{ Nutrition: " + nutrition;
		string += ", Saturation Mod: " + saturationMod;
		string += ", Effects: " + Arrays.toString(effectsList.toArray());
		return string;
	}

	/** This splits as best it can and the last entry contains whatever could not be evenly divided*/
	@Override
	public List<IBite> splitInto(FoodProperties propertiesToSplit, int amount) {
		var list = new ArrayList<IBite>();
		var couple = split(propertiesToSplit.getNutrition(), propertiesToSplit.getNutrition(), propertiesToSplit.getEffects(), amount);
		list.add(couple.getFirst());
		list.add(couple.getSecond());
		return list;
	}

	private Couple<IBite> split(int nutrition, int saturation, List<Pair<MobEffectInstance, Float>> effects, int splitTo) {
		var builder = new Builder();
		var flooredNutrition = (int)((double)nutrition / (double)splitTo);
		builder.nutrition(flooredNutrition)
			   .saturation(nutrition / (float)splitTo);

		var builderLast = new Builder();
		builderLast.nutrition(flooredNutrition + (nutrition % splitTo))
			   .saturation(nutrition / (float)splitTo);

		if (!effects.isEmpty()) {
			var effectsCouple = splitAllEffects(effects, splitTo);
			for (Pair<MobEffectInstance, Float> pair : effectsCouple.getFirst()) {
				builder.addEffect(pair.getFirst(), pair.getSecond());
			}
			for (Pair<MobEffectInstance, Float> pair : effectsCouple.getSecond()) {
				builderLast.addEffect(pair.getFirst(), pair.getSecond());
			}
		}
		return Couple.coupleOf(builder.build(), builderLast.build());
	}

	private Couple<List<Pair<MobEffectInstance, Float>>> splitAllEffects(List<Pair<MobEffectInstance, Float>> effects, int splitTo) {
		var list = new ArrayList<Pair<MobEffectInstance, Float>>();
		var listLast = new ArrayList<Pair<MobEffectInstance, Float>>();
		for (Pair<MobEffectInstance, Float> pair : effects) {
			var splitEffect = splitEffect(pair, splitTo);
			list.add(splitEffect.getFirst());
			listLast.add(splitEffect.getSecond());
		}
		return Couple.coupleOf(list, listLast);
	}

	private Couple<Pair<MobEffectInstance, Float>> splitEffect(Pair<MobEffectInstance, Float> effect, int splitTo) {
		var original = effect.getFirst();
		int modifiedDuration = (int) ((double)original.getDuration() / (double)splitTo);
		var modified = new MobEffectInstance(original.getEffect(), modifiedDuration, original.getAmplifier());
		var last = original.getDuration() % splitTo;
		var modifiedLast = new MobEffectInstance(original.getEffect(), modifiedDuration + last, original.getAmplifier());
		return Couple.coupleOf(Pair.of(modified, effect.getSecond()), Pair.of(modifiedLast, effect.getSecond()));
	}

	public static class Builder {
		private int builderNutrition;
		private float builderSaturationMod;
		private List<Pair<MobEffectInstance, Float>> builderEffectsList;

		public Builder() {
			builderNutrition = 0;
			builderSaturationMod = 0;
			builderEffectsList = new ArrayList<>();
		}

		public IBite build() {
			return new Bite(builderNutrition, builderSaturationMod, builderEffectsList);
		}

		public Builder nutrition(int halfBars) {
			this.builderNutrition = halfBars;
			return this;
		}

		public Builder saturation(float saturationMod) {
			builderSaturationMod = saturationMod;
			return this;
		}

		public Builder addEffect(MobEffectInstance effectInstance, float chance) {
			builderEffectsList.add(Pair.of(effectInstance, chance));
			return this;
		}
	}
}
