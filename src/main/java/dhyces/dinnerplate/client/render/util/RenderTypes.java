package dhyces.dinnerplate.client.render.util;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat.Mode;

import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;

public class RenderTypes extends RenderStateShard {

	public RenderTypes() {
		super(null, null, null);
	}

	private static final RenderType FLUID = RenderType.create("dinnerplate:fluid",
			DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, true, true, RenderType.CompositeState.builder()
				.setShaderState(RENDERTYPE_ENTITY_TRANSLUCENT_CULL_SHADER)
				.setTextureState(BLOCK_SHEET_MIPPED)
				.setTransparencyState(TRANSLUCENT_TRANSPARENCY)
				.setLightmapState(LIGHTMAP)
				.setOverlayState(OVERLAY)
				.createCompositeState(true));

	public static RenderType getFluid() {
		return FLUID;
	}
	
	private static final RenderType MOCK_FOOD = RenderType.create("dinnerplate:mock",
			DefaultVertexFormat.NEW_ENTITY, Mode.QUADS, 256, true, true, RenderType.CompositeState.builder()
			.setShaderState(RENDERTYPE_ITEM_ENTITY_TRANSLUCENT_CULL_SHADER)
			.setTextureState(BLOCK_SHEET_MIPPED)
			.setTransparencyState(TRANSLUCENT_TRANSPARENCY)
			.setLightmapState(LIGHTMAP)
			.setOverlayState(OVERLAY)
			.createCompositeState(true));
	
	public static RenderType getMock() {
		return MOCK_FOOD;
	}
}
