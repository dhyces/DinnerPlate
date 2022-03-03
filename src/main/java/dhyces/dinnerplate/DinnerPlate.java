package dhyces.dinnerplate;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dhyces.dinnerplate.bite.IBitable;
import dhyces.dinnerplate.block.PlateBlock;
import dhyces.dinnerplate.capability.CapabilityEventSubscriber;
import dhyces.dinnerplate.datagen.ModelGenerator;
import dhyces.dinnerplate.item.BitableItem;
import dhyces.dinnerplate.model.SimpleCustomBakedModelWrapper;
import dhyces.dinnerplate.registry.BEntityRegistry;
import dhyces.dinnerplate.registry.BlockRegistry;
import dhyces.dinnerplate.registry.FluidRegistry;
import dhyces.dinnerplate.registry.ItemRegistry;
import dhyces.dinnerplate.registry.SoundRegistry;
import dhyces.dinnerplate.render.block.MeasuringCupBlockRenderer;
import dhyces.dinnerplate.render.block.MixingBowlBlockRenderer;
import dhyces.dinnerplate.render.block.PlateBlockRenderer;
import dhyces.dinnerplate.util.BlockHelper;
import dhyces.dinnerplate.util.ResourceHelper;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.Unit;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;
import net.minecraftforge.client.model.ForgeModelBakery;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;
import net.minecraftforge.registries.ForgeRegistries;

@Mod(DinnerPlate.MODID)
public class DinnerPlate {

	public static final String MODID = "dinnerplate";

	private List<Item> edibleItems;

    public static final Logger LOGGER = LogManager.getLogger();

    public static void LOG_INFO(String str) {
    	LOGGER.info(str);
    }

    public DinnerPlate() {
    	var bus = FMLJavaModLoadingContext.get().getModEventBus();

    	ForgeMod.enableMilkFluid();

        bus.addListener(this::setup);
        bus.addListener(this::clientSetup);
        bus.addListener(this::modelRegistry);
        bus.addListener(EventPriority.HIGHEST, this::reloadSeparateModels);
        bus.addListener(this::entityRenders);
        bus.addListener(this::modelBakery);
        bus.addListener(this::dataGenerators);

        registerRegistries(bus);

        MinecraftForge.EVENT_BUS.register(this);
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
    	event.enqueueWork(() -> ItemProperties.register(ItemRegistry.PLATE_ITEM.get(), new ResourceLocation(DinnerPlate.MODID, "plates"), (stack, level, entity, seed) -> {
			return (float) BlockHelper.getPropertyFromTag(PlateBlock.PLATES, BlockHelper.getBlockStateTag(stack)) / 8;
		}));
    	event.enqueueWork(() -> ItemProperties.register(ItemRegistry.MOCK_FOOD_ITEM.get(), new ResourceLocation(DinnerPlate.MODID, "bites"), (stack, level, entity, seed) -> {
    		var cap = stack.getCapability(CapabilityEventSubscriber.MOCK_FOOD_CAPABILITY).resolve().get();
			return (float) cap.getBiteCount(stack) / cap.getMaxBiteCount(stack);
		}));
    	event.enqueueWork(() -> ItemProperties.register(ItemRegistry.BITABLE_ITEM.get(), new ResourceLocation(DinnerPlate.MODID, "bites"), (stack, level, entity, seed) -> {
			if (stack.getItem() instanceof BitableItem b)
				return (float) b.getBiteCount(stack) / b.getMaxBiteCount(stack);
			return 1.0f;
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

    // TODO: remove the println. Also I want to only actually get models that are used, aka have a list of edible items and then just add those
    // the the special model list
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
    	putInRegistry(e, ItemRegistry.MOCK_FOOD_ITEM.getId(), new SimpleCustomBakedModelWrapper(getModelFromEvent(e, ItemRegistry.MOCK_FOOD_ITEM.getId())));
    	putInRegistry(e, ItemRegistry.PLATE_ITEM.getId(), new SimpleCustomBakedModelWrapper(getModelFromEvent(e, ItemRegistry.PLATE_ITEM.getId())));
    }

    private BakedModel getModelFromEvent(final ModelBakeEvent e, ResourceLocation resource) {
    	return e.getModelManager().getModel(ResourceHelper.inventoryModel(resource));
    }

    private void putInRegistry(final ModelBakeEvent e, ResourceLocation resource, BakedModel model) {
    	e.getModelRegistry().put(ResourceHelper.inventoryModel(resource), model);
    }

    private void dataGenerators(final GatherDataEvent event) {
    	event.getGenerator().addProvider(new ModelGenerator(event.getGenerator(), MODID,  event.getExistingFileHelper()));
    }

    public static CreativeModeTab tab = new CreativeModeTab(MODID) {

		@Override
		public ItemStack makeIcon() {
			return ItemRegistry.PLATE_ITEM.get().getDefaultInstance();
		}
	};

}
