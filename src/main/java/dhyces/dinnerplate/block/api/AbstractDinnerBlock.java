package dhyces.dinnerplate.block.api;

import dhyces.dinnerplate.blockentity.api.AbstractDinnerBlockEntity;
import dhyces.dinnerplate.util.InventoryHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public abstract class AbstractDinnerBlock<T extends AbstractDinnerBlockEntity> extends BaseEntityBlock implements IForkedInteract<T> {

	protected AbstractDinnerBlock(Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResult leftClick(BlockState state, T bEntity, Level level, BlockPos pos, Player player,
											boolean isClient) {
		return InteractionResult.PASS;
	}

	@Override
	public InteractionResult shiftLeftClick(BlockState state, T bEntity, Level level, BlockPos pos,
												Player player, boolean isClient) {

		if (!isClient) {
			var stack = getDrops(state, (ServerLevel)level, pos, bEntity).get(0);
			insertInvOrSpawn(level, pos, 0, player.getInventory(), stack);
			level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
		}
		level.playSound(player, pos, getSoundType(state, level, pos, player).getPlaceSound(), SoundSource.BLOCKS, 1.0F, 1.25F);
		return InteractionResult.SUCCESS;
	}

	@Override
	public InteractionResult rightClick(BlockState state, T bEntity, Level level, BlockPos pos,
											Player player, InteractionHand hand, BlockHitResult res, boolean isClient) {
		return InteractionResult.PASS;
	}

	@Override
	public InteractionResult shiftRightClick(BlockState state, T bEntity, Level level, BlockPos pos,
												Player player, InteractionHand hand, BlockHitResult res, boolean isClient) {
		return InteractionResult.PASS;
	}

	//TODO: either refactor this or remove it if it isn't useful
//	public void insertInvNewOrSpawn(Level level, BlockPos pos, double yModifier, Inventory inv, ItemStack stack) {
//		if (!InventoryHelper.insertItemNewSlot(inv, stack)) {
//			spawnCenterBlock(level, pos, yModifier, stack);
//			return;
//		}
//		PlayerHelper.staticEquipEventAndSound(inv.player, stack);
//	}

	public void insertInvOrSpawn(Level level, BlockPos pos, double yModifier, Inventory inv, ItemStack stack) {
		if (stack.isEmpty()) return;
		var preferredSlot = InventoryHelper.getPreferredSlot(inv, stack);
		if (preferredSlot == -1) {
			spawnCenterBlock(level, pos, yModifier, stack);
			return;
		}
		if (!(inv.findSlotMatchingItem(stack) != -1 && inv.player.getAbilities().instabuild))
			inv.add(preferredSlot, stack);
	}

	/** @param yModifier This will add to the y coordinate after it's been moved to the center*/
	public void spawnCenterBlock(Level level, BlockPos pos, double yModifier, ItemStack stack) {
		var itemEntity = new ItemEntity(level, pos.getX() + .5, pos.getY() + .5 + yModifier, pos.getZ() + .5, stack);
		level.addFreshEntity(itemEntity);
	}

	public ItemStack getPreferredItemOtherwise(Player player, InteractionHand preferredHand) {
		var preferred = player.getItemInHand(preferredHand);
		var otherHand = preferredHand == InteractionHand.MAIN_HAND ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND;
		return !preferred.isEmpty() ? preferred : player.getItemInHand(otherHand);
	}

	@Override
	public void attack(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer) {
		if (pPlayer.getMainHandItem().is(Items.DEBUG_STICK))
			return;
		if (pLevel.getBlockEntity(pPos) instanceof AbstractDinnerBlockEntity bEntity) {
			if (pPlayer.isCrouching()) {
				shiftLeftClick(pState, (T)bEntity, pLevel, pPos, pPlayer, pLevel.isClientSide);
				return;
			}
			leftClick(pState, (T)bEntity,  pLevel, pPos, pPlayer, pLevel.isClientSide);
		}
	}

	@Override
	public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand,
			BlockHitResult pHit) {
		if (pPlayer.getMainHandItem().is(Items.DEBUG_STICK))
			return InteractionResult.PASS;
//		if (InteractionHand.OFF_HAND.equals(pHand))
//			return InteractionResult.PASS;
		if (pLevel.getBlockEntity(pPos) instanceof AbstractDinnerBlockEntity bEntity) {
			if (pPlayer.isCrouching())
				return shiftRightClick(pState, (T)bEntity, pLevel, pPos, pPlayer, pHand, pHit, pLevel.isClientSide);
			return rightClick(pState, (T)bEntity, pLevel, pPos, pPlayer, pHand, pHit, pLevel.isClientSide);
		}
		return InteractionResult.PASS;
	}

	@Override
	public RenderShape getRenderShape(BlockState pState) {
		return RenderShape.MODEL;
	}
}
