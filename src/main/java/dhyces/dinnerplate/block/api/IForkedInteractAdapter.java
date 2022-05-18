package dhyces.dinnerplate.block.api;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public interface IForkedInteractAdapter<T extends BlockEntity> extends IForkedInteract<T> {

    @Override
    default InteractionResult leftClick(BlockState state, T blockEntity, Level level, BlockPos pos, Player player,
                                        boolean isClient) {
        return InteractionResult.PASS;
    }

    @Override
    default InteractionResult shiftLeftClick(BlockState state, T blockEntity, Level level, BlockPos pos, Player player,
                                             boolean isClient) {
        return InteractionResult.PASS;
    }

    @Override
    default InteractionResult rightClick(BlockState state, T blockEntity, Level level, BlockPos pos, Player player,
                                         InteractionHand hand, BlockHitResult res, boolean isClient) {
        return InteractionResult.PASS;
    }

    @Override
    default InteractionResult shiftRightClick(BlockState state, T blockEntity, Level level, BlockPos pos, Player player,
                                              InteractionHand hand, BlockHitResult res, boolean isClient) {
        return InteractionResult.PASS;
    }

}
