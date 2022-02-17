package dhyces.dinnerplate;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.BuiltInModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.client.resources.model.SimpleBakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MilkBucketItem;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.model.DynamicBucketModel;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.systems.RenderSystem;

import dhyces.dinnerplate.block.MixingBowlBlock;
import dhyces.dinnerplate.block.PlateBlock;
import dhyces.dinnerplate.datagen.BucketModelGenerator;
import dhyces.dinnerplate.model.EmptyCustomModel;
import dhyces.dinnerplate.model.SimpleCustomBakedModelWrapper;
import dhyces.dinnerplate.registry.BEntityRegistry;
import dhyces.dinnerplate.registry.BlockRegistry;
import dhyces.dinnerplate.registry.FluidRegistry;
import dhyces.dinnerplate.registry.SoundRegistry;
import dhyces.dinnerplate.registry.ItemRegistry;
import dhyces.dinnerplate.render.block.MixingBowlBlockRenderer;
import dhyces.dinnerplate.render.block.PlateBlockRenderer;
import dhyces.dinnerplate.render.item.MockFoodItemRenderer;
import dhyces.dinnerplate.render.item.PlateItemRenderer;
import dhyces.dinnerplate.render.util.RenderTypes;
import dhyces.dinnerplate.util.BlockHelper;
import dhyces.dinnerplate.util.ModelHelper;

@Mod(DinnerPlate.MODID)
public class DinnerPlate {
	
	public static final String MODID = "dinnerplate";
	
    public static final Logger LOGGER = LogManager.getLogger();
    
    public static void LOG_INFO(String str) {
    	LOGGER.info(str);
    }

    public DinnerPlate() {
    	var bus = FMLJavaModLoadingContext.get().getModEventBus();
    	
    	ForgeMod.enableMilkFluid();
    	
        bus.addListener(this::setup);
        bus.addListener(this::clientSetup);
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
    	event.enqueueWork(() -> setRenderLayers());
    }
    
    private void setRenderLayers() {
    	ItemBlockRenderTypes.setRenderLayer(BlockRegistry.MEASURING_CUP_BLOCK.get(), RenderType.translucent());
    	ItemBlockRenderTypes.setRenderLayer(BlockRegistry.MUSHROOM_STEW_FLUID_BLOCK.get(), RenderType.translucent());
    	ItemBlockRenderTypes.setRenderLayer(BlockRegistry.BEETROOT_SOUP_FLUID_BLOCK.get(), RenderType.translucent());
    	ItemBlockRenderTypes.setRenderLayer(BlockRegistry.RABBIT_STEW_FLUID_BLOCK.get(), RenderType.translucent());
    }
    
    private void entityRenders(final EntityRenderersEvent.RegisterRenderers event) {
    	event.registerBlockEntityRenderer(BEntityRegistry.PLATE_ENTITY.get(), PlateBlockRenderer::new);
    	event.registerBlockEntityRenderer(BEntityRegistry.MIXING_BOWL_ENTITY.get(), MixingBowlBlockRenderer::new);
    }
    
    private void modelBakery(final ModelBakeEvent e) {
    	putInRegistry(e, ItemRegistry.MOCK_FOOD_ITEM.getId(), new SimpleCustomBakedModelWrapper(getModelFromEvent(e, new ResourceLocation(MODID, "item/bite_mask_0"))));
    	putInRegistry(e, ItemRegistry.PLATE_ITEM.getId(), new SimpleCustomBakedModelWrapper(getModelFromEvent(e, ItemRegistry.PLATE_ITEM.getId())));
    }
    
    private void dataGenerators(final GatherDataEvent event) {
    	event.getGenerator().addProvider(new BucketModelGenerator(event.getGenerator(), MODID, event.getExistingFileHelper()));
    }
    
    private BakedModel getModelFromEvent(final ModelBakeEvent e, ResourceLocation resource) {
    	return e.getModelManager().getModel(ModelHelper.inventoryModel(resource));
    }
    
    private void putInRegistry(final ModelBakeEvent e, ResourceLocation resource, BakedModel model) {
    	e.getModelRegistry().put(ModelHelper.inventoryModel(resource), model); 
    }
    
    public static CreativeModeTab tab = new CreativeModeTab(MODID) {
		
		@Override
		public ItemStack makeIcon() {
			return ItemRegistry.PLATE_ITEM.get().getDefaultInstance();
		}
	};
    
}
