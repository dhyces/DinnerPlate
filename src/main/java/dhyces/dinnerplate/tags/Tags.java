package dhyces.dinnerplate.tags;

import dhyces.dinnerplate.DinnerPlate;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;

public class Tags {

    public static final TagKey<Block> PLATES_BLOCK = BlockTags.create(DinnerPlate.modLoc("plates"));
    public static final TagKey<Item> PLATES_ITEM = ItemTags.create(DinnerPlate.modLoc("plates"));
    public static final TagKey<Item> COOKWARE_BLACKLIST = ItemTags.create(DinnerPlate.modLoc("cookware_blacklist"));
    public static final TagKey<Fluid> SOUP = FluidTags.create(DinnerPlate.modLoc("soup"));

}
