package dhyces.dinnerplate.fluid;

import dhyces.dinnerplate.registry.FluidTypeRegistry;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.fluids.ForgeFlowingFluid;

import java.util.function.Supplier;

public abstract class StewFluid extends ForgeFlowingFluid {

    protected StewFluid(int color, Supplier<? extends Fluid> source, Supplier<? extends Fluid> flowing, Supplier<LiquidBlock> block, Supplier<Item> bucket) {
        super(new Properties(FluidTypeRegistry.STEW_FLUID_TYPE, source, flowing));
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