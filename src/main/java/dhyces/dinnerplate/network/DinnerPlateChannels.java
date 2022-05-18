package dhyces.dinnerplate.network;

import dhyces.dinnerplate.DinnerPlate;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

// TODO: maybe use this for capabilities, I'm not sure
public class DinnerPlateChannels {

    public static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry
            .newSimpleChannel(
                    new ResourceLocation(DinnerPlate.MODID, "main"),
                    () -> PROTOCOL_VERSION,
                    PROTOCOL_VERSION::equals,
                    PROTOCOL_VERSION::equals
            );

    public static void registerChannel() {
        var id = 0;
        // Capability sync
        INSTANCE.registerMessage(id,
                CapabilityMessage.class,
                (msg, buffer) -> msg.encode(buffer),
                (buffer) -> CapabilityMessage.decode(buffer),
                (msg, supplier) -> msg.handle(supplier));
    }
}
