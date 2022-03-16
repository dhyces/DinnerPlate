package dhyces.dinnerplate.inventory.api;

import java.util.stream.Stream;

import dhyces.dinnerplate.dinnerunit.FlutemStack;

public interface IMixedInventory extends IItemHolder, IFluidHolder {

	public Stream<FlutemStack> mixedStream();
	
	public int getUsed();

	public default int remaining() {
		return getContainerSize() - getUsed();
	}

	public default boolean isFull() {
		return getUsed() == getContainerSize();
	}

}
