package dhyces.dinnerplate.blockentity.api;

import net.minecraft.world.entity.player.Player;

public interface IDishware {

    void bite(Player player);

    boolean canBite(Player player);

    void eat(Player player);

    int getBiteCount();
}
