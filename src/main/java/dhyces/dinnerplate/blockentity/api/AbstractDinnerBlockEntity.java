package dhyces.dinnerplate.blockentity.api;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class AbstractDinnerBlockEntity extends SyncedBlockEntity {

    public AbstractDinnerBlockEntity(BlockEntityType<?> pType, BlockPos pWorldPosition, BlockState pBlockState) {
        super(pType, pWorldPosition, pBlockState);
    }

    public ItemStack getAsItemStack() {
        var stack = this.getBlockState().getBlock().asItem().getDefaultInstance().copy();
        saveToItem(stack);
        return stack;
    }

    @Override
    public void setChanged() {
        super.setChanged();
        this.getLevel().sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), 3);
    }


}
