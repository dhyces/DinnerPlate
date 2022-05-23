package dhyces.dinnerplate.blockentity;

import dhyces.dinnerplate.Constants;
import dhyces.dinnerplate.bite.IBitable;
import dhyces.dinnerplate.bite.IBitableItem;
import dhyces.dinnerplate.blockentity.api.AbstractDinnerBlockEntity;
import dhyces.dinnerplate.blockentity.api.AbstractInventoryBlockEntity;
import dhyces.dinnerplate.blockentity.api.IDishware;
import dhyces.dinnerplate.inventory.SingleItemInventory;
import dhyces.dinnerplate.inventory.api.ISingleItemHolder;
import dhyces.dinnerplate.item.MockFoodItem;
import dhyces.dinnerplate.registry.BEntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.IItemHandler;

import java.util.Optional;
import java.util.function.Supplier;

public class PlateBlockEntity extends AbstractInventoryBlockEntity<SingleItemInventory> implements IDishware, IItemHandler {

	private ItemStack platedItem = ItemStack.EMPTY;
	private Supplier<Boolean> isFood = () -> !platedItem.isEmpty() && (platedItem.isEdible() || platedItem.getItem() instanceof IBitable);

	public PlateBlockEntity(BlockPos pWorldPosition, BlockState pBlockState) {
		super(BEntityRegistry.PLATE_ENTITY.get(), pWorldPosition, pBlockState, new SingleItemInventory());
	}

	/** Chew is affected by fast foods.*/
	@Override
	public void bite(Player player) {
		if (platedItem.isEdible() && !(platedItem.getItem() instanceof IBitable) && !(platedItem.getItem() instanceof MockFoodItem))
			// TODO: Try to change this to get a value from some map in case someone wants to change the number of bites it takes to eat. I'm
			// just worried about exceeding the number of tags in an item
			platedItem = MockFoodItem.mockFoodStack(platedItem, player, 3);
		if (platedItem.getItem() instanceof IBitableItem e) {
			var ret = e.eat(platedItem, player, level);
			if (!ret.equals(platedItem)) {
				platedItem = ret;
			}
			setChanged();
		}
	}
	
	@Override
	public boolean canBite(Player player) {
		return platedItem.isEdible() && player.canEat(getBitable().map(c -> c.canAlwaysEat(platedItem,  player)).orElse(platedItem.getItem().getFoodProperties().canAlwaysEat()));
	}

	@Override
	public void eat(Player player) {
		if (isFood.get()) {
			bite(player);
		}
	}

	@Override
	public int getBiteCount() {
		return IBitableItem.bitable(platedItem).map(c -> c.getBiteCount(platedItem)).orElse(-1);
	}

	public Optional<IBitableItem> getBitable() {
		return IBitableItem.bitable(platedItem);
	}

	@Override
	public void read(CompoundTag pTag) {
		this.platedItem = ItemStack.of(pTag.getCompound(Constants.TAG_SINGLE_ITEM));
	}

	@Override
	public void write(CompoundTag tag) {
		if (!platedItem.isEmpty())
			tag.put(Constants.TAG_SINGLE_ITEM, platedItem.serializeNBT());
	}

	@Override
	public int getSlots() {
		return 1;
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		return platedItem;
	}

	@Override
	public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
		return getInventory().insertItem(stack);
	}

	@Override
	public ItemStack extractItem(int slot, int amount, boolean simulate) {
		if (!simulate)
			return getInventory().removeLastItem();
		return getInventory().getLastItem();
	}

	@Override
	public int getSlotLimit(int slot) {
		return 1;
	}

	@Override
	public boolean isItemValid(int slot, ItemStack stack) {
		return true;
	}
}
