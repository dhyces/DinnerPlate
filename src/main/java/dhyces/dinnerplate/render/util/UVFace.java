package dhyces.dinnerplate.render.util;

public class UVFace extends QuadFace {

	private double[] uVals;
	private double[] vVals;
	
	public UVFace(UVVertex f00, UVVertex f01, UVVertex f10, UVVertex f11) {
		super(f00, f01, f10, f11);
		uVals = new double[] {f00.getU(), f01.getU(), f10.getU(), f11.getU()};
		vVals = new double[] {f00.getV(), f01.getV(), f10.getV(), f11.getV()};
	}
	
	public double getU(int index) {
		return uVals[index];
	}
	
	public double getV(int index) {
		return vVals[index];
	}
}
