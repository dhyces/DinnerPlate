package dhyces.dinnerplate.util.api;

import dhyces.dinnerplate.Constants;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;

@FunctionalInterface
public interface INBTMapper<NBT extends Tag, MAPPED extends Tag> {
	public static final INBTMapper<CompoundTag, CompoundTag> UNWRAP_BLOCK_ENTITY = c -> {
		var nested = c.getCompound(Constants.TAG_BLOCK_ENTITY);
		return nested;
	};
	public static final INBTMapper<CompoundTag, CompoundTag> WRAP_BLOCK_ENTITY = c -> {
		var n = new CompoundTag();
		n.put(Constants.TAG_BLOCK_ENTITY, c);
		return n;
	};

	public MAPPED apply(NBT givenNBT);
}
