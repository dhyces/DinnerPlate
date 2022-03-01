package dhyces.dinnerplate.blockentity;

import java.util.stream.Stream;

import dhyces.dinnerplate.Constants;
import dhyces.dinnerplate.blockentity.api.AbstractDinnerBlockEntity;
import dhyces.dinnerplate.blockentity.api.AbstractMixedBlockEntity;
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

public class MixingBowlBlockEntity extends AbstractMixedBlockEntity implements IWorkstation, IMixedInventory {

	private byte mixes = 0;

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
	public void craft() {

	}

	@Override
	public boolean hasRecipe() {
		return true;
	}
}
