package dhyces.dinnerplate.util;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;

import java.util.stream.Stream;

public final class FluidHelper {

    public static final int BUCKET = 1000;
    public static final int PILE = 100;

    public static FluidStack getFluidStack(ItemStack itemStack) {
        var contained = FluidUtil.getFluidContained(itemStack);
        if (!contained.isEmpty()) {
            return contained.get();
        } else if (itemStack.is(Items.POTION)) {
            var p = PotionUtils.getPotion(itemStack);
            if (p.equals(Potions.WATER)) {
                return new FluidStack(Fluids.WATER, PILE);
            }
        }
        return FluidStack.EMPTY;
    }

    public static FluidAction clientAction(boolean isClient) {
        return isClient ? FluidAction.SIMULATE : FluidAction.EXECUTE;
    }

    public static int fill(IFluidHandler toFill, IFluidHandler toDrain, FluidAction action) {
        var testFill = toFill.fill(toDrain.drain(Integer.MAX_VALUE, FluidAction.SIMULATE).copy(), FluidAction.SIMULATE);
        return toFill.fill(toDrain.drain(testFill, action).copy(), action);
    }

    public static int fill(IFluidHandler toFill, IFluidHandler toDrain, FluidAction actionFill, FluidAction actionDrain) {
        var testFill = toFill.fill(toDrain.drain(Integer.MAX_VALUE, FluidAction.SIMULATE).copy(), FluidAction.SIMULATE);
        return toFill.fill(toDrain.drain(testFill, actionDrain).copy(), actionFill);
    }

    public static int simFill(IFluidHandler toFill, IFluidHandler toDrain) {
        var test = toDrain.drain(Integer.MAX_VALUE, FluidAction.SIMULATE);
        if (!test.isEmpty()) {
            var cap = compatibleCapacity(toFill, test);
            var drained = toDrain.drain(cap, FluidAction.SIMULATE);
            return toFill.fill(drained, FluidAction.SIMULATE);
        }
        return 0;
    }

    public static int compatibleCapacity(IFluidHandler handler, FluidStack test) {
        int max = 0, contained = 0;
        for (int tank = 0; tank < handler.getTanks(); tank++) {
            var fluid = handler.getFluidInTank(tank);
            max += fluid.isFluidEqual(test) || fluid.isEmpty() ? handler.getTankCapacity(tank) : 0;
            contained += fluid.isFluidEqual(test) ? fluid.getAmount() : 0;
        }
        return max - contained;
//		return Util.streamOf(c -> {
//								return handler.getFluidInTank(c).isFluidEqual(test) ? handler.getTankCapacity(c) : 0;
//							}, handler.getTanks()).mapToInt(c -> c).sum() -
//				tankStream(handler).filter(test::isFluidEqual).mapToInt(FluidStack::getAmount).sum();
    }

    public static Stream<FluidStack> tankStream(IFluidHandler handler) {
        return Util.streamOf(handler::getFluidInTank, handler.getTanks());
    }

    public static boolean isEmpty(IFluidHandlerItem fluidHandler) {
        var stream = tankStream(fluidHandler);
        return stream.allMatch(FluidStack::isEmpty);
    }

    public static FluidStack copyStackWithSize(FluidStack stack, int amount) {
        var copy = stack.copy();
        copy.setAmount(amount);
        return copy;
    }

    public static FluidStack returnStackWithSize(FluidStack stack, int amount) {
        stack.setAmount(amount);
        return stack;
    }
}
