package dhyces.dinnerplate.blockentity;

import java.util.Optional;
import java.util.function.Supplier;

import dhyces.dinnerplate.Constants;
import dhyces.dinnerplate.bite.IBitable;
import dhyces.dinnerplate.blockentity.api.AbstractDinnerBlockEntity;
import dhyces.dinnerplate.blockentity.api.IDishware;
import dhyces.dinnerplate.blockentity.api.ISingleItemHolder;
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
		if (platedItem.isEdible())
		if (platedItem.getItem() instanceof IBitable e) {
			e.incrementBiteCount(platedItem);
			if (e.isFast(platedItem)) {
				//platedItem.finishUsingItem(level, player);
				player.eat(this.level, platedItem);
				consumeItem();
				return;
			}
			var bite = e.getBite(platedItem, e.getBiteCount(platedItem));
			if (!isMockFood())
				FoodHelper.playerStaticEatBite(level, player, platedItem, bite);
			else
				FoodHelper.playerStaticEatBite(level, player, getMockFood().get().getRealStack(), bite);
			setChanged();
		}
	}

	@Override
	public void eat(Player player) {
		if (isFood.get()) {
			bite(player);
			FoodHelper.playerStaticEat(level, player, platedItem);
			consumeItem();
		}
	}

	private void consumeItem() {
		var mockFood = getMockFood();
		if (mockFood.isPresent())
			setItem(ItemHelper.returnedItem(mockFood.get().getRealStack()));
	}

	@Override
	public int getBiteCount() {
		return IBitable.bitable(platedItem).map(c -> c.getBiteCount(platedItem)).orElse(-1);
	}

	private boolean isMockFood() {
		return platedItem.is(ItemRegistry.MOCK_FOOD_ITEM.get());
	}

	public Optional<IMockFoodProvider> getMockFood() {
		return isMockFood() ? Optional.of(MockFoodItem.getCapabilityLowest(platedItem)) : Optional.empty();
	}

	@Override
	public boolean hasItem() {
		return !platedItem.isEmpty();
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
		var optional = getMockFood();
		if (optional.isPresent() && optional.get().getBiteCount() == 0) {
			item = optional.get().getRealStack();
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
