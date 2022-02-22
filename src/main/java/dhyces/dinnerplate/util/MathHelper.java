package dhyces.dinnerplate.util;

public class MathHelper {

	/** returns x + y - max if it's a positive integer*/
	public static int additionOverflow(int value, int max) {
		var ret = value - max;
		return ret > 0 ? ret : 0;
	}

}
