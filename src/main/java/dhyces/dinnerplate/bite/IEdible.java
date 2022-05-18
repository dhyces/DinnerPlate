package dhyces.dinnerplate.bite;

import net.minecraft.world.entity.LivingEntity;

public interface IEdible<T> {

	public boolean isFast(T bitable, LivingEntity entity);

	public boolean isMeat(T bitable, LivingEntity entity);

	public boolean canAlwaysEat(T bitable, LivingEntity entity);
}
