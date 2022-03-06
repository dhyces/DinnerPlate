package dhyces.dinnerplate.capability.bitten;

import dhyces.dinnerplate.bite.Bite;
import dhyces.dinnerplate.bite.IBite;
import dhyces.dinnerplate.util.Couple;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class MockFoodProvider implements IMockFoodProvider {

	protected ItemStack stack = ItemStack.EMPTY;

	protected int chewCount;

	/** The first of the pair is used for first and second bites, the second of the pair is used for the last bite*/
	protected Couple<IBite> bites;

	public MockFoodProvider() {
		chewCount = 0;
		bites = Couple.coupleOf(new Bite.Builder().build(), new Bite.Builder().build());
	}

	@Override
	public void initialize(ItemStack stack, int chewCount) {
		if (!stack.isEdible()) return;
		this.stack = stack;
		this.chewCount = chewCount;
		var biteList = new Bite.Builder().build().splitInto(stack.getItem().getFoodProperties(), 3);
		// TODO: there's an issue here in the case there is an array out of bounds error if the splitInto method returns a list with a
		// size less than 2
		this.bites = Couple.coupleOf(biteList.get(0), biteList.get(1));
	}

	@Override
	public ItemStack getRealStack() {
		return stack;
	}

	@Override
	public SoundEvent getEatingSound(ItemStack stack) {
		return this.stack.getEatingSound();
	}
	
	@Override
	public ParticleOptions getParticle(ItemStack stack) {
		return new ItemParticleOption(ParticleTypes.ITEM, this.stack);
	}

	@Override
	public ItemStack finish(ItemStack stackIn, Level level, LivingEntity livingEntity) {
		var stackCopy = this.stack.copy();
		if (livingEntity instanceof Player p) {
			var tempStorage = new CompoundTag();
			var ability = p.getAbilities().instabuild;
			p.getFoodData().addAdditionalSaveData(tempStorage);
			p.getAbilities().instabuild = false;
			var ret = stackCopy.finishUsingItem(level, livingEntity);
			stackCopy.setCount(1);
			p.getAbilities().instabuild = ability;
			p.getFoodData().readAdditionalSaveData(tempStorage);
			return ret.equals(stackCopy) ? this.stack.getContainerItem() : ret;
		}
		return stack;
	}

	@Override
	public int getBiteCount() {
		return chewCount;
	}

	@Override
	public int getMaxBites() {
		return 3;
	}

	@Override
	public boolean incrementBiteCount() {
		return ++chewCount >= getMaxBiteCount(stack);
	}

	@Override
	public void setBiteCount(int count) {
		this.chewCount = count;
	}

	@Override
	public IBite getBite(int chew) {
		return chew < 2 ? bites.getFirst() : bites.getSecond();
	}

	@Override
	public boolean canBeFast() {
		return stack.getItem().getFoodProperties().isFastFood();
	}

	@Override
	public boolean isMeat() {
		return stack.getItem().getFoodProperties().isMeat();
	}

	@Override
	public boolean canAlwaysEat() {
		return stack.getItem().getFoodProperties().canAlwaysEat();
	}
}
