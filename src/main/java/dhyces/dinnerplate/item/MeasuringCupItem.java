package dhyces.dinnerplate.item;

import dhyces.dinnerplate.block.api.IForkedInteractAdapter;
import dhyces.dinnerplate.registry.BlockRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStack;

public class MeasuringCupItem extends BlockItem implements IForkedInteractAdapter<BlockEntity> {

	public MeasuringCupItem(Properties pProperties) {
		super(BlockRegistry.MEASURING_CUP_BLOCK.get(), pProperties);
	}

	@Override
	public InteractionResult rightClick(BlockState state, BlockEntity blockEntity, Level level, BlockPos pos,
			Player player, InteractionHand hand, BlockHitResult res, boolean isClient) {
		var blockCap = blockEntity.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, res.getDirection());
		if (blockCap.isPresent()) {
			var item = player.getItemInHand(hand);
			var itemCap = item.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY);
			if (itemCap.isPresent()) {
				var blockHandler = blockCap.resolve().get();
				var itemHandler = itemCap.resolve().get();
				blockHandler.fill(itemHandler.drain(1000, FluidAction.EXECUTE), FluidAction.EXECUTE);
				return InteractionResult.SUCCESS;
			}
		}
		return InteractionResult.PASS;
	}

	@Override
	public InteractionResult shiftRightClick(BlockState state, BlockEntity blockEntity, Level level, BlockPos pos,
			Player player, InteractionHand hand, BlockHitResult res, boolean isClient) {
		return InteractionResult.PASS;
	}
	
	@Override
	public InteractionResult useOn(UseOnContext pContext) {
		var level = pContext.getLevel();
		var pos = pContext.getClickedPos();
		var bEntity = level.getBlockEntity(pos);
		if (bEntity != null) {
			var hitRes = fromContext(pContext);
			InteractionResult result;
			if (pContext.getPlayer().isShiftKeyDown()) {
				result = shiftRightClick(level.getBlockState(pos), bEntity, level, pos, pContext.getPlayer(), pContext.getHand(), hitRes, level.isClientSide);
			} else {
				result = rightClick(level.getBlockState(pos), bEntity, level, pos, pContext.getPlayer(), pContext.getHand(), hitRes, level.isClientSide);
			}
			if (!result.equals(InteractionResult.PASS))
				return result;
		}
		return super.useOn(pContext);
	}
	
	private BlockHitResult fromContext(UseOnContext context) {
		return new BlockHitResult(context.getClickLocation(), context.getClickedFace(), context.getClickedPos(), context.isInside());
	}
	
	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, CompoundTag nbt) {
		return new FluidHandlerItemStack(stack, 1000);
	}
}
