package dhyces.dinnerplate.blockentity;


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
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;

public class MixingBowlBlockEntity extends AbstractMixedBlockEntity implements IWorkstation, IMixedInventory, IRenderableTracker {

	private byte mixes = 0;
	public Interpolation renderedFluid = new Interpolation(1);
	private LazyOptional<IFluidHandler> lazyFluid = LazyOptional.of(() -> new ListenedMultiFluidTank(this));

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
		var priorFluid = getFluidAmount();
		super.readClient(tag);
		if (getFluidAmount() != priorFluid)
			renderedFluid.setVals(priorFluid, getFluidAmount());
	}
	
	@Override
	public void craft() {

	}

	@Override
	public boolean hasRecipe() {
		return true;
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
			
		}
		return super.getCapability(cap, side);
	}
}
