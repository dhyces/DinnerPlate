package dhyces.dinnerplate.client.render.util;

import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;

public class RectPrism {

	private final Vec3[] points;

	public RectPrism(Vec3 start, Vec3 end) {
		this(start, end.x-start.x, end.y-start.y, end.z-start.z);
	}

	public RectPrism(Vec3 start, double width, double height, double length) {
		//length, height, width
		var c000 = start;
		var c001 = start.add(width, 0, 0);
		var c010 = start.add(0, height, 0);
		var c011 = c010.add(width, 0, 0);
		var c100 = c000.add(0, 0, length);
		var c101 = c100.add(width, 0, 0);
		var c110 = c100.add(0, height, 0);
		var c111 = c100.add(width, height, 0);
		points = new Vec3[] {c000, c001, c010, c011, c100, c101, c110, c111};
	}

	public static RectPrism.Builder from(double x, double y, double z) {
		return new Builder(x, y, z);
	}

	public static RectPrism.Builder fromPixel(double x, double y, double z) {
		return new Builder(x/16.0, y/16.0, z/16.0);
	}

//	   direction |   start & end  | indices | direction int |       all vertices       | all indices  | distance
//          down =   (c000, c101) -  (0, 5) -             0 - (c000, c001, c100, c101) - (0, 1, 4, 5) - 0,1,3,1,2
//            up =   (c010, c111) -  (2, 7) -             1 - (c010, c011, c110, c111) - (2, 3, 6, 7) - 2,1,3,1,0
//         north =   (c000, c011) -  (0, 3) -             2 - (c000, c001, c010, c011) - (0, 1, 2, 3) - 0,1,1,1,4
//         south =   (c100, c111) -  (4, 7) -             3 - (c100, c101, c110, c111) - (4, 5, 6, 7) - 4,1,1,1,0
//          west =   (c000, c110) -  (0, 6) -             4 - (c000, c010, c100, c110) - (0, 2, 4, 6) - 0,2,2,2,1
//          east =   (c001, c111) -  (1, 7) -             5 - (c001, c011, c101, c111) - (1, 3, 5, 7) - 1,2,2,2,0
	public QuadFace getVertices(Direction face) {
		var i = face.get3DDataValue();
		var a = (i & 0b100) >> 2;
		var a_not = a ^ 1;
		var b = (i & 0b010) >> 1;
		var b_not = b ^ 1;
		var c = i & 0b001;

		var a_and_c = a & c;
		var b_and_c = b & c;
		var a_not_or_c = a_not | c;
		var a_not_and_b_not_and_c = a_not & b_not & c;

		var A3 = a | b_not | c;
		var B3 = a | b | c;
		var C3 = a_not_or_c | b;
		var A2 = A3;
		var B2 = b | a_not_and_b_not_and_c;
		var C2 = a_and_c | (a & b);
		var A1 = b_and_c;
		var B1 = a | (b_not & c);
		var C1 = a_not_or_c;
		var A0 = b_and_c;
		var B0 = a_not_and_b_not_and_c;
		var C0 = a_and_c;
		var topRight = A3 << 2 | B3 << 1 | C3;
		var topLeft = A2 << 2 | B2 << 1 | C2;
		var bottomRight = A1 << 2 | B1 << 1 | C1;
		var bottomLeft = A0 << 2 | B0 << 1 | C0;
		return new QuadFace(points[bottomLeft], points[bottomRight], points[topLeft], points[topRight]);
	}

	@Override
	public String toString() {
		return "Start: " + this.points[0] + " End: " + this.points[this.points.length-1];
	}

	public static class Builder {
		Vec3 v;

		Builder(double x, double y, double z) {
			v = new Vec3(x, y, z);
		}

		public RectPrism to(double x, double y, double z) {
			return new RectPrism(v, new Vec3(x, y, z));
		}

		public RectPrism toPixel(double x, double y, double z) {
			return new RectPrism(v, new Vec3(x/16.0, y/16.0, z/16.0));
		}
	}
}
