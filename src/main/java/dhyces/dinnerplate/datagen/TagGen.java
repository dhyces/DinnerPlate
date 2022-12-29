package dhyces.dinnerplate.datagen;

import dhyces.dinnerplate.registry.BlockRegistry;
import dhyces.dinnerplate.registry.FluidRegistry;
import dhyces.dinnerplate.registry.ItemRegistry;
import dhyces.dinnerplate.tags.Tags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.FluidTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class TagGen {

    public static class BlockTag extends BlockTagsProvider {

        public BlockTag(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, String modId, @Nullable ExistingFileHelper existingFileHelper) {
            super(output, lookupProvider, modId, existingFileHelper);
        }

        @Override
        protected void addTags(HolderLookup.Provider pProvider) {
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

        public ItemTag(PackOutput p_255871_, CompletableFuture<HolderLookup.Provider> p_256035_, TagsProvider<Block> p_256467_, String modId, @Nullable ExistingFileHelper existingFileHelper) {
            super(p_255871_, p_256035_, p_256467_, modId, existingFileHelper);
        }

        @Override
        protected void addTags(HolderLookup.Provider pProvider) {
            copy(Tags.PLATES_BLOCK, Tags.PLATES_ITEM);

            tag(Tags.COOKWARE_BLACKLIST).addTag(Tags.PLATES_ITEM)
                    .add(ItemRegistry.MEASURING_CUP_ITEM.get(),
                            ItemRegistry.MIXING_BOWL_ITEM.get());
        }
    }

    public static class FluidTag extends FluidTagsProvider {

        public FluidTag(PackOutput p_255941_, CompletableFuture<HolderLookup.Provider> p_256600_, String modId, @Nullable ExistingFileHelper existingFileHelper) {
            super(p_255941_, p_256600_, modId, existingFileHelper);
        }

        @Override
        protected void addTags(HolderLookup.Provider pProvider) {
            tag(Tags.SOUP).add(FluidRegistry.BEETROOT_SOUP_FLUID.get(), FluidRegistry.BEETROOT_SOUP_FLUID_FLOWING.get(),
                    FluidRegistry.MUSHROOM_STEW_FLUID.get(), FluidRegistry.MUSHROOM_STEW_FLUID_FLOWING.get(),
                    FluidRegistry.RABBIT_STEW_FLUID.get(), FluidRegistry.RABBIT_STEW_FLUID_FLOWING.get());
        }
    }
}
