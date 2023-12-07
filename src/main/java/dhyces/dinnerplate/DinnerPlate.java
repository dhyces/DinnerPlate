package dhyces.dinnerplate;

import dhyces.dinnerplate.client.ClientInit;
import dhyces.dinnerplate.datagen.*;
import dhyces.dinnerplate.registry.*;
import net.minecraft.data.DataProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.Consumer;

@Mod(DinnerPlate.MODID)
public class DinnerPlate {

    public static final String MODID = "dinnerplate";

    public static final Logger LOGGER = LogManager.getLogger(DinnerPlate.class);
    public static CreativeModeTab TAB;

    public DinnerPlate() {
        var bus = FMLJavaModLoadingContext.get().getModEventBus();

        ForgeMod.enableMilkFluid();

        bus.addListener(this::registerCreativeTab);
        if (FMLLoader.getDist().isClient()) {
            ClientInit.register(bus);
        }

        if (FMLLoader.getLaunchHandler().isData())
            bus.addListener(this::dataGenerators);

        registerRegistries(bus);
    }

    public static ResourceLocation modLoc(String path) {
        return new ResourceLocation(MODID, path);
    }

    private void registerRegistries(final IEventBus bus) {
        BlockRegistry.init(bus);
        BEntityRegistry.register(bus);
        ItemRegistry.register(bus);
        FluidTypeRegistry.register(bus);
        FluidRegistry.register(bus);
        SoundRegistry.register(bus);
        RecipeRegistry.register(bus);
    }

    private void registerCreativeTab(final CreativeModeTabEvent.Register event) {
        TAB = event.registerCreativeModeTab(modLoc("main"), builder -> builder
                .icon(() -> ItemRegistry.WHITE_PLATE_ITEM.get().getDefaultInstance().copy())
                .title(Component.translatable("tabs.dinnerplate.main"))
                .displayItems((displayParameters, pOutput) -> {
                    pOutput.accept(ItemRegistry.WHITE_PLATE_ITEM.get());
                    pOutput.accept(ItemRegistry.ORANGE_PLATE_ITEM.get());
                    pOutput.accept(ItemRegistry.MAGENTA_PLATE_ITEM.get());
                    pOutput.accept(ItemRegistry.LIGHT_BLUE_PLATE_ITEM.get());
                    pOutput.accept(ItemRegistry.YELLOW_PLATE_ITEM.get());
                    pOutput.accept(ItemRegistry.LIME_PLATE_ITEM.get());
                    pOutput.accept(ItemRegistry.PINK_PLATE_ITEM.get());
                    pOutput.accept(ItemRegistry.GRAY_PLATE_ITEM.get());
                    pOutput.accept(ItemRegistry.LIGHT_GRAY_PLATE_ITEM.get());
                    pOutput.accept(ItemRegistry.CYAN_PLATE_ITEM.get());
                    pOutput.accept(ItemRegistry.PURPLE_PLATE_ITEM.get());
                    pOutput.accept(ItemRegistry.BLUE_PLATE_ITEM.get());
                    pOutput.accept(ItemRegistry.BROWN_PLATE_ITEM.get());
                    pOutput.accept(ItemRegistry.GREEN_PLATE_ITEM.get());
                    pOutput.accept(ItemRegistry.RED_PLATE_ITEM.get());
                    pOutput.accept(ItemRegistry.BLACK_PLATE_ITEM.get());

                    pOutput.accept(ItemRegistry.MEASURING_CUP_ITEM.get());
                    pOutput.accept(ItemRegistry.MIXING_BOWL_ITEM.get());

                    pOutput.accept(ItemRegistry.MUSHROOM_STEW_BUCKET.get());
                    pOutput.accept(ItemRegistry.BEETROOT_SOUP_BUCKET.get());
                    pOutput.accept(ItemRegistry.RABBIT_STEW_BUCKET.get());

                    pOutput.accept(ItemRegistry.CUT_CARROT_ITEM.get());
                })
        );
    }

    private void dataGenerators(final GatherDataEvent event) {
        var packOutput = event.getGenerator().getPackOutput();
        var fileHelper = event.getExistingFileHelper();
        var lookupProvider = event.getLookupProvider();
        Consumer<DataProvider> addClientProvider = dataProvider -> event.getGenerator().addProvider(event.includeClient(), dataProvider);
        Consumer<DataProvider> addServerProvider = dataProvider -> event.getGenerator().addProvider(event.includeServer(), dataProvider);
        addClientProvider.accept(new BlockModelGen(packOutput, MODID, fileHelper));
        addClientProvider.accept(new ItemModelGen(packOutput, MODID, fileHelper));
        addClientProvider.accept(new StateGen(packOutput, MODID, fileHelper));
        addClientProvider.accept(new LangGen(packOutput, MODID, "en_us"));

        addServerProvider.accept(new BlockLootTableGen.LootTableGen(packOutput));
        var blockGen = new TagGen.BlockTag(packOutput, lookupProvider, MODID, fileHelper);
        addServerProvider.accept(blockGen);
        addServerProvider.accept(new TagGen.ItemTag(packOutput, lookupProvider, blockGen.contentsGetter(), fileHelper));
        addServerProvider.accept(new TagGen.FluidTag(packOutput, lookupProvider, MODID, fileHelper));
    }
}
