package dhyces.dinnerplate.blockentity.api;

import com.google.common.collect.ImmutableList;
import dhyces.dinnerplate.inventory.MixedInventory;
import dhyces.dinnerplate.inventory.api.IInventory;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.FluidStack;

public abstract class AbstractInventoryBlockEntity<T extends IInventory> extends AbstractDinnerBlockEntity {

    protected T inventory;

    public AbstractInventoryBlockEntity(BlockEntityType<?> pType, BlockPos pWorldPosition, BlockState pBlockState, T inventory) {
        super(pType, pWorldPosition, pBlockState);
        this.inventory = inventory;
        inventory.setOnChanged(() -> this.setChanged());
    }

    @Override
    public void write(CompoundTag tag) {
        var inv = inventory.serializeNBT();
        if (!inv.isEmpty())
            tag.merge(inv);
    }

    @Override
    public void read(CompoundTag tag) {
        inventory.deserializeNBT(tag);
    }

    public T getInventory() {
        return this.inventory;
    }

//    public FlutemStack insertFlutemStack(FlutemStack stack) {
//
//    }
}
