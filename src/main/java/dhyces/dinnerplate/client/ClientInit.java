package dhyces.dinnerplate.client;

import dhyces.dinnerplate.DinnerPlate;
import dhyces.dinnerplate.bite.IBitable;
import dhyces.dinnerplate.bite.IBitableItem;
import dhyces.dinnerplate.block.PlateBlock;
import dhyces.dinnerplate.client.model.SimpleCustomBakedModelWrapper;
import dhyces.dinnerplate.client.render.MeasuringCupRenderer;
import dhyces.dinnerplate.client.render.MixingBowlRenderer;
import dhyces.dinnerplate.client.render.PlateRenderer;
import dhyces.dinnerplate.registry.BEntityRegistry;
import dhyces.dinnerplate.registry.ItemRegistry;
import dhyces.dinnerplate.util.BlockHelper;
import dhyces.dinnerplate.util.ResourceHelper;
import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;

import static dhyces.dinnerplate.DinnerPlate.MODID;

public class ClientInit {

    private static List<Item> edibleItems;

    public static void register(IEventBus bus) {
        bus.addListener(ClientInit::clientSetup);
        bus.addListener(ClientInit::prepareSeparateModels);
        bus.addListener(ClientInit::entityRenders);
        bus.addListener(ClientInit::modelBakery);
    }

    private static void clientSetup(final FMLClientSetupEvent event) {
        ClampedItemPropertyFunction platePropertyFunction = (stack, level, entity, seed) ->
                (float) BlockHelper.getPropertyFromTag(PlateBlock.PLATES, BlockHelper.getBlockStateTag(stack).orElse(new CompoundTag())) / 8;

        var platePropertyRL = new ResourceLocation(DinnerPlate.MODID, "plates");

        edibleItems = ForgeRegistries.ITEMS.getEntries().stream().filter(c -> c.getValue().isEdible()).map(c -> c.getValue()).toList();

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

            edibleItems.stream().filter(i -> i instanceof IBitableItem).forEach(c -> {
                ItemProperties.register(c, DinnerPlate.modLoc("bites"), (pStack, pLevel, pEntity, pSeed) -> {
                    return (float) ((IBitableItem) c).getBiteCount(pStack) / (float) ((IBitableItem) c).getMaxBiteCount(pStack, pEntity);
                });
            });
        });
    }

    // TODO: remove the println.
    private static void prepareSeparateModels(final ModelEvent.RegisterAdditional event) {
        edibleItems.stream().filter(c -> !(c instanceof IBitable))
                .map(c -> {
                    var n = new ResourceLocation(MODID, "item/bitten/" + ForgeRegistries.ITEMS.getKey(c).getPath() + "_bitten");
                    System.out.println(c + " new one " + n);
                    return n;
                })
                .forEach(event::register);
    }

    private static void entityRenders(final EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(BEntityRegistry.PLATE_ENTITY.get(), PlateRenderer::new);
        event.registerBlockEntityRenderer(BEntityRegistry.MIXING_BOWL_ENTITY.get(), MixingBowlRenderer::new);
        event.registerBlockEntityRenderer(BEntityRegistry.MEASURING_CUP_ENTITY.get(), MeasuringCupRenderer::new);
    }

    private static void modelBakery(final ModelEvent.ModifyBakingResult e) {
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

    private static BakedModel getModelFromEvent(final ModelEvent.ModifyBakingResult e, ResourceLocation resource) {
        return e.getModels().get(ResourceHelper.inventoryModel(resource));
    }

    private static void putCustomInRegistry(final ModelEvent.ModifyBakingResult e, ResourceLocation resource) {
        e.getModels().put(ResourceHelper.inventoryModel(resource), new SimpleCustomBakedModelWrapper(getModelFromEvent(e, resource)));
    }

    // TODO: remove if unused
//    private void putInRegistry(final ModelBakeEvent e, ResourceLocation resource, BakedModel model) {
//    	e.getModelRegistry().put(ResourceHelper.inventoryModel(resource), model);
//    }
}
