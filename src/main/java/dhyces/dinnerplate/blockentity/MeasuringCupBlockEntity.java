package dhyces.dinnerplate.blockentity;

import dhyces.dinnerplate.Constants;
import dhyces.dinnerplate.blockentity.api.AbstractDinnerBlockEntity;
import dhyces.dinnerplate.blockentity.api.IRenderableTracker;
import dhyces.dinnerplate.inventory.api.IFluidHolder;
import dhyces.dinnerplate.registry.BEntityRegistry;
import dhyces.dinnerplate.util.FluidHelper;
import dhyces.dinnerplate.util.Interpolation;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;
import net.minecraftforge.fluids.capability.templates.FluidTank;

public class MeasuringCupBlockEntity extends AbstractDinnerBlockEntity implements IFluidHolder, IRenderableTracker {

    protected ListenedTank tank = new ListenedTank(FluidHelper.BUCKET);
    private final LazyOptional<IFluidHandler> tankLazy = LazyOptional.of(() -> tank);
    Interpolation renderedFluid = new Interpolation(() -> (float) getFluidAmount());

    public MeasuringCupBlockEntity(BlockPos pWorldPosition, BlockState pBlockState) {
        super(BEntityRegistry.MEASURING_CUP_ENTITY.get(), pWorldPosition, pBlockState);
    }

    @Override
    public void write(CompoundTag tag) {
        if (!tank.isEmpty()) {
            var tTag = new CompoundTag();
            tank.writeToNBT(tTag);
            tag.put(Constants.SINGLE_FLUID_TAG, tTag);
        }
    }

    @Override
    public void read(CompoundTag tag) {
        tank.readFromNBT(tag.getCompound(Constants.SINGLE_FLUID_TAG));
    }

    @Override
    public boolean hasFluid() {
        return !tank.isEmpty();
    }

    @Override
    public boolean containsFluidStack(FluidStack stack) {
        return tank.getFluid().isFluidEqual(stack);
    }

    @Override
    public boolean containsFluid(Fluid fluid) {
        return tank.getFluid().getFluid().isSame(fluid);
    }

    @Override
    public FluidStack getLastFluid() {
        return tank.getFluid();
    }

    @Override
    public FluidStack getFluidStack(int index) {
        return getLastFluid();
    }

    @Override
    public FluidStack removeFluid(int index) {
        return removeLastFluid(tank.getCapacity());
    }

    @Override
    public FluidStack removeLastFluid(int capacity) {
        return tank.drain(capacity, FluidAction.EXECUTE);
    }

    @Override
    public FluidStack insertFluid(FluidStack stack) {
        var filled = tank.fill(stack, FluidAction.EXECUTE);
        stack.shrink(filled);
        return stack;
    }

    @Override
    public FluidStack insertFluidAt(FluidStack stack, int slot) {
        return insertFluid(stack);
    }

    @Override
    public FluidStack setFluid(int index, FluidStack stack) {
        var old = tank.getFluid();
        tank.setFluid(stack);
        return old;
    }

    @Override
    public int getFluidAmount() {
        return tank.getFluidAmount();
    }

    @Override
    public int getFluidSize() {
        return hasFluid() ? 1 : 0;
    }

    @Override
    public int getFluidCapacity() {
        return 1;
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        if ((side == null || side.equals(Direction.UP)) && cap == ForgeCapabilities.FLUID_HANDLER)
            return tankLazy.cast();
        return super.getCapability(cap, side);
    }

    @Override
    public float updateRenderable(String id, float partial) {
        return renderedFluid.updateChase(partial);
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
