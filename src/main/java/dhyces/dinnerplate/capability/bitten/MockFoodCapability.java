package dhyces.dinnerplate.capability.bitten;

import dhyces.dinnerplate.Constants;
import dhyces.dinnerplate.DinnerPlate;
import dhyces.dinnerplate.bite.Bite;
import dhyces.dinnerplate.capability.CapabilityEventSubscriber;
import dhyces.dinnerplate.util.Couple;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

public class MockFoodCapability extends MockFoodProvider implements ICapabilitySerializable<CompoundTag> {

	public static final ResourceLocation CAP_ID = new ResourceLocation(DinnerPlate.MODID, "mock_food_cap");

	private final LazyOptional<IMockFoodProvider> PROVIDER = LazyOptional.of(() -> this);

	@Override
	public CompoundTag serializeNBT() {
		var tag = new CompoundTag();
		if (!getRealStack().isEmpty()) {
			tag.put(Constants.TAG_SINGLE_ITEM, getRealStack().serializeNBT());
			tag.putInt(Constants.TAG_BITE_COUNT, getBiteCount());
			tag.put(Constants.TAG_FIRST_BITE, getBite(0).serializeNBT());
			tag.put(Constants.TAG_LAST_BITE, getBite(2).serializeNBT());
		}
		var returnTag = new CompoundTag();
		returnTag.put(CAP_ID.toString(), tag);
		return returnTag;
	}

	@Override
	public void deserializeNBT(CompoundTag nbt) {
		var tag = nbt.getCompound(CAP_ID.toString());
		if (!tag.getCompound(Constants.TAG_SINGLE_ITEM).isEmpty()) {
			this.stack = ItemStack.of(tag.getCompound(Constants.TAG_SINGLE_ITEM));
			this.chewCount = tag.getInt(Constants.TAG_BITE_COUNT);
			var firstBite = new Bite.Builder().build();
			firstBite.deserializeNBT(tag.getCompound(Constants.TAG_FIRST_BITE));
			var lastBite = new Bite.Builder().build();
			lastBite.deserializeNBT(tag.getCompound(Constants.TAG_LAST_BITE));
			this.bites = Couple.coupleOf(firstBite, lastBite);
		}
	}

	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
		return CapabilityEventSubscriber.MOCK_FOOD_CAPABILITY.orEmpty(cap, PROVIDER);
	}
}
