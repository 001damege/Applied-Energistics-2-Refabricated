package appeng.core.network;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public class InitNetwork {
    public static void init() {
    }

    private static <T extends CustomAppEngPayload & ServerboundPacket<T>> void serverbound(CustomPacketPayload.Type<T> type, StreamCodec<RegistryFriendlyByteBuf, T> streamCodec) {
        PayloadTypeRegistry.playS2C().register(type, streamCodec);
        ServerPlayNetworking.registerGlobalReceiver(type, (payload, context) -> payload.receive(payload, context));
    }

    private static <T extends CustomAppEngPayload & ClientboundPacket<T>> void clientbound(CustomPacketPayload.Type<T> type, StreamCodec<FriendlyByteBuf, T> streamCodec) {
        PayloadTypeRegistry.playC2S().register(type, streamCodec);
        ClientPlayNetworking.registerGlobalReceiver(type, (payload, context) -> payload.receive(payload, context));
    }

    private static <T extends CustomAppEngPayload & ServerboundPacket<T> & ClientboundPacket<T>> void bidirectional(CustomPacketPayload.Type<T> type, StreamCodec<FriendlyByteBuf, T> streamCodec) {
        PayloadTypeRegistry.playS2C().register(type, streamCodec);
        PayloadTypeRegistry.playC2S().register(type, streamCodec);
        ServerPlayNetworking.registerGlobalReceiver(type, (payload, context) -> payload.receive(payload, context));
        ClientPlayNetworking.registerGlobalReceiver(type, (payload, context) -> payload.receive(payload, context));
    }
}
