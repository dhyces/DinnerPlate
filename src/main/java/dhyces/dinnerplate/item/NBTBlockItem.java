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
            var existingTag = pContext.getItemInHand().getOrCreateTag();
            if (existingTag.contains(Constants.BLOCK_STATE_TAG)) {
                tag.put(Constants.BLOCK_STATE_TAG, existingTag.getCompound(Constants.BLOCK_STATE_TAG));
                existingTag.remove(Constants.BLOCK_STATE_TAG);
            }
            tag.put(Constants.BLOCK_ENTITY_TAG, existingTag);
            pContext.getItemInHand().setTag(tag);
            return true;
        }
        return false;
    }

    @Override
    public InteractionResult place(BlockPlaceContext pContext) {
        var res = super.place(pContext);
        if (res.consumesAction()) {
            var existingTag = pContext.getItemInHand().getOrCreateTag();
            if (existingTag.contains(Constants.BLOCK_ENTITY_TAG)) {
                var tag = existingTag.getCompound(Constants.BLOCK_ENTITY_TAG);
                existingTag.remove(Constants.BLOCK_ENTITY_TAG);
                existingTag.merge(tag);
            }
        }
        return res;
    }
}
