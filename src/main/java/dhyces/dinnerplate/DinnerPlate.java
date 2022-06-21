package dhyces.dinnerplate;

import dhyces.dinnerplate.client.ClientInit;
import dhyces.dinnerplate.datagen.*;
import dhyces.dinnerplate.registry.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@SuppressWarnings("deprecation")
@Mod(DinnerPlate.MODID)
public class DinnerPlate {

    public static final String MODID = "dinnerplate";

    public static final Logger LOGGER = LogManager.getLogger(DinnerPlate.class);
    public static final CreativeModeTab TAB = new CreativeModeTab(MODID) {

        @Override
        public ItemStack makeIcon() {
            return ItemRegistry.WHITE_PLATE_ITEM.get().getDefaultInstance();
        }
    };

    public DinnerPlate() {
        var bus = FMLJavaModLoadingContext.get().getModEventBus();

        ForgeMod.enableMilkFluid();

        bus.addListener(this::setup);
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            new ClientInit(bus);
        });

        if (FMLLoader.getLaunchHandler().isData())
            bus.addListener(this::dataGenerators);

        registerRegistries(bus);
    }

    public static void LOG_INFO(String str) {
        LOGGER.info(str);
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

    private void setup(final FMLCommonSetupEvent event) {
    }

    private void dataGenerators(final GatherDataEvent event) {
        event.getGenerator().addProvider(event.includeClient(), new BlockModelGen(event.getGenerator(), MODID, event.getExistingFileHelper()));
        event.getGenerator().addProvider(event.includeClient(), new ItemModelGen(event.getGenerator(), MODID, event.getExistingFileHelper()));
        event.getGenerator().addProvider(event.includeClient(), new StateGen(event.getGenerator(), MODID, event.getExistingFileHelper()));

        event.getGenerator().addProvider(event.includeServer(), new BlockLootTableGen.BlockLootTableProvider(event.getGenerator()));
        var blockGen = new TagGen.BlockTag(event.getGenerator(), MODID, event.getExistingFileHelper());
        event.getGenerator().addProvider(event.includeServer(), blockGen);
        event.getGenerator().addProvider(event.includeServer(), new TagGen.ItemTag(event.getGenerator(), blockGen, MODID, event.getExistingFileHelper()));
        event.getGenerator().addProvider(event.includeServer(), new TagGen.FluidTag(event.getGenerator(), MODID, event.getExistingFileHelper()));
    }
}
