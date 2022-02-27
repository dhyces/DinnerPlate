package dhyces.dinnerplate.blockentity;

import java.util.Optional;
import java.util.function.Supplier;

import dhyces.dinnerplate.Constants;
import dhyces.dinnerplate.bite.IBitable;
import dhyces.dinnerplate.blockentity.api.AbstractDinnerBlockEntity;
import dhyces.dinnerplate.blockentity.api.IDishware;
import dhyces.dinnerplate.blockentity.api.ISingleItemHolder;
import dhyces.dinnerplate.capability.CapabilityEventSubscriber;
import dhyces.dinnerplate.capability.IMockFoodProvider;
import dhyces.dinnerplate.item.MockFoodItem;
import dhyces.dinnerplate.registry.BEntityRegistry;
import dhyces.dinnerplate.registry.ItemRegistry;
import dhyces.dinnerplate.util.FoodHelper;
import dhyces.dinnerplate.util.ItemHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public class PlateBlockEntity extends AbstractDinnerBlockEntity implements IDishware, ISingleItemHolder {

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
			platedItem = MockFoodItem.mockFoodStack(platedItem, 3);
		if (platedItem.getItem() instanceof IBitable e) {
			var ret = e.eat(platedItem, player, level);
			if (!ret.equals(platedItem)) {
				platedItem = ret;
			}
			setChanged();
		}
	}

	@Override
	public void eat(Player player) {
		if (isFood.get()) {
			bite(player);
		}
	}

	@Override
	public int getBiteCount() {
		return IBitable.bitable(platedItem).map(c -> c.getBiteCount(platedItem)).orElse(-1);
	}

	public Optional<IBitable> getBitable() {
		return IBitable.bitable(platedItem);
	}

	@Override
	public boolean hasItem() {
		return !platedItem.isEmpty();
	}

	@Override
	public void read(CompoundTag pTag) {
		this.platedItem = ItemStack.of(pTag.getCompound(Constants.TAG_SINGLE_ITEM));
		if (platedItem.getItem() == null)
			platedItem = ItemStack.EMPTY;
	}

	@Override
	public void write(CompoundTag tag) {
		if (!platedItem.isEmpty())
			tag.put(Constants.TAG_SINGLE_ITEM, platedItem.serializeNBT());
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
		var optional = platedItem.getCapability(CapabilityEventSubscriber.MOCK_FOOD_CAPABILITY);
		if (optional.isPresent() && optional.resolve().get().getBiteCount() == 0) {
			item = optional.resolve().get().getRealStack();
		}
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
	public boolean stillValid(Player pPlayer) {
		return true;
	}

	@Override
	public void clearContent() {
		platedItem = ItemStack.EMPTY;
		setChanged();
	}
}
