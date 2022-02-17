package dhyces.dinnerplate.block;

import dhyces.dinnerplate.block.api.AbstractDinnerBlock;
import dhyces.dinnerplate.blockentity.MixingBowlBlockEntity;
import dhyces.dinnerplate.blockentity.api.AbstractDinnerBlockEntity;
import dhyces.dinnerplate.util.FluidHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ClipContext.Fluid;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;
import net.minecraftforge.items.ItemHandlerHelper;

public class MixingBowlBlock extends AbstractDinnerBlock<MixingBowlBlockEntity> {
	
	private static final IntegerProperty MIX_POSITION = IntegerProperty.create("mix_pos", 0, 2);

	public MixingBowlBlock(Properties properties) {
		super(properties);
		registerDefaultState(this.stateDefinition.any().setValue(MIX_POSITION, 0));
	}
	
	@Override
	public InteractionResult rightClick(BlockState state, MixingBowlBlockEntity bEntity, Level level,
			BlockPos pos, Player player, InteractionHand hand, BlockHitResult res, boolean isClient) {
		if (!player.getItemInHand(InteractionHand.OFF_HAND).isEmpty() && hand.equals(InteractionHand.MAIN_HAND))
			return InteractionResult.PASS;
		var preferredStack = getPreferredItemOtherwise(player, InteractionHand.OFF_HAND);
		if (!preferredStack.isEmpty() && !bEntity.isFull()) {
			var fluidContainerOptional = FluidUtil.getFluidHandler(preferredStack);
			if (!fluidContainerOptional.isPresent() || FluidHelper.isEmpty(fluidContainerOptional.resolve().get())) {
				if (!isClient) {
					var itemCopy = ItemHandlerHelper.copyStackWithSize(preferredStack, 1);
					var ret = bEntity.insertItem(itemCopy);
					if (ret.isEmpty() && !player.getAbilities().instabuild)
						preferredStack.shrink(1);
				}
			} else {
				if (fluidContainerOptional.map(c -> c.drain(bEntity.remaining() * 100, FluidAction.SIMULATE)).get().isEmpty())
					return InteractionResult.FAIL;
				if (!isClient) {
					var handler = fluidContainerOptional.resolve().get();
					var fluid = handler.drain(bEntity.remaining() * 100, FluidAction.EXECUTE);
					var ret = bEntity.insertFluid(fluid);
					if (!handler.getContainer().sameItem(preferredStack)) {
						player.setItemInHand(hand, handler.getContainer());
					}
				}
			}
			return InteractionResult.sidedSuccess(isClient);
		} else if (bEntity.hasRecipe()) {
			level.setBlockAndUpdate(pos, state.cycle(MIX_POSITION));
		}
		return super.rightClick(state, bEntity, level, pos, player, hand, res, isClient);
	}
	
	@Override
	public InteractionResult shiftRightClick(BlockState state, MixingBowlBlockEntity bEntity, Level level,
			BlockPos pos, Player player, InteractionHand hand, BlockHitResult res, boolean isClient) {
		if (InteractionHand.OFF_HAND.equals(hand))
			return InteractionResult.FAIL;
		var tag = new CompoundTag();
		bEntity.write(tag);
		System.out.println(tag);
		if (bEntity.hasItem()) {
			var i = bEntity.removeLastItem();
			insertInvOrSpawn(level, pos, 0.25, player.getInventory(), i);
			return InteractionResult.CONSUME;
		}
		return super.shiftRightClick(state, bEntity, level, pos, player, hand, res, isClient);
	}
	
	@Override
	public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
		return Shapes.box(0.125, 0, 0.125, 0.875, 0.4375, 0.875);
	}
	
	@Override
	protected void createBlockStateDefinition(Builder<Block, BlockState> pBuilder) {
		super.createBlockStateDefinition(pBuilder);
		pBuilder.add(MIX_POSITION);
	}
	
	@Override
	public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
		return new MixingBowlBlockEntity(pPos, pState);
	}

}
