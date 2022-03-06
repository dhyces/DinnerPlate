package dhyces.dinnerplate;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;

public class Constants {

	// General Tags
	public static final String TAG_SINGLE_ITEM = "Item";
	public static final String TAG_SINGLE_FLUID = "Fluid";
	public static final String TAG_ITEMS = "Items";
	public static final String TAG_FLUIDS = "Fluids";
	public static final String TAG_BLOCK_STATE = BlockItem.BLOCK_STATE_TAG;
	public static final String TAG_BLOCK_ENTITY = "BlockEntityTag";
	public static final String TAG_SLOT = "Slot";
	public static final String TAG_USED_SIZE = "UsedSize";

	// Bitten Tags
	public static final String TAG_BITE_COUNT = "Chews";

	// Mock Food Capability Tags
	public static final String TAG_FIRST_BITE = "FirstBite";
	public static final String TAG_LAST_BITE = "LastBite";

	// Bite Tags
	public static final String TAG_NUTRITION = "Nutrition";
	public static final String TAG_SATURATION_MOD = "SaturationMod";
	public static final String TAG_EFFECT_CHANCE = "Chance";
	public static final String TAG_EFFECTS_LIST = "Effects";

	// Mock Food Constants
	public static final int MAX_ITEM_CHEW_COUNT = 2;

	// Mixing Bowl Tags
	public static final String TAG_MIX_STATE = "MixState";

	public static ResourceLocation mcLocation(String str) {
		return new ResourceLocation(str);
	}

	public static ResourceLocation modLocation(String str) {
		return new ResourceLocation(DinnerPlate.MODID, str);
	}
}
