package dhyces.dinnerplate.inventory.api;

import dhyces.dinnerplate.util.FlutemStack;

import java.util.stream.Stream;

public interface IMixedInventory extends IItemHolder, IFluidHolder {

    Stream<FlutemStack> mixedStream();

    int getUsed();

    default int remaining() {
        return getContainerSize() - getUsed();
    }

    default boolean isFull() {
        return getUsed() == getContainerSize();
    }

}
