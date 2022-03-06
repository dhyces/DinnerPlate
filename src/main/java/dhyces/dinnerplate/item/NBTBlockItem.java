package dhyces.dinnerplate.item;

import dhyces.dinnerplate.Constants;
import dhyces.dinnerplate.util.api.INBTMapper;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class NBTBlockItem extends BlockItem {

	public NBTBlockItem(Block pBlock, Properties pProperties) {
		super(pBlock, pProperties);
	}

	@Override
	protected boolean placeBlock(BlockPlaceContext pContext, BlockState pState) {
		if (super.placeBlock(pContext, pState)) {
			pContext.getItemInHand().setTag(INBTMapper.WRAP_BLOCK_ENTITY.apply(pContext.getItemInHand().getOrCreateTag()));
			return true;
		}
		return false;
	}
	
	@Override
	public InteractionResult useOn(UseOnContext pContext) {
		var ret = super.useOn(pContext);
		pContext.getItemInHand().setTag(pContext.getItemInHand().getOrCreateTagElement(Constants.TAG_BLOCK_ENTITY));
		return ret;
	}
}