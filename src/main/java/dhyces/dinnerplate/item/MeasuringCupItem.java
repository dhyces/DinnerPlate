package dhyces.dinnerplate.item;

import java.util.Optional;

import dhyces.dinnerplate.block.api.IForkedInteractAdapter;
import dhyces.dinnerplate.capability.fluid.MeasuredFluidCapability;
import dhyces.dinnerplate.registry.BlockRegistry;
import dhyces.dinnerplate.render.item.MeasuringCupItemRenderer;
import dhyces.dinnerplate.util.FluidHelper;
import dhyces.dinnerplate.util.LoosePair;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;

public class MeasuringCupItem extends RenderableNBTBlockItem implements IForkedInteractAdapter<BlockEntity> {

	public MeasuringCupItem(Properties pProperties) {
		super(new MeasuringCupItemRenderer(), BlockRegistry.MEASURING_CUP_BLOCK.get(), pProperties);
	}
	
	@Override
	public InteractionResult rightClick(BlockState state, BlockEntity blockEntity, Level level, BlockPos pos,
			Player player, InteractionHand hand, BlockHitResult res, boolean isClient) {
		var item = player.getItemInHand(hand);
		var caps = getCaps(blockEntity, item);
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
		return IForkedInteractAdapter.super.rightClick(state, blockEntity, level, pos, player, hand, res, isClient);
	}
	
	@Override
	public InteractionResult shiftRightClick(BlockState state, BlockEntity blockEntity, Level level, BlockPos pos,
			Player player, InteractionHand hand, BlockHitResult res, boolean isClient) {
		var item = player.getItemInHand(hand);
		var caps = getCaps(blockEntity, item);
		if (caps.isPresent()) {
			var blockHandler = caps.get().first;
			var itemHandler = caps.get().second;
			var filled = FluidHelper.fill(itemHandler, blockHandler);
			if (filled > 0) {
				if (!itemHandler.getContainer().sameItem(item)) {
					player.setItemInHand(hand, itemHandler.getContainer());
				}
				return InteractionResult.sidedSuccess(isClient);
			}
		}
		return IForkedInteractAdapter.super.shiftRightClick(state, blockEntity, level, pos, player, hand, res, isClient);
	}
	
	@Override
	public InteractionResult useOn(UseOnContext pContext) {
		var player = pContext.getPlayer();
		var level = pContext.getLevel();
		var blockpos = pContext.getClickedPos();
		var blockstate = level.getBlockState(blockpos);
		var blockentity = level.getBlockEntity(blockpos);
		if (blockentity != null) {
			if (player.isShiftKeyDown())
				return shiftRightClick(blockstate, blockentity, level, blockpos, player, pContext.getHand(), getHitResFromContext(pContext), level.isClientSide);
			return rightClick(blockstate, blockentity, level, blockpos, player, pContext.getHand(), getHitResFromContext(pContext), level.isClientSide);
		}
		return super.useOn(pContext);
	}
	
	public BlockHitResult getHitResFromContext(UseOnContext context) {
		return new BlockHitResult(context.getClickLocation(), context.getClickedFace(), context.getClickedPos(), context.isInside());
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
	public ICapabilityProvider initCapabilities(ItemStack stack, CompoundTag nbt) {
		return new MeasuredFluidCapability(stack, 1000, FluidHelper.PILE);
	}
}
