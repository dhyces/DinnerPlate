package dhyces.dinnerplate.blockentity;

import dhyces.dinnerplate.Constants;
import dhyces.dinnerplate.bite.IBitable;
import dhyces.dinnerplate.bite.IBitableItem;
import dhyces.dinnerplate.blockentity.api.AbstractDinnerBlockEntity;
import dhyces.dinnerplate.blockentity.api.IDishware;
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

public class PlateBlockEntity extends AbstractDinnerBlockEntity implements IDishware, ISingleItemHolder, IItemHandler {

	private ItemStack platedItem = ItemStack.EMPTY;
	private Supplier<Boolean> isFood = () -> !platedItem.isEmpty() && (platedItem.isEdible() || platedItem.getItem() instanceof IBitable);

	public PlateBlockEntity(BlockPos pWorldPosition, BlockState pBlockState) {
		super(BEntityRegistry.PLATE_ENTITY.get(), pWorldPosition, pBlockState);
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
	public boolean hasItem() {
		return !platedItem.isEmpty();
	}

	@Override
	public void read(CompoundTag pTag) {
		this.platedItem = ItemStack.of(pTag.getCompound(Constants.SINGLE_ITEM_TAG));
	}

	@Override
	public void write(CompoundTag tag) {
		if (!platedItem.isEmpty())
			tag.put(Constants.SINGLE_ITEM_TAG, platedItem.serializeNBT());
	}

	@Override
	public boolean containsItemStack(ItemStack stack) {
		return ItemStack.isSame(stack, platedItem);
	}

	@Override
	public boolean containsItem(Item item) {
		return platedItem.is(item);
	}

	@Override
	public ItemStack getLastItem() {
		return platedItem;
	}

	@Override
	public ItemStack removeLastItem() {
		var item = platedItem;
		setItem(ItemStack.EMPTY);
		return item;
	}

	@Override
	public void setItem(ItemStack stack) {
		this.platedItem = stack;
		setChanged();
	}

	@Override
	public int getContainerSize() {
		return 1;
	}

	@Override
	public boolean isEmpty() {
		return platedItem.isEmpty();
	}

	@Override
	public ItemStack getItem(int pIndex) {
		return platedItem;
	}

	@Override
	public ItemStack removeItem(int pIndex, int pCount) {
		var copy = platedItem.copy();
		var count = Math.min(copy.getCount(), pCount);
		copy.setCount(count);
		platedItem.shrink(count);
		setChanged();
		return copy;
	}

	@Override
	public ItemStack removeItemNoUpdate(int pIndex) {
		var stackCopy = platedItem.copy();
		platedItem = ItemStack.EMPTY;
		return stackCopy;
	}

	@Override
	public void setItem(int pIndex, ItemStack pStack) {
		setChanged();
	}

	@Override
	public int getItemSize() {
		return 1;
	}

	@Override
	public boolean stillValid(Player pPlayer) {
		return true;
	}

	@Override
	public void clearContent() {
		platedItem = ItemStack.EMPTY;
		setChanged();
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
		return insertItem(stack);
	}

	@Override
	public ItemStack extractItem(int slot, int amount, boolean simulate) {
		if (!simulate)
			return removeLastItem();
		return getLastItem();
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
