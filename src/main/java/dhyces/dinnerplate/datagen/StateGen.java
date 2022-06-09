package dhyces.dinnerplate.datagen;

import dhyces.dinnerplate.block.MixingBowlBlock;
import dhyces.dinnerplate.block.PlateBlock;
import dhyces.dinnerplate.registry.BlockRegistry;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;

public class StateGen extends BlockStateProvider {

    public StateGen(DataGenerator gen, String modid, ExistingFileHelper exFileHelper) {
        super(gen, modid, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        horizontalBlock(BlockRegistry.MEASURING_CUP_BLOCK.get(), models().getExistingFile(modLoc("measuring_cup")));
        getVariantBuilder(BlockRegistry.MIXING_BOWL_BLOCK.get()).forAllStates(blockState -> {
            var mixPos = blockState.getValue(MixingBowlBlock.MIX_POSITION);
            var model = models().getExistingFile(modLoc("mixing_bowl_" + mixPos));
            return ConfiguredModel.builder()
                    .modelFile(model)
                    .build();
        });

        plateBlock(BlockRegistry.WHITE_PLATE_BLOCK.get());
        plateBlock(BlockRegistry.ORANGE_PLATE_BLOCK.get());
        plateBlock(BlockRegistry.MAGENTA_PLATE_BLOCK.get());
        plateBlock(BlockRegistry.LIGHT_BLUE_PLATE_BLOCK.get());
        plateBlock(BlockRegistry.YELLOW_PLATE_BLOCK.get());
        plateBlock(BlockRegistry.LIME_PLATE_BLOCK.get());
        plateBlock(BlockRegistry.PINK_PLATE_BLOCK.get());
        plateBlock(BlockRegistry.GRAY_PLATE_BLOCK.get());
        plateBlock(BlockRegistry.LIGHT_GRAY_PLATE_BLOCK.get());
        plateBlock(BlockRegistry.CYAN_PLATE_BLOCK.get());
        plateBlock(BlockRegistry.PURPLE_PLATE_BLOCK.get());
        plateBlock(BlockRegistry.BLUE_PLATE_BLOCK.get());
        plateBlock(BlockRegistry.BROWN_PLATE_BLOCK.get());
        plateBlock(BlockRegistry.GREEN_PLATE_BLOCK.get());
        plateBlock(BlockRegistry.RED_PLATE_BLOCK.get());
        plateBlock(BlockRegistry.BLACK_PLATE_BLOCK.get());
    }

    private void plateBlock(Block block) {
        getVariantBuilder(block)
                .forAllStates(blockState -> {
                    var plates = blockState.getValue(PlateBlock.PLATES);
                    var plateStr = ForgeRegistries.BLOCKS.getKey(block).getPath();
                    var model = models().getExistingFile(modLoc(plates == 1 ? plateStr : plateStr + "_stacked_" + (plates - 1)));
                    return ConfiguredModel.builder()
                            .modelFile(model)
                            .build();
                });
    }
}
