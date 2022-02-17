package dhyces.dinnerplate.bite;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.mojang.datafixers.util.Pair;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.food.FoodProperties;
import oshi.util.tuples.Triplet;

public class BitableProperties {

	boolean isFast;
	boolean isMeat;
	boolean canAlwaysEat;
	List<IBite> bites;
	
	BitableProperties(boolean isFast, boolean isMeat, boolean canAlwaysEat, List<IBite> bites) {
		this.isFast = isFast;
		this.isMeat = isMeat;
		this.canAlwaysEat = canAlwaysEat;
		this.bites = bites;
	}
	
	public IBite getBite(int index) {
		return bites.get(index);
	}
	
	public int getBiteSize() {
		return bites.size();
	}
	
	public boolean isFast() {
		return isFast;
	}
	
	public boolean isMeat() {
		return isMeat;
	}
	
	public boolean canAlwaysEat() {
		return canAlwaysEat;
	}
	
	public FoodProperties toFoodProperties() {
		var nutrition = 0;
		var satMod = 0;
		List<Pair<MobEffectInstance, Float>> effects = new ArrayList<>();
		Map<MobEffect, Triplet<Integer, Integer, Float>> effectMap = new HashMap<>();
		for (IBite bite : bites) {
			nutrition += bite.getNutrition();
			satMod += bite.getSaturationModifier();
			bite.getEffects().forEach(c -> {
				var instance = c.getFirst();
				var chance = c.getSecond();
				effectMap.merge(instance.getEffect(), new Triplet<>(instance.getDuration(), instance.getAmplifier(), chance), (mapValue,listValue) -> {
					return new Triplet<>(mapValue.getA()+listValue.getA(), mapValue.getB(), mapValue.getC());
				});
			});
		}
		
		var builder = new FoodProperties.Builder();
		builder.nutrition(nutrition);
		builder.saturationMod(satMod);
		if (isFast)
			builder.fast();
		if (isMeat)
			builder.meat();
		if (canAlwaysEat)
			builder.alwaysEat();
		for (Entry<MobEffect, Triplet<Integer, Integer, Float>> entry : effectMap.entrySet()) {
			var effect = entry.getKey();
			var duration = entry.getValue().getA();
			var amplifier = entry.getValue().getB();
			var chance = entry.getValue().getC();
			builder.effect(() -> new MobEffectInstance(effect, duration, amplifier), chance);
		}
		return builder.build();
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
		
		public BitableProperties build() {
			return new BitableProperties(fast, meat, always, bBites);
		}
	}
}
