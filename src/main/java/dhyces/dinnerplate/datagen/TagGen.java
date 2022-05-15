package dhyces.dinnerplate.datagen;

import dhyces.dinnerplate.DinnerPlate;
import dhyces.dinnerplate.registry.ItemRegistry;
import dhyces.dinnerplate.tags.Tags;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

public class TagGen extends ItemTagsProvider {

    public TagGen(DataGenerator pGenerator, BlockTagsProvider pBlockTagsProvider, String modId, @Nullable ExistingFileHelper existingFileHelper) {
        super(pGenerator, pBlockTagsProvider, modId, existingFileHelper);
    }

    @Override
    protected void addTags() {
        tag(Tags.PLATES).add(ItemRegistry.WHITE_PLATE_ITEM.get(),
                                ItemRegistry.ORANGE_PLATE_ITEM.get(),
                                ItemRegistry.MAGENTA_PLATE_ITEM.get(),
                                ItemRegistry.LIGHT_BLUE_PLATE_ITEM.get(),
                                ItemRegistry.YELLOW_PLATE_ITEM.get(),
                                ItemRegistry.LIME_PLATE_ITEM.get(),
                                ItemRegistry.PINK_PLATE_ITEM.get(),
                                ItemRegistry.GRAY_PLATE_ITEM.get(),
                                ItemRegistry.LIGHT_GRAY_PLATE_ITEM.get(),
                                ItemRegistry.CYAN_PLATE_ITEM.get(),
                                ItemRegistry.PURPLE_PLATE_ITEM.get(),
                                ItemRegistry.BLUE_PLATE_ITEM.get(),
                                ItemRegistry.BROWN_PLATE_ITEM.get(),
                                ItemRegistry.GREEN_PLATE_ITEM.get(),
                                ItemRegistry.RED_PLATE_ITEM.get(),
                                ItemRegistry.BLACK_PLATE_ITEM.get());

        tag(Tags.COOK_WARE_BLACKLIST).addTag(Tags.PLATES)
                .add(ItemRegistry.MEASURING_CUP_ITEM.get(),
                        ItemRegistry.MIXING_BOWL_ITEM.get());

    }

}
