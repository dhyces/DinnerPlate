package dhyces.dinnerplate.blockentity;

import dhyces.dinnerplate.blockentity.api.AbstractDinnerBlockEntity;
import dhyces.dinnerplate.registry.BEntityRegistry;
import dhyces.dinnerplate.util.FluidHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;

public class MeasuringCupBlockEntity extends AbstractDinnerBlockEntity {

	protected ListenedTank tank = new ListenedTank(FluidHelper.BUCKET);
	private final LazyOptional<IFluidHandler> tankLazy = LazyOptional.of(() -> tank);

	public MeasuringCupBlockEntity(BlockPos pWorldPosition, BlockState pBlockState) {
		super(BEntityRegistry.MEASURING_CUP_ENTITY.get(), pWorldPosition, pBlockState);
	}

	@Override
	public void write(CompoundTag tag) {
		if (!tank.isEmpty())
			tank.writeToNBT(tag);
	}

	@Override
	public void read(CompoundTag tag) {
		tank.readFromNBT(tag);
	}

	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
		if ((side == null || side.equals(Direction.UP)) && cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
			return tankLazy.cast();
		return super.getCapability(cap, side);
	}

	class ListenedTank extends FluidTank {

		public ListenedTank(int capacity) {
			super(capacity);
		}

		@Override
		protected void onContentsChanged() {
			MeasuringCupBlockEntity.this.setChanged();
		}

	}
}
