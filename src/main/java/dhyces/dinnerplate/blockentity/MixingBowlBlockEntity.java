package dhyces.dinnerplate.blockentity;

import dhyces.dinnerplate.Constants;
import dhyces.dinnerplate.blockentity.api.AbstractInventoryBlockEntity;
import dhyces.dinnerplate.blockentity.api.IRenderableTracker;
import dhyces.dinnerplate.blockentity.api.IWorkstation;
import dhyces.dinnerplate.capability.mixed.MixedCapability;
import dhyces.dinnerplate.inventory.MixedInventory;
import dhyces.dinnerplate.recipe.MixingRecipe;
import dhyces.dinnerplate.recipe.ResultState;
import dhyces.dinnerplate.registry.BEntityRegistry;
import dhyces.dinnerplate.util.Interpolation;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class MixingBowlBlockEntity extends AbstractInventoryBlockEntity<MixedInventory> implements IWorkstation, IRenderableTracker {

    @OnlyIn(Dist.CLIENT)
    Interpolation renderedFluid = new Interpolation(() -> (float) inventory.getFluidAmount());
    private byte mixes = 0;
    private MixingRecipe recipe;
    private final MixedCapability cap = new MixedCapability(this.inventory);
    private final LazyOptional<IFluidHandler> lazyFluid = LazyOptional.of(() -> cap);
    private final LazyOptional<IItemHandler> lazyItem = LazyOptional.of(() -> cap);

    public MixingBowlBlockEntity(BlockPos pWorldPosition, BlockState pBlockState) {
        super(BEntityRegistry.MIXING_BOWL_ENTITY.get(), pWorldPosition, pBlockState, new MixedInventory(9));
    }

    public boolean mix(Level level) {
        var recipe = level.getRecipeManager().getRecipeFor(MixingRecipe.MIXING_TYPE, this.inventory, level);
        if (recipe.isPresent()) {
            this.recipe = recipe.get();
            mixes++;
        } else {
            mixes = 0;
        }
        // TODO: change max mixes to be set in the config
        if (mixes >= 9) {
            mixes = 0;
            if (hasRecipe())
                craft();
        }
        return mixes >= 9;
    }

    @Override
    public void setChanged() {
        super.setChanged();
        this.recipe = null;
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
    public void craft() {
        if (recipe.getResultState() == ResultState.ITEM) {
            var item = recipe.assemble(inventory);
            
        }
        inventory.clearContent();
        switch (recipe.getResultState()) {
            case ITEM -> inventory.insertItem(recipe.assemble(inventory));
            case FLUID -> inventory.insertFluid(recipe.assembleFluid(inventory));
        }
    }

    @Override
    public boolean hasRecipe() {
        return recipe != null;
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
}
