package dhyces.dinnerplate.dinnerunit.api;

import net.minecraft.world.entity.player.Player;

public interface IDinnerUnitHolder {

    int getMaxUnits();

    int getFilledUnits();

    IDinnerUnit<?> getDinnerUnit(int slot);

    IDinnerUnit<?> getLastUnit();

    IDinnerUnit<?> removeLastUnit();

    IDinnerUnit<?> insertUnit(IDinnerUnit<?> unit);

    //public boolean hasUnit();

    boolean canTake(Player player);
}
