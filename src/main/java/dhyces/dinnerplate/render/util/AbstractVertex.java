package dhyces.dinnerplate.render.util;

import com.mojang.blaze3d.vertex.VertexFormatElement;

// Represents 16 bytes of data
public abstract class AbstractVertex {

	protected final float v0, v1, v2, v3;
	AbstractVertex next;
	final VertexFormatElement.Usage element;

	public AbstractVertex(VertexFormatElement.Usage e, float value0, float value1, float value2, float value3) {
		this.element = e;
		v0 = value0;
		v1 = value1;
		v2 = value2;
		v3 = value3;
	}

	public float[] getData() {
		return new float[] {v0, v1, v2, v3};
	}

	public AbstractVertex getNext() {
		return next;
	}

}
