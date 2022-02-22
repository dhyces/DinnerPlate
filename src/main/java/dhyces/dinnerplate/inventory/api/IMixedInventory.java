package dhyces.dinnerplate.inventory.api;

import java.util.stream.Stream;

import dhyces.dinnerplate.blockentity.api.IFluidHolder;
import dhyces.dinnerplate.blockentity.api.IItemHolder;
import dhyces.dinnerplate.dinnerunit.FlutemStack;

public interface IMixedInventory extends IItemHolder, IFluidHolder {

	public Stream<FlutemStack> mixedStream();

	public int getUsed();

	public int getFluidSize();

	public int getFluidSizeScaled();

	public int getItemSize();

	public default int remaining() {
		return getContainerSize() - getUsed();
	}

	public default boolean isFull() {
		return getUsed() == getContainerSize();
	}

}
