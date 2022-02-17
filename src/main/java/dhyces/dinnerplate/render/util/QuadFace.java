package dhyces.dinnerplate.render.util;

import net.minecraft.world.phys.Vec3;

public class QuadFace {

	private final Vec3[] vertices;
	private int startIndex = 0;
	
	public QuadFace(Vec3 f00, Vec3 f01, Vec3 f10, Vec3 f11) {
		vertices = new Vec3[] {f00, f01, f10, f11};
	}
	
	public Vec3 bottomLeft() {
		return vertices[startIndex];
	}
	
	public Vec3 bottomRight() {
		return vertices[cycleIndex(1)];
	}
	
	public Vec3 topLeft() {
		return vertices[cycleIndex(2)];
	}
	
	public Vec3 topRight() {
		return vertices[cycleIndex(3)];
	}
	
	private int cycleIndex(int cycles) {
		if (cycles < 0)
			cycles = 3 * (cycles * -1);
		return (startIndex + cycles) % 4;
	}
	
	public void rotate(int rotations) {
		startIndex = cycleIndex(rotations);
	}
	
	public void swap(int vert1, int vert2) {
		var t = vertices[cycleIndex(vert1)];
		vertices[cycleIndex(vert1)] = vertices[cycleIndex(vert2)];
		vertices[cycleIndex(vert2)] = t;
	}
	
	@Override
	public String toString() {
		return "bottomLeft: " + bottomLeft() + ", bottomRight: " + bottomRight() + ", topLeft: " + topLeft() + ", topRight: " + topRight();
	}
}
