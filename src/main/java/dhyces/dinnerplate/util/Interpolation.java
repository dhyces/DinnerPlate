package dhyces.dinnerplate.util;

import java.util.function.Function;

import net.minecraft.util.Mth;

public class Interpolation {

	private float previous;
	private float value;
	private Chaser chaser;
	private Function<Float, Float> onUpdate;


	public Interpolation(float goal) {
		this(goal, (c, p, v) -> Mth.lerp(c, p, v));
	}

	public Interpolation(float goal, Chaser chaser) {
		this.value = goal;
		this.chaser = chaser;
		this.onUpdate = UPDATE_FUNCTION;
	}

	public float updateChase(float partial) {
		return onUpdate.apply(partial);
	}

	private final Function<Float, Float> IMMEDIATE_RETURN_FUNCTION =  (p) -> value;
	private final Function<Float, Float> UPDATE_FUNCTION = (p) -> {
		var prev = this.previous;
		if (Mth.equal(previous, value)) {
			this.onUpdate = IMMEDIATE_RETURN_FUNCTION;
		}
		this.previous = chaser.chase(p, previous, value);
		return prev;
	};

	@FunctionalInterface
	public static interface Chaser {

		public float chase(float chaseAmount, float prev, float val);
	}
}
