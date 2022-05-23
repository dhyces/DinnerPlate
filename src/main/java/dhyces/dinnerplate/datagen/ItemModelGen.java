package dhyces.dinnerplate.datagen;

import dhyces.dinnerplate.DinnerPlate;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ItemModelGen extends ItemModelProvider {

    private final ModelFile BUILT_IN_GENERATED;

    public ItemModelGen(DataGenerator generator, String modid, ExistingFileHelper existingFileHelper) {
        super(generator, modid, existingFileHelper);
        BUILT_IN_GENERATED = getExistingFile(mcLoc("item/generated"));
    }

    @Override
    protected void registerModels() {
        registerVanillaBitten();
        registerModdedBitten();
        put(singleTexture("item/wheat_flour_bucket", mcLoc("item/generated"), "layer0", modLoc("item/wheat_flour_bucket")));
    }

    private void registerVanillaBitten() {
        put(threeBiteMocked(bittenRL("cooked_beef")));
        put(threeBiteMocked(bittenRL("baked_potato")));
        put(threeBiteMocked(bittenRL("apple")));
        put(threeBiteMocked(bittenRL("carrot")));
        put(threeBiteMocked(bittenRL("cookie")));
        put(threeBiteMocked(bittenRL("bread")));
        put(threeBiteMocked(bittenRL("pumpkin_pie")));
        put(threeBiteMocked(bittenRL("honey_bottle")));
        put(threeBiteMocked(bittenRL("beef")));
        put(threeBiteMocked(bittenRL("beetroot_soup")));
        put(threeBiteMocked(bittenRL("beetroot")));
        put(threeBiteMocked(bittenRL("chicken")));
        put(threeBiteMocked(bittenRL("potato")));
        put(threeBiteMocked(bittenRL("melon_slice")));
        put(threeBiteMocked(bittenRL("glow_berries")));
        put(threeBiteMocked(bittenRL("sweet_berries")));
        put(threeBiteMocked(bittenRL("mushroom_stew")));
        put(threeBiteMocked(bittenRL("suspicious_stew")));
        put(threeBiteMocked(bittenRL("golden_carrot")));
        put(threeBiteMocked(bittenRL("golden_apple")));
        put(threeBiteMocked(bittenRL("mutton")));
        put(threeBiteMocked(bittenRL("cooked_mutton")));
        put(threeBiteMocked(bittenRL("cod")));
        put(threeBiteMocked(bittenRL("salmon")));
        put(threeBiteMocked(bittenRL("tropical_fish")));
        put(threeBiteMocked(bittenRL("cooked_cod")));
        put(threeBiteMocked(bittenRL("cooked_salmon")));
        put(threeBiteMocked(bittenRL("cooked_porkchop")));
        put(threeBiteMocked(bittenRL("porkchop")));

        put(withExistingParent("item/bitten/dried_kelp_bitten", "dried_kelp"));
    }

    public void registerModdedBitten() {
        put(withExistingParent("item/bitten/mock_food_bitten", "air"));

        put(threeBite(modLoc("item/dough"), modLoc("bitten/dough_bitten")));
    }

    public void put(ItemModelBuilder builder) {
        generatedModels.put(builder.getLocation(), builder);
    }

    public ResourceLocation bittenRL(String item) {
        return modLoc("bitten/" + item + "_bitten");
    }

    public ItemModelBuilder threeBiteMocked(ResourceLocation location) {
        var builder = getBuilder(location.getNamespace() + ":item/" + location.getPath());
        for (float f = 0, i = 0; f < 1.0f; f += 0.5, i += 1) {
            builder.override()
                    .predicate(new ResourceLocation(DinnerPlate.MODID, "bites"), f)
                    .model(predicateBittenModel(location, (int) i))
                    .end();
        }
        return builder;
    }

    public ItemModelBuilder threeBite(ResourceLocation baseLocation, ResourceLocation predicateLocation) {
        return bittenModel(baseLocation, predicateLocation, 0.3F, 0.3F, 2);
    }

    public ItemModelBuilder bittenModel(ResourceLocation baseLocation, ResourceLocation predicateLocation, float predicateStart, float predicateStep, int repeat) {
        var builder = getBuilder(baseLocation.toString());
        builder.parent(BUILT_IN_GENERATED).texture("layer0", baseLocation);
        for (float f = predicateStart, i = 0; f < 1.0f && i < repeat; f += predicateStep, i += 1) {
            builder.override()
                    .predicate(new ResourceLocation(DinnerPlate.MODID, "bites"), f)
                    .model(predicateBittenModel(predicateLocation, (int) i))
                    .end();
        }
        return builder;
    }

    public ItemModelBuilder predicateBittenModel(ResourceLocation location, int biteCount) {
        return singleTexture("item/" + location.getPath() + "_" + biteCount,
                BUILT_IN_GENERATED.getLocation(),
                "layer0",
                modLoc("item/" + location.getPath() + "_" + biteCount));
    }
}
