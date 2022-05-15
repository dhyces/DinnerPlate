package dhyces.dinnerplate;

import dhyces.dinnerplate.bite.IBitable;
import dhyces.dinnerplate.block.PlateBlock;
import dhyces.dinnerplate.capability.CapabilityEventSubscriber;
import dhyces.dinnerplate.client.model.SimpleCustomBakedModelWrapper;
import dhyces.dinnerplate.client.render.block.MeasuringCupBlockRenderer;
import dhyces.dinnerplate.client.render.block.MixingBowlBlockRenderer;
import dhyces.dinnerplate.client.render.block.PlateBlockRenderer;
import dhyces.dinnerplate.datagen.BlockLootTableGen;
import dhyces.dinnerplate.datagen.ModelGen;
import dhyces.dinnerplate.datagen.TagGen;
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

	private List<Item> edibleItems;

    public static final Logger LOGGER = LogManager.getLogger(DinnerPlate.class);

    public static void LOG_INFO(String str) {
    	LOGGER.info(str);
    }

    public DinnerPlate() {
    	var bus = FMLJavaModLoadingContext.get().getModEventBus();

    	ForgeMod.enableMilkFluid();

        bus.addListener(this::setup);
		DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
			bus.addListener(this::clientSetup);
			bus.addListener(this::modelRegistry);
			bus.addListener(EventPriority.HIGHEST, this::reloadSeparateModels);
			bus.addListener(this::entityRenders);
			bus.addListener(this::modelBakery);
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
    
	private void clientSetup(final FMLClientSetupEvent event) {
    	ItemPropertyFunction platePropertyFunction = (stack, level, entity, seed) -> {
			return (float) BlockHelper.getPropertyFromTag(PlateBlock.PLATES, BlockHelper.getBlockStateTag(stack).orElse(new CompoundTag())) / 8;
		};
		var platePropertyRL = new ResourceLocation(DinnerPlate.MODID, "plates");
    	event.enqueueWork(() -> {
    		ItemProperties.register(ItemRegistry.WHITE_PLATE_ITEM.get(), platePropertyRL, platePropertyFunction);
    		ItemProperties.register(ItemRegistry.ORANGE_PLATE_ITEM.get(), platePropertyRL, platePropertyFunction);
    		ItemProperties.register(ItemRegistry.MAGENTA_PLATE_ITEM.get(), platePropertyRL, platePropertyFunction);
    		ItemProperties.register(ItemRegistry.LIGHT_BLUE_PLATE_ITEM.get(), platePropertyRL, platePropertyFunction);
    		ItemProperties.register(ItemRegistry.YELLOW_PLATE_ITEM.get(), platePropertyRL, platePropertyFunction);
    		ItemProperties.register(ItemRegistry.LIME_PLATE_ITEM.get(), platePropertyRL, platePropertyFunction);
    		ItemProperties.register(ItemRegistry.PINK_PLATE_ITEM.get(), platePropertyRL, platePropertyFunction);
    		ItemProperties.register(ItemRegistry.GRAY_PLATE_ITEM.get(), platePropertyRL, platePropertyFunction);
    		ItemProperties.register(ItemRegistry.LIGHT_GRAY_PLATE_ITEM.get(), platePropertyRL, platePropertyFunction);
    		ItemProperties.register(ItemRegistry.CYAN_PLATE_ITEM.get(), platePropertyRL, platePropertyFunction);
    		ItemProperties.register(ItemRegistry.PURPLE_PLATE_ITEM.get(), platePropertyRL, platePropertyFunction);
    		ItemProperties.register(ItemRegistry.BLUE_PLATE_ITEM.get(), platePropertyRL, platePropertyFunction);
    		ItemProperties.register(ItemRegistry.BROWN_PLATE_ITEM.get(), platePropertyRL, platePropertyFunction);
    		ItemProperties.register(ItemRegistry.GREEN_PLATE_ITEM.get(), platePropertyRL, platePropertyFunction);
    		ItemProperties.register(ItemRegistry.RED_PLATE_ITEM.get(), platePropertyRL, platePropertyFunction);
    		ItemProperties.register(ItemRegistry.BLACK_PLATE_ITEM.get(), platePropertyRL, platePropertyFunction);
    		});
    	event.enqueueWork(() -> ItemProperties.register(ItemRegistry.MOCK_FOOD_ITEM.get(), new ResourceLocation(DinnerPlate.MODID, "bites"), (stack, level, entity, seed) -> {
    		var cap = stack.getCapability(CapabilityEventSubscriber.MOCK_FOOD_CAPABILITY).resolve().get();
			return (float) cap.getBiteCount(stack) / cap.getMaxBiteCount(stack);
		}));
    	event.enqueueWork(() -> setRenderLayers());
    }

    private void setRenderLayers() {
    	ItemBlockRenderTypes.setRenderLayer(BlockRegistry.MEASURING_CUP_BLOCK.get(), RenderType.translucent());
    	ItemBlockRenderTypes.setRenderLayer(BlockRegistry.MUSHROOM_STEW_FLUID_BLOCK.get(), RenderType.translucent());
    	ItemBlockRenderTypes.setRenderLayer(BlockRegistry.BEETROOT_SOUP_FLUID_BLOCK.get(), RenderType.translucent());
    	ItemBlockRenderTypes.setRenderLayer(BlockRegistry.RABBIT_STEW_FLUID_BLOCK.get(), RenderType.translucent());
    }

    private void modelRegistry(final ModelRegistryEvent event) {
    	edibleItems = ForgeRegistries.ITEMS.getEntries().stream().filter(c -> c.getValue().isEdible()).map(c -> c.getValue()).toList();
    	prepareSeparateModels();
    }

    // TODO: remove the println.
    private void prepareSeparateModels() {
    	edibleItems.stream().filter(c -> !(c instanceof IBitable))
		.map(c -> {
			var n = new ResourceLocation(MODID, "item/bitten/bitten_" + c.getRegistryName().getPath());
			System.out.println(c + " new one " + n);
			return n;
			})
		.forEach(ForgeModelBakery::addSpecialModel);
    }

    private void reloadSeparateModels(final RegisterClientReloadListenersEvent event) {
    	event.registerReloadListener(new PreparableReloadListener() {

			@Override
			public CompletableFuture<Void> reload(PreparationBarrier pPreparationBarrier, ResourceManager pResourceManager,
					ProfilerFiller pPreparationsProfiler, ProfilerFiller pReloadProfiler, Executor pBackgroundExecutor,
					Executor pGameExecutor) {
				return pPreparationBarrier.wait(Unit.INSTANCE).thenRunAsync(() -> prepareSeparateModels());
			}
		});
    }

    private void entityRenders(final EntityRenderersEvent.RegisterRenderers event) {
    	event.registerBlockEntityRenderer(BEntityRegistry.PLATE_ENTITY.get(), PlateBlockRenderer::new);
    	event.registerBlockEntityRenderer(BEntityRegistry.MIXING_BOWL_ENTITY.get(), MixingBowlBlockRenderer::new);
    	event.registerBlockEntityRenderer(BEntityRegistry.MEASURING_CUP_ENTITY.get(), MeasuringCupBlockRenderer::new);
    }

    private void modelBakery(final ModelBakeEvent e) {
    	putCustomInRegistry(e, ItemRegistry.MOCK_FOOD_ITEM.getId());
    	putCustomInRegistry(e, ItemRegistry.MIXING_BOWL_ITEM.getId());
    	putCustomInRegistry(e, ItemRegistry.MEASURING_CUP_ITEM.getId());
    	
    	putCustomInRegistry(e, ItemRegistry.WHITE_PLATE_ITEM.getId());
    	putCustomInRegistry(e, ItemRegistry.ORANGE_PLATE_ITEM.getId());
    	putCustomInRegistry(e, ItemRegistry.MAGENTA_PLATE_ITEM.getId());
    	putCustomInRegistry(e, ItemRegistry.LIGHT_BLUE_PLATE_ITEM.getId());
    	putCustomInRegistry(e, ItemRegistry.YELLOW_PLATE_ITEM.getId());
    	putCustomInRegistry(e, ItemRegistry.LIME_PLATE_ITEM.getId());
    	putCustomInRegistry(e, ItemRegistry.PINK_PLATE_ITEM.getId());
    	putCustomInRegistry(e, ItemRegistry.GRAY_PLATE_ITEM.getId());
    	putCustomInRegistry(e, ItemRegistry.LIGHT_GRAY_PLATE_ITEM.getId());
    	putCustomInRegistry(e, ItemRegistry.CYAN_PLATE_ITEM.getId());
    	putCustomInRegistry(e, ItemRegistry.PURPLE_PLATE_ITEM.getId());
    	putCustomInRegistry(e, ItemRegistry.BLUE_PLATE_ITEM.getId());
    	putCustomInRegistry(e, ItemRegistry.BROWN_PLATE_ITEM.getId());
    	putCustomInRegistry(e, ItemRegistry.GREEN_PLATE_ITEM.getId());
    	putCustomInRegistry(e, ItemRegistry.RED_PLATE_ITEM.getId());
    	putCustomInRegistry(e, ItemRegistry.BLACK_PLATE_ITEM.getId());
    }

    private BakedModel getModelFromEvent(final ModelBakeEvent e, ResourceLocation resource) {
    	return e.getModelManager().getModel(ResourceHelper.inventoryModel(resource));
    }

    private void putCustomInRegistry(final ModelBakeEvent e, ResourceLocation resource) {
    	e.getModelRegistry().put(ResourceHelper.inventoryModel(resource), new SimpleCustomBakedModelWrapper(getModelFromEvent(e, resource)));
    }

    // TODO: remove if unused
//    private void putInRegistry(final ModelBakeEvent e, ResourceLocation resource, BakedModel model) {
//    	e.getModelRegistry().put(ResourceHelper.inventoryModel(resource), model);
//    }

    private void dataGenerators(final GatherDataEvent event) {
    	event.getGenerator().addProvider(new ModelGen(event.getGenerator(), MODID,  event.getExistingFileHelper()));
    	event.getGenerator().addProvider(new BlockLootTableGen.BlockLootTableProvider(event.getGenerator()));
		event.getGenerator().addProvider(new TagGen(event.getGenerator(), new BlockTagsProvider(event.getGenerator()), MODID, event.getExistingFileHelper()));
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
