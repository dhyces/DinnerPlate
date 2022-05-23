package dhyces.dinnerplate.inventory.api;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;

public interface IInventory extends INBTSerializable<CompoundTag> {
    void setOnChanged(Runnable methodToCall);
}
