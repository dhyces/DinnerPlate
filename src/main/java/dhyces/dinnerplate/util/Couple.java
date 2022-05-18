package dhyces.dinnerplate.util;

import com.mojang.datafixers.util.Pair;

public class Couple<F> extends Pair<F, F> {

    public Couple(F first, F second) {
        super(first, second);
    }

    public static <F> Couple<F> coupleOf(F first, F second) {
        return new Couple<>(first, second);
    }

}
