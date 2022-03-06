package dhyces.dinnerplate.block;

import java.util.List;
import java.util.Optional;

import dhyces.dinnerplate.Constants;
import dhyces.dinnerplate.block.api.AbstractDinnerBlock;
import dhyces.dinnerplate.blockentity.MeasuringCupBlockEntity;
import dhyces.dinnerplate.registry.ItemRegistry;
import dhyces.dinnerplate.util.FluidHelper;
import dhyces.dinnerplate.util.LoosePair;
import dhyces.dinnerplate.util.api.INBTMapper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext.Builder;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;

public class MeasuringCupBlock extends AbstractDinnerBlock<MeasuringCupBlockEntity> {

	public MeasuringCupBlock(Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResult rightClick(BlockState state, MeasuringCupBlockEntity bEntity, Level level, BlockPos pos,
			Player player, InteractionHand hand, BlockHitResult res, boolean isClient) {
		var item = player.getItemInHand(hand);
		var caps = getCaps(bEntity, item);
		if (caps.isPresent()) {
			var blockHandler = caps.get().first;
			var itemHandler = caps.get().second;
			var filled = FluidHelper.fill(blockHandler, itemHandler);
			if (filled > 0) {
				if (!itemHandler.getContainer().sameItem(item)) {
					player.setItemInHand(hand, itemHandler.getContainer());
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
		if (caps.isPresent()) {
			var blockHandler = caps.get().first;
			var itemHandler = caps.get().second;
			// TODO: this doesn't work for things like bowls, unfortunately, thus I must find a way to do that. I have added a capability on bowls.
			var filled = FluidHelper.fill(itemHandler, blockHandler);
			if (filled > 0) {
				if (!itemHandler.getContainer().sameItem(item)) {
					player.setItemInHand(hand, itemHandler.getContainer());
				}
				return InteractionResult.sidedSuccess(isClient);
			}
		}
		return super.shiftRightClick(state, bEntity, level, pos, player, hand, res, isClient);
	}

	private Optional<LoosePair<IFluidHandler, IFluidHandlerItem>> getCaps(BlockEntity blockEntity, ItemStack itemStack) {
		var blockCap = blockEntity.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY);
		if (blockCap.isPresent()) {
			var itemCap = itemStack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY);
			if (itemCap.isPresent()) {
				return Optional.of(LoosePair.of(blockCap.resolve().get(), itemCap.resolve().get()));
			}
		}
		return Optional.empty();
	}
	
	@Override
	public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
		return Shapes.box(0.3125, 0, 0.3125, 0.6875, 0.4375, 0.6875);
	}

	@Override
	public boolean propagatesSkylightDown(BlockState pState, BlockGetter pLevel, BlockPos pPos) {
		return true;
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
		return new MeasuringCupBlockEntity(pPos, pState);
	}

}
