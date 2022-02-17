package dhyces.dinnerplate.nbt;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;

/** TODO: if this is to be used, this would be an object that contains the string key and a supplier to the value*/
public interface ITagPart<T extends Tag> {

	/** this method would be used to quickly put the held data into the given compound tag*/
	public void addTo(CompoundTag tag);
	
}
