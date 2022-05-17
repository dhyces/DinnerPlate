package dhyces.dinnerplate;

import dhyces.dinnerplate.bite.IBitable;
import dhyces.dinnerplate.bite.IBitableItem;
import dhyces.dinnerplate.block.PlateBlock;
import dhyces.dinnerplate.capability.CapabilityEventSubscriber;
import dhyces.dinnerplate.client.ClientInit;
import dhyces.dinnerplate.client.model.SimpleCustomBakedModelWrapper;
import dhyces.dinnerplate.client.render.MixingBowlRenderer;
import dhyces.dinnerplate.client.render.SimpleBlockItemRenderer;
import dhyces.dinnerplate.client.render.block.MeasuringCupBlockRenderer;
import dhyces.dinnerplate.client.render.block.MixingBowlBlockRenderer;
import dhyces.dinnerplate.client.render.block.PlateBlockRenderer;
import dhyces.dinnerplate.datagen.BlockLootTableGen;
import dhyces.dinnerplate.datagen.ModelGen;
import dhyces.dinnerplate.datagen.TagGen;
import dhyces.dinnerplate.item.BitableItem;
import dhyces.dinnerplate.registry.*;
import dhyces.dinnerplate.util.BlockHelper;
import dhyces.dinnerplate.util.ResourceHelper;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.renderer.item.ItemPropertyFunction;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.Unit;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;
import net.minecraftforge.client.model.ForgeModelBakery;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@SuppressWarnings("deprecation")
@Mod(DinnerPlate.MODID)
public class DinnerPlate {

	public static final String MODID = "dinnerplate";

    public static final Logger LOGGER = LogManager.getLogger(DinnerPlate.class);

    public static void LOG_INFO(String str) {
    	LOGGER.info(str);
    }

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

    private void registerRegistries(final IEventBus bus) {
    	BlockRegistry.register(bus);
        BEntityRegistry.register(bus);
        ItemRegistry.register(bus);
        FluidRegistry.register(bus);
        SoundRegistry.register(bus);
    }

    private void setup(final FMLCommonSetupEvent event) {

    }

    private void dataGenerators(final GatherDataEvent event) {
    	event.getGenerator().addProvider(new ModelGen(event.getGenerator(), MODID,  event.getExistingFileHelper()));
    	event.getGenerator().addProvider(new BlockLootTableGen.BlockLootTableProvider(event.getGenerator()));
		var blockGen = new TagGen.BlockTag(event.getGenerator(), MODID, event.getExistingFileHelper());
		event.getGenerator().addProvider(blockGen);
		event.getGenerator().addProvider(new TagGen.ItemTag(event.getGenerator(), blockGen, MODID, event.getExistingFileHelper()));
		event.getGenerator().addProvider(new TagGen.FluidTag(event.getGenerator(), MODID, event.getExistingFileHelper()));
    }

	public static ResourceLocation modLoc(String path) {
		return new ResourceLocation(MODID, path);
	}
    
    public static final CreativeModeTab TAB = new CreativeModeTab(MODID) {

		@Override
		public ItemStack makeIcon() {
			return ItemRegistry.WHITE_PLATE_ITEM.get().getDefaultInstance();
		}
	};
}
