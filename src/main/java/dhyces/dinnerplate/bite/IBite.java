package dhyces.dinnerplate.bite;

import com.mojang.datafixers.util.Pair;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.List;

public interface IBite extends INBTSerializable<CompoundTag> {

	public int getNutrition();

	public float getSaturationModifier();

	public List<Pair<MobEffectInstance, Float>> getEffects();

	// TODO: figure out whether this could be useful or not. I don't see anywhere that I wrote down
	//  what this was supposed to be for, so I assume it was for the last bite, but it kind of feels like it
	//  goes against the purpose of this interface and the parenting classes
	public void onConsume(Level level, LivingEntity entity);
}
