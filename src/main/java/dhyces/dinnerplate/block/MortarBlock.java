package dhyces.dinnerplate.block;

import dhyces.dinnerplate.block.api.AbstractDinnerBlock;
import dhyces.dinnerplate.blockentity.MortarBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class MortarBlock extends AbstractDinnerBlock<MortarBlockEntity> {

    public MortarBlock(Properties properties) {
        super(properties);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return null;
    }
}
