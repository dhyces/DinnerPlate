package dhyces.dinnerplate.util;

import dhyces.dinnerplate.Constants;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.properties.Property;

import java.util.Optional;

public class BlockHelper {

	public static Optional<CompoundTag> getBlockStateTag(ItemStack stack) {
		return Optional.ofNullable(stack.getTagElement(Constants.TAG_BLOCK_STATE));
	}

	public static Optional<CompoundTag> getBlockEntityTag(ItemStack stack) {
		return Optional.ofNullable(stack.getTagElement(Constants.TAG_BLOCK_ENTITY));
	}

	public static <E extends Comparable<E>> E getPropertyFromTag(Property<E> property, CompoundTag tag) {
		return property.getValue(tag.getString(property.getName())).orElse(property.getAllValues().findFirst().get().value());
	}

	public static <E extends Comparable<E>> void setPropertyInTag(Property<E> property, E value, CompoundTag tag) {
		tag.putString(property.getName(), String.valueOf(value));
	}
}
