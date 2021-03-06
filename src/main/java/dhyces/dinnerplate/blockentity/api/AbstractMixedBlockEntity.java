package dhyces.dinnerplate.blockentity.api;

import com.google.common.collect.ImmutableList;
import dhyces.dinnerplate.util.FlutemStack;
import dhyces.dinnerplate.inventory.MixedInventory;
import dhyces.dinnerplate.inventory.api.IMixedInventory;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;

import java.util.stream.Stream;

public abstract class AbstractMixedBlockEntity extends AbstractDinnerBlockEntity implements IMixedInventory {

    protected MixedInventory inventory;

    public AbstractMixedBlockEntity(BlockEntityType<?> pType, BlockPos pWorldPosition, BlockState pBlockState, int inventorySize) {
        super(pType, pWorldPosition, pBlockState);
        inventory = new MixedInventory(inventorySize);
    }

    @Override
    public void write(CompoundTag tag) {
        var inv = inventory.serializeNBT();
        if (!inv.isEmpty())
            tag.merge(inv);
    }

    @Override
    public void read(CompoundTag tag) {
        inventory.deserializeNBT(tag);
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
        if (!ItemStack.matches(stack, ret))
            setChanged();
        return ret;
    }

    @Override
    public ItemStack insertItemAt(ItemStack stack, int slot) {
        var ret = inventory.insertItemAt(stack, slot);
        if (!ItemStack.matches(stack, ret))
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
    public FluidStack removeFluid(int index) {
        var ret = inventory.removeFluid(index);
        if (!ret.isEmpty())
            setChanged();
        return ret;
    }

    @Override
    public FluidStack insertFluid(FluidStack stack) {
        var ret = inventory.insertFluid(stack);
        if (!stack.isFluidStackIdentical(ret))
            setChanged();
        return ret;
    }

    @Override
    public FluidStack insertFluidAt(FluidStack stack, int slot) {
        return null;
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

    public ImmutableList<FluidStack> getFluids() {
        return ImmutableList.copyOf(inventory.getFluids());
    }

    public ImmutableList<ItemStack> getItems() {
        return ImmutableList.copyOf(inventory.getItems());
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
    public int getFluidAmount() {
        return inventory.getFluidAmount();
    }

    @Override
    public int getFluidCapacity() {
        return 9;
    }

    @Override
    public int getItemSize() {
        return inventory.getItemSize();
    }
}
