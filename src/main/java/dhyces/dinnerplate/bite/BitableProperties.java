package dhyces.dinnerplate.bite;

import com.mojang.datafixers.util.Pair;
import dhyces.dinnerplate.util.Couple;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.food.FoodProperties;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class BitableProperties extends FoodProperties {

	List<IBite> bites;
	int[] traversal;

	/* Preferably, use the builder, as it allows for a more readable format **/
	public BitableProperties(boolean isFast, boolean isMeat, boolean canAlwaysEat, List<IBite> bites, int[] traversal) {
		super(	bites.stream().mapToInt(IBite::getNutrition).sum(),
				(float)bites.stream().mapToDouble(IBite::getSaturationModifier).sum(),
				isMeat,
				canAlwaysEat,
				isFast,
				bites.stream().flatMap(c -> c.getEffects().stream()).collect(Collectors.toList()));
		this.bites = bites;
		this.traversal = traversal;
	}

	public int getMaxBites() {
		return traversal.length;
	}

	/** Most of the time, this is what you will want to use to get the accurate info for the given bite.
	 *  When doing custom things, like randomly getting a bite, it may be more accurate to use {@link BitableProperties#getBite(int)}*/
	public IBite traverse(int bite) {
		return bites.get(traversal[bite]);
	}

	public IBite getBite(int index) {
		return bites.get(index);
	}

	public int getBiteSize() {
		return bites.size();
	}

	public static BitableProperties threeBite(FoodProperties foodProperties) {
		var bites = splitInto(foodProperties, 3);
		return new BitableProperties(foodProperties.isFastFood(), foodProperties.isMeat(), foodProperties.canAlwaysEat(), bites, new int[] {0, 0, 1});
	}

	/** This splits as best it can and the last entry contains whatever could not be evenly divided*/
	public static List<IBite> splitInto(FoodProperties propertiesToSplit, int amount) {
		var list = new ArrayList<IBite>();
		var couple = split(propertiesToSplit.getNutrition(), propertiesToSplit.getNutrition(), propertiesToSplit.getEffects(), amount);
		list.add(couple.getFirst());
		list.add(couple.getSecond());
		return list;
	}

	private static Couple<IBite> split(int nutrition, int saturation, List<Pair<MobEffectInstance, Float>> effects, int splitTo) {
		var builder = new Bite.Builder();
		var floorNutrition = (int)((double)nutrition / (double)splitTo);
		var moduloNutrition = nutrition % splitTo;
		var floorSaturation = saturation / (float)splitTo;
		builder.nutrition(floorNutrition)
				.saturation(floorSaturation);

		var builderRemainder = new Bite.Builder();
		builderRemainder.nutrition(floorNutrition + moduloNutrition)
				.saturation(floorSaturation);

		if (!effects.isEmpty()) {
			var effectsCouple = splitAllEffects(effects, splitTo);
			for (Pair<MobEffectInstance, Float> pair : effectsCouple.getFirst()) {
				builder.addEffect(pair.getFirst(), pair.getSecond());
			}
			for (Pair<MobEffectInstance, Float> pair : effectsCouple.getSecond()) {
				builderRemainder.addEffect(pair.getFirst(), pair.getSecond());
			}
		}
		return Couple.coupleOf(builder.build(), builderRemainder.build());
	}

	private static Couple<List<Pair<MobEffectInstance, Float>>> splitAllEffects(List<Pair<MobEffectInstance, Float>> effects, int splitTo) {
		var list = new ArrayList<Pair<MobEffectInstance, Float>>();
		var listRemainder = new ArrayList<Pair<MobEffectInstance, Float>>();
		for (Pair<MobEffectInstance, Float> pair : effects) {
			var splitEffect = splitEffect(pair, splitTo);
			list.add(splitEffect.getFirst());
			listRemainder.add(splitEffect.getSecond());
		}
		return Couple.coupleOf(list, listRemainder);
	}

	private static Couple<Pair<MobEffectInstance, Float>> splitEffect(Pair<MobEffectInstance, Float> effect, int splitTo) {
		var original = effect.getFirst();
		int modifiedDuration = (int) ((double)original.getDuration() / (double)splitTo);
		var modified = new MobEffectInstance(original.getEffect(), modifiedDuration, original.getAmplifier());
		var remainder = original.getDuration() % splitTo;
		var modifiedRemainder = new MobEffectInstance(original.getEffect(), modifiedDuration + remainder, original.getAmplifier());
		return Couple.coupleOf(Pair.of(modified, effect.getSecond()), Pair.of(modifiedRemainder, effect.getSecond()));
	}
	
	public static class Builder {
		List<Integer> traversal;
		boolean fast;
		boolean meat;
		boolean always;
		List<IBite> bBites;

		public Builder() {
			traversal = new ArrayList<>();
			fast = false;
			meat = false;
			always = false;
			bBites = new ArrayList<>();
		}

		public Builder fast() {
			this.fast = true;
			return this;
		}

		public Builder meat() {
			this.meat = true;
			return this;
		}

		public Builder always() {
			this.always = true;
			return this;
		}

		public Builder addBite(IBite bite) {
			traversal.add(bBites.size());
			this.bBites.add(bite);
			return this;
		}

		public Builder addSimpleBite(int nutrition, float saturationModifier) {
			this.addBite(new Bite(nutrition, saturationModifier));
			return this;
		}
		
		public BitableProperties build() {
			return build(null);
		}

		/** Must not include an index that is greater than the number of bites*/
		public BitableProperties build(int... traversalIndices) {
			return new BitableProperties(fast, meat, always, bBites, traversalIndices == null ? traversal.stream().mapToInt(Integer::intValue).toArray() : traversalIndices);
		}
	}
}
