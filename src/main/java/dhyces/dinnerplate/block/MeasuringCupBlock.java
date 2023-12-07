package dhyces.dinnerplate.block;

import com.mojang.datafixers.util.Pair;
import dhyces.dinnerplate.block.api.AbstractDinnerBlock;
import dhyces.dinnerplate.blockentity.MeasuringCupBlockEntity;
import dhyces.dinnerplate.util.FluidHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;

import java.util.Optional;

public class MeasuringCupBlock extends AbstractDinnerBlock<MeasuringCupBlockEntity> {

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    public MeasuringCupBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @Override
    public InteractionResult rightClick(BlockState state, MeasuringCupBlockEntity bEntity, Level level, BlockPos pos,
                                        Player player, InteractionHand hand, BlockHitResult res, boolean isClient) {
        var item = player.getItemInHand(hand);
        var caps = getCaps(bEntity, item);
        if (caps.isPresent()) {
            var blockHandler = caps.get().getFirst();
            var itemHandler = caps.get().getSecond();
            var filled = FluidHelper.fill(blockHandler, itemHandler, FluidHelper.clientAction(isClient), FluidHelper.clientAction(player.getAbilities().instabuild));
            if (filled > 0) {
                if (!isClient) {
                    if (!itemHandler.getContainer().sameItem(item)) {
                        player.setItemInHand(hand, itemHandler.getContainer());
                    }
                }
                return InteractionResult.sidedSuccess(isClient);
            }
        }
        return super.rightClick(state, bEntity, level, pos, player, hand, res, isClient);
    }

    @Override
    public InteractionResult shiftRightClick(BlockState state, MeasuringCupBlockEntity bEntity, Level level,
                                             BlockPos pos, Player player, InteractionHand hand, BlockHitResult res, boolean isClient) {
        var item = player.getItemInHand(hand);
        var caps = getCaps(bEntity, item);
        if (!item.isEmpty() && caps.isPresent()) {
            var blockHandler = caps.get().getFirst();
            var itemHandler = caps.get().getSecond();
            var filled = FluidHelper.fill(itemHandler, blockHandler, FluidHelper.clientAction(isClient));
            if (filled > 0) {
                if (!isClient) {
                    if (!itemHandler.getContainer().sameItem(item)) {
                        player.setItemInHand(hand, itemHandler.getContainer());
                    }
                }
                return InteractionResult.CONSUME;
            }
        }
        return super.shiftRightClick(state, bEntity, level, pos, player, hand, res, isClient);
    }

    /**
     * Retrieves the block fluid handler and the item fluid handler, if they exist
     */
    private Optional<Pair<IFluidHandler, IFluidHandlerItem>> getCaps(BlockEntity blockEntity, ItemStack itemStack) {
        var blockCap = blockEntity.getCapability(ForgeCapabilities.FLUID_HANDLER);
        if (blockCap.isPresent()) {
            var itemCap = itemStack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM);
            if (itemCap.isPresent()) {
                return Optional.of(Pair.of(blockCap.resolve().get(), itemCap.resolve().get()));
            }
        }
        return Optional.empty();
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return Shapes.box(0.3125, 0, 0.3125, 0.6875, 0.4375, 0.6875);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return defaultBlockState().setValue(FACING, pContext.getHorizontalDirection().getCounterClockWise());
    }

    @Override
    public boolean propagatesSkylightDown(BlockState pState, BlockGetter pLevel, BlockPos pPos) {
        return true;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FACING);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new MeasuringCupBlockEntity(pPos, pState);
    }

}
