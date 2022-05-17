package dhyces.dinnerplate.util;

import net.minecraft.util.Mth;

import java.util.function.Supplier;

public class Interpolation {

	private float previous;
	private Supplier<Float> goal;
	protected Chaser chaser;


	public Interpolation(Supplier<Float> goal) {
		this(0, goal);
	}

	public Interpolation(float from, Supplier<Float> goal) {
		this(from, goal, (c, p, v) -> Mth.lerp(c, p, v));
	}

	public Interpolation(float from, Supplier<Float> goal, Chaser chaser) {
		this.previous = from;
		this.goal = goal;
		this.chaser = chaser;
	}

	public float updateChase(float partial) {
		var goalResolved = goal.get();
		if (Mth.equal(previous, goalResolved)) {
			return this.previous = goalResolved;
		}
		var prev = this.previous;
		this.previous = chaser.chase(partial, previous, goalResolved);
		return prev;
	}

	public float getPrevious() {
		return this.previous;
	}

	public void setVals(float from, Supplier<Float> goal) {
		previous = from;
		this.goal = goal;
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
