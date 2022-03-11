package dhyces.dinnerplate.util;

import net.minecraft.util.Mth;

public class Interpolation {

	private float previous;
	private float goal;
	protected Chaser chaser;


	public Interpolation(float goal) {
		this(0, goal);
	}

	public Interpolation(float from, float goal) {
		this(from, goal, (c, p, v) -> Mth.lerp(c, p, v));
	}

	public Interpolation(float from, float goal, Chaser chaser) {
		this.goal = goal;
		this.chaser = chaser;
	}

	public float updateChase(float partial) {
		if (Mth.equal(previous, goal)) {
			return this.goal;
		}
		var prev = this.previous;
		this.previous = chaser.chase(partial, previous, goal);
		System.out.println(previous);
		return prev;
	}

	public float getPrevious() {
		return this.previous;
	}

	public void setVals(float from, float goal) {
		previous = from;
		this.goal = goal;
		System.out.println("we are set: " + this);
	}

	@Override
	public String toString() {
		return "Goal: " + goal + ", Previous: " + previous;
	}

	@FunctionalInterface
	public static interface Chaser {

		public float chase(float chaseAmount, float prev, float val);
	}
}
