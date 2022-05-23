package dhyces.dinnerplate.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockModelBuilder;
import net.minecraftforge.client.model.generators.BlockModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class BlockModelGen extends BlockModelProvider {

    public BlockModelGen(DataGenerator generator, String modid, ExistingFileHelper existingFileHelper) {
        super(generator, modid, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        registerPlates();
        cubeAll("dough", modLoc("block/dough"));
    }

    private void registerPlates() {
        plateBatch(modLoc("block/white_plate"));
        plateBatch(modLoc("block/orange_plate"));
        plateBatch(modLoc("block/magenta_plate"));
        plateBatch(modLoc("block/light_blue_plate"));
        plateBatch(modLoc("block/yellow_plate"));
        plateBatch(modLoc("block/lime_plate"));
        plateBatch(modLoc("block/pink_plate"));
        plateBatch(modLoc("block/gray_plate"));
        plateBatch(modLoc("block/light_gray_plate"));
        plateBatch(modLoc("block/cyan_plate"));
        plateBatch(modLoc("block/purple_plate"));
        plateBatch(modLoc("block/blue_plate"));
        plateBatch(modLoc("block/brown_plate"));
        plateBatch(modLoc("block/green_plate"));
        plateBatch(modLoc("block/red_plate"));
        plateBatch(modLoc("block/black_plate"));

    }

    private void plateBatch(ResourceLocation location) {
        put(plateBlock(location, location, modLoc("block/plate")));
        put(plateBlock(modLoc(location.getPath() + "_stacked_1"), location, modLoc("block/plate_stacked_1")));
        put(plateBlock(modLoc(location.getPath() + "_stacked_2"), location, modLoc("block/plate_stacked_2")));
        put(plateBlock(modLoc(location.getPath() + "_stacked_3"), location, modLoc("block/plate_stacked_3")));
        put(plateBlock(modLoc(location.getPath() + "_stacked_4"), location, modLoc("block/plate_stacked_4")));
        put(plateBlock(modLoc(location.getPath() + "_stacked_5"), location, modLoc("block/plate_stacked_5")));
        put(plateBlock(modLoc(location.getPath() + "_stacked_6"), location, modLoc("block/plate_stacked_6")));
        put(plateBlock(modLoc(location.getPath() + "_stacked_7"), location, modLoc("block/plate_stacked_7")));
    }

    public BlockModelBuilder plateBlock(ResourceLocation location, ResourceLocation texture, ResourceLocation parent) {
        return withExistingParent(location.toString(), parent)
                .texture("0", texture)
                .texture("particle", texture);
    }

    public void put(BlockModelBuilder builder) {
        generatedModels.put(builder.getLocation(), builder);
    }
}
