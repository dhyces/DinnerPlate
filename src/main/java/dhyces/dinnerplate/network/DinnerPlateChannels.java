package dhyces.dinnerplate.network;

import dhyces.dinnerplate.DinnerPlate;
import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.function.Function;

public final class DinnerPlateChannels {

    public static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel MAIN_CHANNEL = NetworkRegistry
            .newSimpleChannel(
                    new ResourceLocation(DinnerPlate.MODID, "main"),
                    () -> PROTOCOL_VERSION,
                    PROTOCOL_VERSION::equals,
                    PROTOCOL_VERSION::equals
            );
    private static int ID = 0;

    public static void init() {

    }

    public static <T extends SimplePacketHandler<T>> void sendMessageToPlayer(ServerPlayer serverPlayer, T packetHandler) {
        MAIN_CHANNEL.send(PacketDistributor.PLAYER.with(() -> serverPlayer), packetHandler);
    }

    public static FriendlyByteBuf unpooled() {
        return new FriendlyByteBuf(Unpooled.buffer());
    }

    private static <T extends SimplePacketHandler<T>> void registerMessage(Class<T> simplePacketHandler, Function<FriendlyByteBuf, T> factory) {
        MAIN_CHANNEL.registerMessage(ID++, simplePacketHandler, SimplePacketHandler::encoder, factory::apply, SimplePacketHandler::messageConsumer);
    }
}
