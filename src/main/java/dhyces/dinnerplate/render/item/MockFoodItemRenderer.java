package dhyces.dinnerplate.render.item;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

import org.apache.http.impl.io.EmptyInputStream;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Matrix4f;
import com.mojang.math.Transformation;

import dhyces.dinnerplate.DinnerPlate;
import dhyces.dinnerplate.capability.CapabilityEventSubscriber;
import dhyces.dinnerplate.render.util.ItemModel;
import dhyces.dinnerplate.util.ModelHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderStateShard.TextureStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockElement;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.block.model.ItemModelGenerator;
import net.minecraft.client.renderer.block.model.ItemTransforms.TransformType;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.client.renderer.texture.Stitcher;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.metadata.animation.AnimationMetadataSection;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.data.models.ItemModelGenerators;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.metadata.MetadataSectionSerializer;
import net.minecraft.server.packs.resources.FallbackResourceManager;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.model.ItemLayerModel;
import net.minecraftforge.client.model.ItemMultiLayerBakedModel;
import net.minecraftforge.client.model.ItemTextureQuadConverter;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.client.model.PerspectiveMapWrapper;
import net.minecraftforge.client.model.StandaloneModelConfiguration;
import net.minecraftforge.common.extensions.IForgePackResources;

@OnlyIn(Dist.CLIENT)
public class MockFoodItemRenderer extends SimpleItemRenderer {

	private static final boolean testGen = false;
	private Map<ResourceLocation, BakedModel> generatedModels = testGen ? new ConcurrentHashMap<>() : null;
	boolean written = false;
	
	@SuppressWarnings("deprecation")
	@Override
	public void renderByItem(ItemStack pStack, TransformType pTransformType, PoseStack pPoseStack,
			MultiBufferSource pBuffer, int pPackedLight, int pPackedOverlay) {
		var capability = pStack.getCapability(CapabilityEventSubscriber.MOCK_FOOD_CAPABILITY).resolve();
		if (capability.isEmpty()) {
			return;
		}
		var realStack = capability.get().getRealStack();
		BakedModel model = Minecraft.getInstance().getItemRenderer().getItemModelShaper().getItemModel(realStack);
		var biteCount = capability.get().getBiteCount();
		if (biteCount > 0) {
			var itemRL = realStack.getItem().getRegistryName();
			var bittenModelRL = new ResourceLocation(DinnerPlate.MODID, "bitten_" + itemRL.getPath() + "_" + (biteCount-1));
			
			var bittenInvRL = new ResourceLocation(bittenModelRL.getNamespace(), "item/bitten/" + bittenModelRL.getPath());
			var bittenModel = Minecraft.getInstance().getModelManager().getModel(bittenInvRL);
			if (!bittenModel.equals(Minecraft.getInstance().getModelManager().getMissingModel())) {
				model = bittenModel;
			} else if (testGen) {
				var biteMaskRL = new ResourceLocation(DinnerPlate.MODID, biteCount == 1 ? "bite_mask_0" : "bite_mask_1");
				
				var biteMaskAtlasRL = atlasitemTextureRL(biteMaskRL);
				var biteMaskResourceRL = resourceItemTextureRL(biteMaskAtlasRL);
				
				
				if (generatedModels.get(bittenModelRL) == null) {
					var modelQuads = model.getQuads(null, null, null, null);
					if (!modelQuads.isEmpty()) {
						var itemAtlasRL = modelQuads.get(0).getSprite().getName();
						var itemResourceRL = resourceItemTextureRL(itemAtlasRL);
						
						Optional<NativeImage> itemImage = tryGetStaticImage(itemResourceRL), maskImage = tryGetStaticImage(biteMaskResourceRL);
						if (itemImage.isPresent() && maskImage.isPresent()) {
							var atlas = Minecraft.getInstance().getTextureAtlas(TextureAtlas.LOCATION_BLOCKS);
							var sprite = atlas.apply(itemAtlasRL);
							var mask = atlas.apply(biteMaskAtlasRL);
							generatedModels.put(bittenModelRL, testGen(model, mask, sprite, combineImages(itemImage.get(), maskImage.get())));
						}
					}
				}
				model = generatedModels.get(bittenModelRL);
			}
		}
		pPoseStack.pushPose();
		/** TODO: need to modify the model with a mask*/
		pPoseStack.translate(0.5, 0.5, 0.5);
		var flag = pTransformType == TransformType.GUI && !model.usesBlockLight();
		if (flag)
			Lighting.setupForFlatItems();
		Minecraft.getInstance().getItemRenderer().render(realStack, pTransformType, false, pPoseStack, pBuffer, pPackedLight, pPackedOverlay, model);

		Minecraft.getInstance().renderBuffers().bufferSource().endBatch();
		RenderSystem.enableDepthTest();
		if (flag)
			Lighting.setupFor3DItems();
		pPoseStack.popPose();
		RenderSystem.applyModelViewMatrix();
	}
	
	private BakedModel testGen(BakedModel baseModel, TextureAtlasSprite template, TextureAtlasSprite sprite, NativeImage img) {
		var builder = ItemMultiLayerBakedModel.builder(StandaloneModelConfiguration.INSTANCE, baseModel.getParticleIcon(), baseModel.getOverrides(), PerspectiveMapWrapper.getTransforms(baseModel.getTransforms()));
		builder.addQuads(ItemLayerModel.getLayerRenderType(false), ItemModel.genStandardItem(img, sprite, 0));
		builder.addQuads(ItemLayerModel.getLayerRenderType(false), ItemTextureQuadConverter.convertTexture(Transformation.identity(), template, sprite, 7.498f / 16f, Direction.NORTH, 0xFFFFFFFF, 1));
		builder.addQuads(ItemLayerModel.getLayerRenderType(false), ItemTextureQuadConverter.convertTexture(Transformation.identity(), template, sprite, 8.502f / 16f, Direction.SOUTH, 0xFFFFFFFF, 1));
		return builder.build();
	}
	
	@SuppressWarnings("deprecation")
	private BakedModel generateModel(BakedModel baseModel, TextureAtlasSprite template, TextureAtlasSprite sprite) {
		var builder = ItemMultiLayerBakedModel.builder(StandaloneModelConfiguration.INSTANCE, null, baseModel.getOverrides(), PerspectiveMapWrapper.getTransforms(baseModel.getTransforms()));

		builder.addQuads(ItemLayerModel.getLayerRenderType(false), ItemTextureQuadConverter.convertTexture(Transformation.identity(), template, sprite, 7.498f / 16f, Direction.NORTH, 0xFFFFFFFF, 1));
		builder.addQuads(ItemLayerModel.getLayerRenderType(false), ItemTextureQuadConverter.convertTexture(Transformation.identity(), template, sprite, 8.502f / 16f, Direction.SOUTH, 0xFFFFFFFF, 1));
		var immList = ItemLayerModel.getQuadsForSprite(0, sprite, Transformation.identity(), false);
		var realList = new ArrayList<BakedQuad>();
		for (int i = 0; i < immList.size()-2; i++) {
			realList.add(immList.get(i));
		}
		builder.addQuads(ItemLayerModel.getLayerRenderType(false), realList);
		
		return builder.build();
	}
	
	private ResourceLocation atlasitemTextureRL(ResourceLocation registryRL) {
		return new ResourceLocation(registryRL.getNamespace(), "item/" + registryRL.getPath());
	}
	
	
	private ResourceLocation resourceItemTextureRL(ResourceLocation atlasRL) {
		return new ResourceLocation(atlasRL.getNamespace(), "textures/" + atlasRL.getPath() + ".png");
	}
	
	private Resource getResource(ResourceLocation resourceRL) throws Exception {
		try {
			return Minecraft.getInstance().getResourceManager().getResource(resourceRL);
		} catch (IOException e) {
			e.printStackTrace();
		}
		throw new Exception("Resource with RL: {" + resourceRL + "} not found");
	}
	
	private Optional<NativeImage> tryGetStaticImage(ResourceLocation resourceRL) {
		try {
			return Optional.of(NativeImage.read(getResource(resourceRL).getInputStream()));
		} catch (Exception e) {
			e.printStackTrace();
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
			super(Minecraft.getInstance().getModelManager().getAtlas(TextureAtlas.LOCATION_BLOCKS), 
					new TextureAtlasSprite.Info(baseSprite.getName(), image.getWidth(), image.getHeight(), AnimationMetadataSection.EMPTY),
					Minecraft.getInstance().options.mipmapLevels,
					(int)(baseSprite.getX() / baseSprite.getU0()),
					(int)(baseSprite.getY() / baseSprite.getV0()),
					baseSprite.getX(),
					baseSprite.getY(),
					image);
		}
		
	}
}
