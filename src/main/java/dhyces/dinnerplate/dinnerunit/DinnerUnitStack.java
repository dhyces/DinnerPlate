package dhyces.dinnerplate.dinnerunit;

import dhyces.dinnerplate.dinnerunit.api.IDinnerUnit;
import dhyces.dinnerplate.dinnerunit.api.IDinnerUnitHolder;
import net.minecraft.world.entity.player.Player;

public class DinnerUnitStack implements IDinnerUnitHolder {

    public DinnerUnitStack(int capacityIn) {

    }

    @Override
    public int getMaxUnits() {
        return 0;
    }

    @Override
    public int getFilledUnits() {
        return 0;
    }

    @Override
    public boolean canTake(Player player) {
        return false;
    }

    @Override
    public IDinnerUnit<?> getDinnerUnit(int slot) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public IDinnerUnit<?> getLastUnit() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public IDinnerUnit<?> removeLastUnit() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public IDinnerUnit<?> insertUnit(IDinnerUnit<?> unit) {
        // TODO Auto-generated method stub
        return null;
    }

}
