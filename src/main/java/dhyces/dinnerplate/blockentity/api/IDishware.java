package dhyces.dinnerplate.blockentity.api;

import net.minecraft.world.entity.player.Player;

public interface IDishware {

	public void bite(Player player);
	public boolean canBite(Player player);
	public void eat(Player player);
	public int getBiteCount();
}
