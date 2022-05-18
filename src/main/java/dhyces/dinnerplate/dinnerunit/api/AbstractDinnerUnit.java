package dhyces.dinnerplate.dinnerunit.api;

public abstract class AbstractDinnerUnit<T> implements IDinnerUnit<T> {

    protected T unit;

    public AbstractDinnerUnit(T wrappedStack) {
        unit = wrappedStack;
    }

    @Override
    public T getUnderlyingStack() {
        return unit;
    }
}
