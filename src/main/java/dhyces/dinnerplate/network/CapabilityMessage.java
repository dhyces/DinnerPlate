package dhyces.dinnerplate.network;

import dhyces.dinnerplate.capability.CapabilityEventSubscriber;
import dhyces.dinnerplate.capability.bitten.IMockFoodProvider;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record CapabilityMessage(LazyOptional<IMockFoodProvider> cap) {

    public static CapabilityMessage decode(FriendlyByteBuf buffer) {
        return new CapabilityMessage(buffer.readItem().getCapability(CapabilityEventSubscriber.MOCK_FOOD_CAPABILITY));
    }

    public void encode(FriendlyByteBuf buffer) {

    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() ->
                {
                }
        );
    }

}
