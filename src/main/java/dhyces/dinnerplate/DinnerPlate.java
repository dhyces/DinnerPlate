package dhyces.dinnerplate;

import dhyces.dinnerplate.client.ClientInit;
import dhyces.dinnerplate.datagen.*;
import dhyces.dinnerplate.registry.*;
import net.minecraft.data.DataProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.Consumer;
import java.util.function.Function;

@Mod(DinnerPlate.MODID)
public class DinnerPlate {

    public static final String MODID = "dinnerplate";

    public static final Logger LOGGER = LogManager.getLogger(DinnerPlate.class);
    public static CreativeModeTab TAB;

    public DinnerPlate() {
        var bus = FMLJavaModLoadingContext.get().getModEventBus();

        ForgeMod.enableMilkFluid();

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
        BlockRegistry.register(bus);
        BEntityRegistry.register(bus);
        ItemRegistry.register(bus);
        FluidTypeRegistry.register(bus);
        FluidRegistry.register(bus);
        SoundRegistry.register(bus);
        RecipeRegistry.register(bus);
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

        addServerProvider.accept(new BlockLootTableGen.BlockLootTableProvider(packOutput));
        var blockGen = new TagGen.BlockTag(packOutput, lookupProvider, MODID, fileHelper);
        addServerProvider.accept(blockGen);
        addServerProvider.accept(new TagGen.ItemTag(packOutput, lookupProvider, blockGen, MODID, fileHelper));
        addServerProvider.accept(new TagGen.FluidTag(packOutput, lookupProvider, MODID, fileHelper));
    }
}
