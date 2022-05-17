package dhyces.dinnerplate.block;

import dhyces.dinnerplate.block.api.AbstractDinnerBlock;
import dhyces.dinnerplate.blockentity.MixingBowlBlockEntity;
import dhyces.dinnerplate.tags.Tags;
import dhyces.dinnerplate.util.FluidHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.ItemHandlerHelper;

public class MixingBowlBlock extends AbstractDinnerBlock<MixingBowlBlockEntity> {

	public static final IntegerProperty MIX_POSITION = IntegerProperty.create("mix_pos", 0, 2);

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
			var lazyItemFluidHandler = FluidUtil.getFluidHandler(preferredStack);
			if (lazyItemFluidHandler.isPresent()) {
				var itemHandler = lazyItemFluidHandler.resolve().get();
				var lazyBlockHandler = bEntity.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY);
				if (lazyBlockHandler.isPresent()) {
					var blockHandler = lazyBlockHandler.resolve().get();
					var fill = FluidHelper.fill(blockHandler, itemHandler, FluidHelper.clientAction(isClient));
					if (!isClient) {
						if (!itemHandler.getContainer().sameItem(preferredStack)) {
							player.setItemInHand(hand, itemHandler.getContainer());
						}
					}
					if (fill > 0)
						return InteractionResult.sidedSuccess(isClient);
				}
			}
			if (!preferredStack.is(Tags.COOKWARE_BLACKLIST)) {
				if (!isClient) {
					var itemCopy = ItemHandlerHelper.copyStackWithSize(preferredStack, 1);
					var ret = bEntity.insertItem(itemCopy);
					if (ret.isEmpty() && !player.getAbilities().instabuild)
						preferredStack.shrink(1);
				}
				return InteractionResult.sidedSuccess(isClient);
			}
		}
		if (bEntity.hasRecipe()) {
			level.setBlockAndUpdate(pos, state.cycle(MIX_POSITION));
			return InteractionResult.sidedSuccess(isClient);
		}
		return super.rightClick(state, bEntity, level, pos, player, hand, res, isClient);
	}

	@Override
	public InteractionResult shiftRightClick(BlockState state, MixingBowlBlockEntity bEntity, Level level,
			BlockPos pos, Player player, InteractionHand hand, BlockHitResult res, boolean isClient) {
		if (InteractionHand.OFF_HAND.equals(hand))
			return InteractionResult.FAIL;
		var playerHandCap = player.getItemInHand(hand).getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY);
		if (playerHandCap.isPresent()) {
			var bEntityCap = bEntity.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY);
			var fill = FluidHelper.fill(playerHandCap.resolve().get(), bEntityCap.resolve().get(), FluidHelper.clientAction(isClient));
			if (fill > 0)
				return InteractionResult.sidedSuccess(isClient);
		}
		if (bEntity.hasItem()) {
			var i = bEntity.removeLastItem();
			insertInvOrSpawn(level, pos, 0.25, player.getInventory(), i);
			if (isClient)
				Minecraft.getInstance().getItemInHandRenderer().itemUsed(hand);
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
