package dhyces.dinnerplate.render.util;

import net.minecraft.world.phys.Vec3;

public class UVVertex extends Vec3 {
	
	private final double u;
	private final double v;

	public UVVertex(double pX, double pY, double pZ, double u, double v) {
		super(pX, pY, pZ);
		this.u = u;
		this.v = v;
	}
	
	public double getU() {
		return u;
	}

	public double getV() {
		return v;
	}
}
