package dhyces.dinnerplate.blockentity;


import java.util.Optional;

import dhyces.dinnerplate.Constants;
import dhyces.dinnerplate.blockentity.api.AbstractMixedBlockEntity;
import dhyces.dinnerplate.blockentity.api.IRenderableTracker;
import dhyces.dinnerplate.blockentity.api.IWorkstation;
import dhyces.dinnerplate.capability.fluid.ListenedMultiFluidTank;
import dhyces.dinnerplate.inventory.api.IMixedInventory;
import dhyces.dinnerplate.registry.BEntityRegistry;
import dhyces.dinnerplate.util.Interpolation;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class MixingBowlBlockEntity extends AbstractMixedBlockEntity implements IWorkstation, IMixedInventory, IRenderableTracker,
																			   IFluidHandler, IItemHandler {

	private byte mixes = 0;
	public Interpolation renderedFluid = new Interpolation(1);
	private LazyOptional<IFluidHandler> lazyFluid = LazyOptional.of(() -> this);
	private LazyOptional<IItemHandler> lazyItem = LazyOptional.of(() -> this);

	public MixingBowlBlockEntity(BlockPos pWorldPosition, BlockState pBlockState) {
		super(BEntityRegistry.MIXING_BOWL_ENTITY.get(), pWorldPosition, pBlockState, 9);
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
		super.write(tag);
		if (mixes > 0)
			tag.putByte(Constants.TAG_MIX_STATE, mixes);
	}

	@Override
	public void read(CompoundTag tag) {
		super.read(tag);
		mixes = tag.getByte(Constants.TAG_MIX_STATE);
	}

	@Override
	public void readClient(CompoundTag tag) {
		var fluidPrev = getFluidAmount();
		super.readClient(tag);
		if (fluidPrev != getFluidAmount())
			renderedFluid.setVals(fluidPrev, getFluidAmount());
	}
	
	@Override
	public void craft() {

	}

	@Override
	public boolean hasRecipe() {
		return false;
	}

	@Override
	public float updateRenderable(String id, float partial) {
		return renderedFluid.updateChase(partial);
	}
	
	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
		if (cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
			return lazyFluid.cast();
		}
		if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return lazyItem.cast();
		}
		return super.getCapability(cap, side);
	}

	@Override
	public int getSlots() {
		return getItemSize();
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		return getItem(slot);
	}

	@Override
	public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
		if (slot < 0 || slot >= getSlots())
			return stack;
		if (simulate)
			return stack.copy().split(remaining());
		return inventory.insertItemAt(stack, slot);
	}

	@Override
	public ItemStack extractItem(int slot, int amount, boolean simulate) {
		if (slot < 0 || slot >= getSlots())
			return ItemStack.EMPTY;
		if (simulate)
			return getStackInSlot(slot).copy().split(amount);
		return removeItem(slot, amount);
	}

	@Override
	public int getSlotLimit(int slot) {
		return 1;
	}

	@Override
	public boolean isItemValid(int slot, ItemStack stack) {
		return true;
	}

	@Override
	public int getTanks() {
		return remaining();
	}

	@Override
	public FluidStack getFluidInTank(int tank) {
		return getFluidStack(tank);
	}

	@Override
	public int getTankCapacity(int tank) {
		return 100;
	}

	@Override
	public boolean isFluidValid(int tank, FluidStack stack) {
		return true;
	}

	@Override
	public int fill(FluidStack resource, FluidAction action) {
		if (resource.isEmpty())
			return 0;
		var maxFill = Math.min(remaining() * 100, resource.getAmount());
		if (action.execute() && maxFill > 0) {
			insertFluid(resource);
		}
		return maxFill;
	}

	@Override
	public FluidStack drain(FluidStack resource, FluidAction action) {
		if (resource.isEmpty())
			return resource;
		int i = 0;
		Optional<FluidStack> stack = Optional.empty();
		while (i < getTanks()) {
			var next = getFluidInTank(i);
			if (next.isFluidEqual(resource)) {
				stack = Optional.of(next);
				break;
			}
			i++;
		}
		var ret = stack.orElse(FluidStack.EMPTY);
		var maxDrain = Math.min(ret.getAmount(), resource.getAmount());
		if (action.execute()) {
			if (maxDrain == ret.getAmount())
				ret = removeFluid(i);
			else
				ret.setAmount(maxDrain);
		} else {
			ret = ret.copy();
			ret.setAmount(maxDrain);
		}
		return ret;
	}

	@Override
	public FluidStack drain(int maxDrain, FluidAction action) {
		if (maxDrain == 0)
			return FluidStack.EMPTY;
		var stack = getLastFluid();
		if (stack.isEmpty())
			return FluidStack.EMPTY;
		var trueMax = Math.min(stack.getAmount(), maxDrain);
		if (action.execute()) {
			if (trueMax == stack.getAmount())
				stack = removeLastFluid(trueMax);
			else
				stack.setAmount(trueMax);
		} else {
			stack = stack.copy();
			stack.setAmount(trueMax);
		}
		return stack;
	}
}
