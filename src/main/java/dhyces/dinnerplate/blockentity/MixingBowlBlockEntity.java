package dhyces.dinnerplate.blockentity;

import java.util.stream.Stream;

import dhyces.dinnerplate.Constants;
import dhyces.dinnerplate.blockentity.api.AbstractDinnerBlockEntity;
import dhyces.dinnerplate.blockentity.api.IWorkstation;
import dhyces.dinnerplate.dinnerunit.FlutemStack;
import dhyces.dinnerplate.inventory.MixedInventory;
import dhyces.dinnerplate.inventory.api.IMixedInventory;
import dhyces.dinnerplate.registry.BEntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;

public class MixingBowlBlockEntity extends AbstractDinnerBlockEntity implements IWorkstation, IMixedInventory {

	private MixedInventory inventory;
	private byte mixes = 0;

	public MixingBowlBlockEntity(BlockPos pWorldPosition, BlockState pBlockState) {
		super(BEntityRegistry.MIXING_BOWL_ENTITY.get(), pWorldPosition, pBlockState);
		inventory = new MixedInventory(9);
	}

	public boolean mix() {
		mixes++;
		// TODO: change max mixes to be set in the config
		var shouldCraft = mixes >= 3;
		if (shouldCraft)
			craft();
		return shouldCraft;
	}

	@Override
	public void write(CompoundTag tag) {
		var inv = inventory.serializeNBT();
		if (!inv.isEmpty())
			tag.merge(inv);
		if (mixes > 0)
			tag.putByte(Constants.TAG_MIX_STATE, mixes);
	}

	@Override
	public void read(CompoundTag tag) {
		inventory.deserializeNBT(tag);
		mixes = tag.getByte(Constants.TAG_MIX_STATE);
	}

	@Override
	public void craft() {

	}

	@Override
	public boolean hasRecipe() {
		return true;
	}

	@Override
	public boolean hasItem() {
		return inventory.hasItem();
	}

	@Override
	public boolean containsItemStack(ItemStack stack) {
		return inventory.containsItemStack(stack);
	}

	@Override
	public boolean containsItem(Item item) {
		return inventory.containsItem(item);
	}

	@Override
	public ItemStack getLastItem() {
		return inventory.getLastItem();
	}

	@Override
	public ItemStack removeLastItem() {
		var ret = inventory.removeLastItem();
		if (!ret.isEmpty())
			setChanged();
		return ret;
	}

	@Override
	public ItemStack insertItem(ItemStack stack) {
		var ret = inventory.insertItem(stack);
		if (!stack.equals(ret))
			setChanged();
		return ret;
	}

	@Override
	public int getContainerSize() {
		return inventory.getContainerSize();
	}

	@Override
	public boolean isEmpty() {
		return inventory.isEmpty();
	}

	@Override
	public ItemStack getItem(int pIndex) {
		return inventory.getItem(pIndex);
	}

	@Override
	public ItemStack removeItem(int pIndex, int pCount) {
		var ret = inventory.removeItem(pIndex, pCount);
		if (!ret.isEmpty())
			setChanged();
		return ret;
	}

	@Override
	public ItemStack removeItemNoUpdate(int pIndex) {
		return inventory.removeItemNoUpdate(pIndex);
	}

	@Override
	public void setItem(int pIndex, ItemStack pStack) {
		inventory.setItem(pIndex, pStack);
		setChanged();
	}

	@Override
	public boolean stillValid(Player pPlayer) {
		return inventory.stillValid(pPlayer);
	}

	@Override
	public void clearContent() {
		inventory.clearContent();
	}

	@Override
	public boolean hasFluid() {
		return inventory.hasFluid();
	}

	@Override
	public boolean containsFluidStack(FluidStack stack) {
		return inventory.containsFluidStack(stack);
	}

	@Override
	public boolean containsFluid(Fluid fluid) {
		return inventory.containsFluid(fluid);
	}

	@Override
	public FluidStack getLastFluid() {
		return inventory.getLastFluid();
	}

	@Override
	public FluidStack getFluidStack(int index) {
		return inventory.getFluidStack(index);
	}

	@Override
	public FluidStack removeLastFluid(int capacity) {
		var ret = inventory.removeLastFluid(capacity);
		if (!ret.isEmpty())
			setChanged();
		return ret;
	}

	@Override
	public FluidStack insertFluid(FluidStack stack) {
		var ret = inventory.insertFluid(stack);
		if (!stack.equals(ret))
			setChanged();
		return ret;
	}

	@Override
	public FluidStack setFluid(int index, FluidStack stack) {
		var ret = inventory.setFluid(index, stack);
		setChanged();
		return ret;
	}

	@Override
	public Stream<FlutemStack> mixedStream() {
		return inventory.mixedStream();
	}

	@Override
	public int getUsed() {
		return inventory.getUsed();
	}

	@Override
	public int getFluidSize() {
		return inventory.getFluidSize();
	}

	@Override
	public int getFluidSizeScaled() {
		return inventory.getFluidSizeScaled();
	}

	@Override
	public int getItemSize() {
		return inventory.getItemSize();
	}
}
