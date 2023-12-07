package dhyces.dinnerplate.client.render.item;

import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Transformation;
import dhyces.dinnerplate.DinnerPlate;
import dhyces.dinnerplate.capability.CapabilityEventSubscriber;
import dhyces.dinnerplate.util.ResourceHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.SpriteContents;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.metadata.animation.AnimationMetadataSection;
import net.minecraft.client.resources.metadata.animation.FrameSize;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.client.model.*;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.client.textures.ForgeTextureMetadata;
import net.minecraftforge.registries.ForgeRegistries;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@OnlyIn(Dist.CLIENT)
public class MockFoodItemRenderer extends SimpleItemRenderer {

    private static final boolean testGen = false;
    boolean written = false;
    private final Map<ResourceLocation, BakedModel> generatedModels = testGen ? new ConcurrentHashMap<>() : null;

    @SuppressWarnings({"resource", "deprecation"})
    @Override
    public void render(ItemStack pStack, ItemDisplayContext displayContext, PoseStack pPoseStack, MultiBufferSource pBuffer,
                       int pPackedLight, int pPackedOverlay) {
        var capability = pStack.getCapability(CapabilityEventSubscriber.MOCK_FOOD_CAPABILITY).resolve();
        if (capability.isEmpty()) {
            return;
        }
        var realStack = capability.get().getRealStack();
        BakedModel model = Minecraft.getInstance().getItemRenderer().getItemModelShaper().getItemModel(realStack);
        model = model.getOverrides().resolve(model, realStack, clientLevel(), clientPlayer(), new Random(42).nextInt());
        if (model.isCustomRenderer()) {
            IClientItemExtensions.of(realStack).getCustomRenderer().renderByItem(realStack, displayContext, pPoseStack, pBuffer, pPackedLight, pPackedOverlay);
            return;
        }
        var biteCount = capability.get().getBiteCount();
        if (biteCount > 0) {
            var itemRL = ForgeRegistries.ITEMS.getKey(realStack.getItem());
            var bittenModelRL = new ResourceLocation(DinnerPlate.MODID, itemRL.getPath() + "_bitten");

            var bittenInvRL = new ResourceLocation(bittenModelRL.getNamespace(), "item/bitten/" + bittenModelRL.getPath());
            var bittenModel = Minecraft.getInstance().getModelManager().getModel(bittenInvRL);
            if (!bittenModel.equals(Minecraft.getInstance().getModelManager().getMissingModel()) && !bittenModel.getOverrides().equals(ItemOverrides.EMPTY)) {
                model = bittenModel.getOverrides().resolve(bittenModel, pStack, clientLevel(), clientPlayer(), 42);
            }
            // TODO: the commented section below is subject to removal
//            else if (testGen) {
//                var biteMaskRL = new ResourceLocation(DinnerPlate.MODID, biteCount == 1 ? "bite_mask_0" : "bite_mask_1");
//
//                var biteMaskAtlasRL = ResourceHelper.atlasitemTextureRL(biteMaskRL);
//                var biteMaskResourceRL = ResourceHelper.resourceItemTextureRL(biteMaskAtlasRL);
//
//
//                if (generatedModels.get(bittenModelRL) == null) {
//                    var modelQuads = model.getQuads(null, null, RandomSource.create(42L), ModelData.EMPTY, null);
//                    if (!modelQuads.isEmpty()) {
//                        var itemAtlasRL = modelQuads.get(0).getSprite().getName();
//                        var itemResourceRL = ResourceHelper.resourceItemTextureRL(itemAtlasRL);
//
//                        Optional<NativeImage> itemImage = tryGetStaticImage(itemResourceRL), maskImage = tryGetStaticImage(biteMaskResourceRL);
//                        if (itemImage.isPresent() && maskImage.isPresent()) {
//                            var atlas = Minecraft.getInstance().getTextureAtlas(atlas());
//                            var sprite = atlas.apply(itemAtlasRL);
//                            var mask = atlas.apply(biteMaskAtlasRL);
//                            generatedModels.put(bittenModelRL, testGen(model, mask, sprite, combineImages(itemImage.get(), maskImage.get())));
//                        }
//                    }
//                }
//                model = generatedModels.get(bittenModelRL);
//            }
        }
        var consumer = ItemRenderer.getFoilBufferDirect(pBuffer, RenderType.itemEntityTranslucentCull(atlas()), false, realStack.hasFoil());
        pPoseStack.pushPose();
        Minecraft.getInstance().getItemRenderer().renderModelLists(model, pStack, pPackedLight, pPackedOverlay, pPoseStack, consumer);
        pPoseStack.popPose();
    }

    // TODO: the commented section below is subject to removal
    /**
     * This is a method that generates a "builtin/generated" type item model, due to the inability to generate one outside of the model bakery
     * with vanilla and the broken state of the forge version. Right now, this does make a quad for pixel per pixel.
     */
//    private BakedModel testGen(BakedModel baseModel, TextureAtlasSprite template, TextureAtlasSprite sprite, NativeImage img) {
//        var builder = ItemMultiLayerBakedModel.builder(StandaloneModelConfiguration.INSTANCE, baseModel.getParticleIcon(), baseModel.getOverrides(), PerspectiveMapWrapper.getTransforms(baseModel.getTransforms()));
//        builder.addQuads(ItemLayerModel.getLayerRenderType(false), ItemModel.genStandardItem(img, sprite, 0));
//        builder.addQuads(ItemLayerModel.getLayerRenderType(false), ItemTextureQuadConverter.convertTexture(Transformation.identity(), template, sprite, 7.498f / 16f, Direction.NORTH, 0xFFFFFFFF, 1));
//        builder.addQuads(ItemLayerModel.getLayerRenderType(false), ItemTextureQuadConverter.convertTexture(Transformation.identity(), template, sprite, 8.502f / 16f, Direction.SOUTH, 0xFFFFFFFF, 1));
//        return builder.build();
//    }
//
//    @SuppressWarnings("deprecation")
//    private BakedModel generateModel(BakedModel baseModel, TextureAtlasSprite template, TextureAtlasSprite sprite) {
//        var builder = ItemMultiLayerBakedModel.builder(StandaloneModelConfiguration.INSTANCE, null, baseModel.getOverrides(), PerspectiveMapWrapper.getTransforms(baseModel.getTransforms()));
//
//        builder.addQuads(ItemLayerModel.getLayerRenderType(false), ItemTextureQuadConverter.convertTexture(Transformation.identity(), template, sprite, 7.498f / 16f, Direction.NORTH, 0xFFFFFFFF, 1));
//        builder.addQuads(ItemLayerModel.getLayerRenderType(false), ItemTextureQuadConverter.convertTexture(Transformation.identity(), template, sprite, 8.502f / 16f, Direction.SOUTH, 0xFFFFFFFF, 1));
//        var immList = ItemLayerModel.getQuadsForSprite(0, sprite, Transformation.identity(), false);
//        var realList = new ArrayList<BakedQuad>();
//        for (int i = 0; i < immList.size() - 2; i++) {
//            realList.add(immList.get(i));
//        }
//        builder.addQuads(ItemLayerModel.getLayerRenderType(false), realList);
//
//        return builder.build();
//    }

    private Optional<Resource> getResource(ResourceLocation resourceRL) {
        return Minecraft.getInstance().getResourceManager().getResource(resourceRL);
    }

    private Optional<NativeImage> tryGetStaticImage(ResourceLocation resourceRL) {
        var optionalResource = getResource(resourceRL);
        if (optionalResource.isPresent()) {
            try {
                return Optional.of(NativeImage.read(optionalResource.get().open()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return Optional.empty();
    }

    private NativeImage combineImages(NativeImage base, NativeImage mask) {
        NativeImage maskedBase;
        maskedBase = new NativeImage(base.getWidth(), base.getHeight(), false);
        maskedBase.copyFrom(base);
        // TODO: Remove this profiler
        Minecraft.getInstance().getProfiler().push("testing for reading");
        for (int i = 0; i < maskedBase.getHeight(); i++) {
            for (int j = 0; j < maskedBase.getWidth(); j++) {
                var maskRGBA = mask.getPixelRGBA(j, i) & 0xFF000000;
                var isAlpha = ~(maskRGBA >> 24) == -1;
                var itemRGBA = maskedBase.getPixelRGBA(j, i);

                maskedBase.setPixelRGBA(j, i, isAlpha ? maskRGBA : itemRGBA);
            }
        }
        Minecraft.getInstance().getProfiler().pop();
        return maskedBase;
    }


    private class VirtualSprite extends TextureAtlasSprite {

        protected VirtualSprite(TextureAtlasSprite baseSprite, NativeImage image) {
            super(TextureAtlas.LOCATION_BLOCKS,
                    new SpriteContents(baseSprite.atlasLocation(), new FrameSize(image.getWidth(), image.getHeight()), image, AnimationMetadataSection.EMPTY, ForgeTextureMetadata.EMPTY),
                    (int) (baseSprite.getX() / baseSprite.getU0()),
                    (int) (baseSprite.getY() / baseSprite.getV0()),
                    baseSprite.getX(),
                    baseSprite.getY());
        }

    }
}
