package dhyces.dinnerplate.fluid;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.ForgeFlowingFluid;

import java.util.function.Supplier;

public abstract class StewFluid extends ForgeFlowingFluid {

    protected StewFluid(Supplier<? extends FluidType> fluidType, Supplier<? extends Fluid> source, Supplier<? extends Fluid> flowing, Supplier<? extends LiquidBlock> block, Supplier<? extends Item> bucket) {
        super(new Properties(fluidType, source, flowing).block(block).bucket(bucket).tickRate(20));
    }

    public static class Source extends StewFluid {

        public Source(Supplier<? extends FluidType> fluidType, Supplier<? extends Fluid> source, Supplier<? extends Fluid> flowing, Supplier<? extends LiquidBlock> block, Supplier<? extends Item> bucket) {
            super(fluidType, source, flowing, block, bucket);
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

        public Flowing(Supplier<? extends FluidType> fluidType, Supplier<? extends Fluid> source, Supplier<? extends Fluid> flowing, Supplier<? extends LiquidBlock> block, Supplier<? extends Item> bucket) {
            super(fluidType, source, flowing, block, bucket);
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