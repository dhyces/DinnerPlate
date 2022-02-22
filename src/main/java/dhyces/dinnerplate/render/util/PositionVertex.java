package dhyces.dinnerplate.render.util;

import com.mojang.blaze3d.vertex.VertexFormatElement;

public class PositionVertex extends AbstractVertex {

	public PositionVertex(float x, float y, float z, float w) {
		super(VertexFormatElement.Usage.POSITION, x, y, z, w);
	}
}
