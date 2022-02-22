package dhyces.dinnerplate.render.util;

import com.mojang.blaze3d.vertex.VertexFormatElement;

public class UVVertex extends AbstractVertex {

	public UVVertex(float u, float v) {
		super(VertexFormatElement.Usage.UV, u, v, 0f, 1f);
	}
}
