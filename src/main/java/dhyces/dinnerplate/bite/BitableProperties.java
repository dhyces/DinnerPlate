package dhyces.dinnerplate.bite;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import com.mojang.datafixers.util.Pair;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.food.FoodProperties;
import oshi.util.tuples.Triplet;

public class BitableProperties extends FoodProperties {

	List<IBite> bites;

	/* Preferably, use the builder, as it allows for a more readable format **/
	public BitableProperties(boolean isFast, boolean isMeat, boolean canAlwaysEat, List<IBite> bites) {
		super(	bites.stream().mapToInt(IBite::getNutrition).sum(),
				(float)bites.stream().mapToDouble(IBite::getSaturationModifier).sum(),
				isMeat,
				canAlwaysEat,
				isFast,
				bites.stream().flatMap(c -> c.getEffects().stream()).collect(Collectors.toList()));
		this.bites = bites;
	}

	public IBite getBite(int index) {
		return bites.get(index);
	}

	public int getBiteSize() {
		return bites.size();
	}

	public static class Builder {
		boolean fast;
		boolean meat;
		boolean always;
		List<IBite> bBites;

		public Builder() {
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
			this.bBites.add(bite);
			return this;
		}

		public Builder addSimpleBite(int nutrition, float saturationModifier) {
			this.bBites.add(new Bite.Builder().nutrition(nutrition).saturation(saturationModifier).build());
			return this;
		}

		public BitableProperties build() {
			return new BitableProperties(fast, meat, always, bBites);
		}
	}
}
