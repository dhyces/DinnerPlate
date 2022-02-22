package dhyces.dinnerplate.dinnerunit.api;

import net.minecraft.world.entity.player.Player;

public interface IDinnerUnitHolder {

	public int getMaxUnits();

	public int getFilledUnits();

	public IDinnerUnit<?> getDinnerUnit(int slot);

	public IDinnerUnit<?> getLastUnit();

	public IDinnerUnit<?> removeLastUnit();

	public IDinnerUnit<?> insertUnit(IDinnerUnit<?> unit);

	//public boolean hasUnit();

	public boolean canTake(Player player);
}
