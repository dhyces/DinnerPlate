package dhyces.dinnerplate.util;

public class LoosePair<T, R> {

    public T first;
    public R second;

    protected LoosePair(T t, R r) {
        this.first = t;
        this.second = r;
    }

    public static <Q, Z> LoosePair<Q, Z> of(Q first, Z second) {
        return new LoosePair<>(first, second);
    }

}
