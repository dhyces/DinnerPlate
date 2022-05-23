package dhyces.dinnerplate.blockentity;

import dhyces.dinnerplate.blockentity.api.AbstractInventoryBlockEntity;
import dhyces.dinnerplate.blockentity.api.IWorkstation;
import dhyces.dinnerplate.inventory.MixedInventory;
import dhyces.dinnerplate.registry.BEntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.BlockState;

public class MortarBlockEntity extends AbstractInventoryBlockEntity<MixedInventory> implements IWorkstation {

    public MortarBlockEntity(BlockPos pWorldPosition, BlockState pBlockState) {
        super(BEntityRegistry.MORTAR_ENTITY.get(), pWorldPosition, pBlockState, new MixedInventory(9));
    }

    @Override
    public void write(CompoundTag tag) {

    }

    @Override
    public void read(CompoundTag tag) {

    }

    @Override
    public void craft() {

    }

    @Override
    public boolean hasRecipe() {
        return false;
    }
}
