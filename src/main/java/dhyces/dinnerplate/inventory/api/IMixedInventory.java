package dhyces.dinnerplate.inventory.api;

import dhyces.dinnerplate.dinnerunit.FlutemStack;

import java.util.stream.Stream;

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
