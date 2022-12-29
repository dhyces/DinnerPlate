package dhyces.dinnerplate.bite;

import com.mojang.datafixers.util.Pair;
import dhyces.dinnerplate.Constants;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/** Not to be confused with {@link java.lang.Byte}*/
public class Bite implements IBite {

	private int nutrition;
	private float saturationMod;
	private List<Pair<MobEffectInstance, Float>> effectsList;

	Bite(int nutrition, float saturationMod) {
		this(nutrition, saturationMod, List.of());
	}

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
	public void onConsume(Level level, LivingEntity entity) {
		/*NO-OP*/
	}

	@Override
	public CompoundTag serializeNBT() {
		var tag = new CompoundTag();
		tag.putInt(Constants.NUTRITION_TAG, nutrition);
		tag.putFloat(Constants.SATURATION_MOD_TAG, saturationMod);
		ListTag effectsTag = new ListTag();
		effectsList.forEach(c -> effectsTag.add(combineEffectAndChance(c.getFirst(), c.getSecond())));
		tag.put(Constants.EFFECTS_LIST_TAG, effectsTag);
		return tag;
	}

	private CompoundTag combineEffectAndChance(MobEffectInstance effect, float chance) {
		var tag = effect.save(new CompoundTag());
		tag.putFloat(Constants.EFFECT_CHANCE_TAG, chance);
		return tag;
	}

	@Override
	public void deserializeNBT(CompoundTag nbt) {
		this.nutrition = nbt.getInt(Constants.NUTRITION_TAG);
		this.saturationMod = nbt.getFloat(Constants.SATURATION_MOD_TAG);
		var effectsTag = nbt.getList(Constants.EFFECTS_LIST_TAG, Tag.TAG_COMPOUND);
		effectsTag.forEach(c -> {
			CompoundTag castedTag = (CompoundTag) c;
			effectsList.add(Pair.of(MobEffectInstance.load(castedTag), castedTag.getFloat(Constants.EFFECT_CHANCE_TAG)));
		});
	}

	@Override
	public String toString() {
		var string = "{ Nutrition: " + nutrition;
		string += ", Saturation Mod: " + saturationMod;
		string += ", Effects: " + Arrays.toString(effectsList.toArray());
		return string;
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

		public Builder addEffect(MobEffect effect, int amplifier, int duration, float chance) {
			return addEffect(new MobEffectInstance(effect, duration, amplifier), chance);
		}

		public Builder addEffect(MobEffectInstance effectInstance, float chance) {
			builderEffectsList.add(Pair.of(effectInstance, chance));
			return this;
		}
	}
}
