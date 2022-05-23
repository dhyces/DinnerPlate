package dhyces.dinnerplate.datagen;

import dhyces.dinnerplate.registry.BlockRegistry;
import dhyces.dinnerplate.registry.FluidRegistry;
import dhyces.dinnerplate.registry.ItemRegistry;
import dhyces.dinnerplate.tags.Tags;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.tags.FluidTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

public class TagGen {

    public static class BlockTag extends BlockTagsProvider {

        public BlockTag(DataGenerator pGenerator, String modId, @Nullable ExistingFileHelper existingFileHelper) {
            super(pGenerator, modId, existingFileHelper);
        }

        @Override
        protected void addTags() {
            tag(Tags.PLATES_BLOCK)
                    .add(BlockRegistry.WHITE_PLATE_BLOCK.get(),
                            BlockRegistry.ORANGE_PLATE_BLOCK.get(),
                            BlockRegistry.MAGENTA_PLATE_BLOCK.get(),
                            BlockRegistry.LIGHT_BLUE_PLATE_BLOCK.get(),
                            BlockRegistry.YELLOW_PLATE_BLOCK.get(),
                            BlockRegistry.LIME_PLATE_BLOCK.get(),
                            BlockRegistry.PINK_PLATE_BLOCK.get(),
                            BlockRegistry.GRAY_PLATE_BLOCK.get(),
                            BlockRegistry.LIGHT_GRAY_PLATE_BLOCK.get(),
                            BlockRegistry.CYAN_PLATE_BLOCK.get(),
                            BlockRegistry.PURPLE_PLATE_BLOCK.get(),
                            BlockRegistry.BLUE_PLATE_BLOCK.get(),
                            BlockRegistry.BROWN_PLATE_BLOCK.get(),
                            BlockRegistry.GREEN_PLATE_BLOCK.get(),
                            BlockRegistry.RED_PLATE_BLOCK.get(),
                            BlockRegistry.BLACK_PLATE_BLOCK.get());
        }
    }

    public static class ItemTag extends ItemTagsProvider {
        public ItemTag(DataGenerator pGenerator, BlockTagsProvider pBlockTagsProvider, String modId, @Nullable ExistingFileHelper existingFileHelper) {
            super(pGenerator, pBlockTagsProvider, modId, existingFileHelper);
        }

        @Override
        protected void addTags() {
            copy(Tags.PLATES_BLOCK, Tags.PLATES_ITEM);

            tag(Tags.COOKWARE_BLACKLIST).addTag(Tags.PLATES_ITEM)
                    .add(ItemRegistry.MEASURING_CUP_ITEM.get(),
                            ItemRegistry.MIXING_BOWL_ITEM.get());
        }
    }

    public static class FluidTag extends FluidTagsProvider {

        public FluidTag(DataGenerator pGenerator, String modId, @Nullable ExistingFileHelper existingFileHelper) {
            super(pGenerator, modId, existingFileHelper);
        }

        @Override
        protected void addTags() {
            tag(Tags.SOUP).add(FluidRegistry.BEETROOT_SOUP_FLUID.get(), FluidRegistry.BEETROOT_SOUP_FLUID_FLOWING.get(),
                    FluidRegistry.MUSHROOM_STEW_FLUID.get(), FluidRegistry.MUSHROOM_STEW_FLUID_FLOWING.get(),
                    FluidRegistry.RABBIT_STEW_FLUID.get(), FluidRegistry.RABBIT_STEW_FLUID_FLOWING.get());
            tag(Tags.FLOUR).add(FluidRegistry.WHEAT_FLOUR_FLUID.get(), FluidRegistry.WHEAT_FLOUR_FLUID_FLOWING.get());
        }
    }
}
