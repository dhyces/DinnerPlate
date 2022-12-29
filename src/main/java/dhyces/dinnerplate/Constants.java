package dhyces.dinnerplate;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;

public class Constants {

    // General Tags
    public static final String SINGLE_ITEM_TAG = "Item";
    public static final String SINGLE_FLUID_TAG = "Fluid";
    public static final String ITEMS_TAG = "Items";
    public static final String FLUIDS_TAG = "Fluids";
    public static final String BLOCK_STATE_TAG = BlockItem.BLOCK_STATE_TAG;
    public static final String BLOCK_ENTITY_TAG = "BlockEntityTag";
    public static final String SLOT_TAG = "Slot";
    public static final String USED_SIZE_TAG = "UsedSize";

    // Bitten Tags
    public static final String BITE_COUNT_TAG = "Chews";

    // Mock Food Capability Tags
    public static final String FIRST_BITE_TAG = "FirstBite";
    public static final String LAST_BITE_TAG = "LastBite";

    // Bite Tags
    public static final String NUTRITION_TAG = "Nutrition";
    public static final String SATURATION_MOD_TAG = "SaturationMod";
    public static final String EFFECT_CHANCE_TAG = "Chance";
    public static final String EFFECTS_LIST_TAG = "Effects";

    // Mock Food Constants
    public static final int MAX_ITEM_CHEW_COUNT = 2;

    // Mixing Bowl Tags
    public static final String MIX_STATE_TAG = "MixState";

    public static ResourceLocation mcLocation(String str) {
        return new ResourceLocation(str);
    }

    public static ResourceLocation modLocation(String str) {
        return new ResourceLocation(DinnerPlate.MODID, str);
    }
}
