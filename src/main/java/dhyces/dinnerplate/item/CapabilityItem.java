package dhyces.dinnerplate.item;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import javax.annotation.Nullable;
import java.util.function.BiFunction;

public class CapabilityItem extends Item {

    protected final BiFunction<ItemStack, CompoundTag, ICapabilityProvider> cap;

    public CapabilityItem(BiFunction<ItemStack, CompoundTag, ICapabilityProvider> capability, Properties pProperties) {
        super(pProperties);
        this.cap = capability;
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, CompoundTag nbt) {
        return cap.apply(stack, nbt);
    }
}
