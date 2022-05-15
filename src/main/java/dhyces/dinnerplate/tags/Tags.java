package dhyces.dinnerplate.tags;

import dhyces.dinnerplate.DinnerPlate;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class Tags {

    public static final TagKey<Item> PLATES = ItemTags.create(DinnerPlate.modLoc("plates"));
    public static final TagKey<Item> COOK_WARE_BLACKLIST = ItemTags.create(DinnerPlate.modLoc("cook_ware_blacklist"));

}
