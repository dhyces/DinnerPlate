package dhyces.dinnerplate.item;

import dhyces.dinnerplate.Constants;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class NBTBlockItem extends BlockItem {

    public NBTBlockItem(Block pBlock, Properties pProperties) {
        super(pBlock, pProperties);
    }

    @Override
    protected boolean placeBlock(BlockPlaceContext pContext, BlockState pState) {
        if (super.placeBlock(pContext, pState)) {
            var tag = new CompoundTag();
            tag.put(Constants.TAG_BLOCK_ENTITY, pContext.getItemInHand().getOrCreateTag());
            pContext.getItemInHand().setTag(tag);
            return true;
        }
        return false;
    }

    @Override
    public InteractionResult place(BlockPlaceContext pContext) {
        var res = super.place(pContext);
        if (res.consumesAction()) {
            pContext.getItemInHand().setTag(pContext.getItemInHand().getOrCreateTagElement(Constants.TAG_BLOCK_ENTITY));
        }
        return res;
    }
}
