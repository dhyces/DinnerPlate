package dhyces.dinnerplate.util;

import dhyces.dinnerplate.Constants;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.properties.Property;

public class BlockHelper {

	public static CompoundTag getBlockStateTag(ItemStack stack) {
		return stack.getOrCreateTag().getCompound(Constants.TAG_BLOCK_STATE);
	}

	public static CompoundTag getBlockEntityTag(ItemStack stack) {
		return stack.getOrCreateTag().getCompound(Constants.TAG_BLOCK_ENTITY);
	}

	public static <E extends Comparable<E>> E getPropertyFromTag(Property<E> property, CompoundTag tag) {
		return property.getValue(tag.getString(property.getName())).orElse(property.getAllValues().findFirst().get().value());
	}

	public static <E extends Comparable<E>> void setPropertyInTag(Property<E> property, E value, CompoundTag tag) {
		tag.putString(property.getName(), String.valueOf(value));
	}
}
