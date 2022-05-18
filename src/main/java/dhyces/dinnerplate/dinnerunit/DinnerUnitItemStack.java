package dhyces.dinnerplate.dinnerunit;

import dhyces.dinnerplate.dinnerunit.api.AbstractDinnerUnit;
import net.minecraft.world.item.ItemStack;

public class DinnerUnitItemStack extends AbstractDinnerUnit<ItemStack> {

    public DinnerUnitItemStack(ItemStack wrappedStack) {
        super(wrappedStack);
    }

    @Override
    public int getAmount() {
        return unit.getCount();
    }
}
