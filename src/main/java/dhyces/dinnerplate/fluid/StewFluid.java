package dhyces.dinnerplate.fluid;

import java.util.function.Supplier;

import dhyces.dinnerplate.DinnerPlate;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.ForgeFlowingFluid;

public abstract class StewFluid extends ForgeFlowingFluid {

	protected StewFluid(int color, Supplier<? extends Fluid> source, Supplier<? extends Fluid> flowing, Supplier<LiquidBlock> block, Supplier<Item> bucket) {
		super(new Properties(source, 
							 flowing, 
							 FluidAttributes.builder(new ResourceLocation(DinnerPlate.MODID, "block/soup_still"), 
									 				 new ResourceLocation(DinnerPlate.MODID, "block/soup_flow"))
							 				.color(color)
							 				.density(3000)
							 				.viscosity(6000))
											.block(block)
											.bucket(bucket)
											.tickRate(20));
	}
	
	public static class Source extends StewFluid {
		
		public Source(int color, Supplier<? extends Fluid> source, Supplier<? extends Fluid> flowing, Supplier<LiquidBlock> block, Supplier<Item> bucket) {
			super(color, source, flowing, block, bucket);
		}

		@Override
		public int getAmount(FluidState pState) {
			return 8;
		}

		@Override
		public boolean isSource(FluidState pState) {
			return true;
		}
	}
	
	public static class Flowing extends StewFluid {
		
		public Flowing(int color, Supplier<? extends Fluid> source, Supplier<? extends Fluid> flowing, Supplier<LiquidBlock> block, Supplier<Item> bucket) {
			super(color, source, flowing, block, bucket);
			registerDefaultState(getStateDefinition().any().setValue(LEVEL, 7));
		}
		
		@Override
		protected void createFluidStateDefinition(Builder<Fluid, FluidState> pBuilder) {
			super.createFluidStateDefinition(pBuilder);
			pBuilder.add(LEVEL);
		}

		@Override
		public int getAmount(FluidState pState) {
			return pState.getValue(LEVEL);
		}

		@Override
		public boolean isSource(FluidState pState) {
			return false;
		}
	}
}