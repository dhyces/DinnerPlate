package dhyces.dinnerplate.bite;

public interface IEdible<T> {

	public boolean isFast(T bitable);

	public boolean isMeat(T bitable);

	public boolean canAlwaysEat(T bitable);
}
