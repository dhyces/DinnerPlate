package dhyces.dinnerplate.util;

import java.util.function.Function;

import net.minecraft.util.Mth;

public class Interpolation {

	private float previous;
	private float goal;
	protected Chaser chaser;
	protected Function<Float, Float> onUpdate;


	public Interpolation(float goal) {
		this(goal, (c, p, v) -> Mth.lerp(c, p, v));
	}

	public Interpolation(float goal, Chaser chaser) {
		this.goal = goal;
		this.chaser = chaser;
		this.onUpdate = UPDATE_FUNCTION;
	}

	public float updateChase(float partial) {
		return onUpdate.apply(partial);
	}
	
	public float getPrevious() {
		return this.previous;
	}
	
	public void reset() {
		previous = 0;
		onUpdate = UPDATE_FUNCTION;
	}

	private final Function<Float, Float> IMMEDIATE_RETURN_FUNCTION =  (p) -> goal;
	private final Function<Float, Float> UPDATE_FUNCTION = (p) -> {
		var prev = this.previous;
		if (Mth.equal(previous, goal)) {
			this.previous = this.goal;
			this.onUpdate = IMMEDIATE_RETURN_FUNCTION;
			return this.goal;
		}
		this.previous = chaser.chase(p, previous, goal);
		return prev;
	};

	@Override
	public String toString() {
		return "Goal: " + goal + ", Previous: " + previous;
	}
	
	@FunctionalInterface
	public static interface Chaser {

		public float chase(float chaseAmount, float prev, float val);
	}
}
