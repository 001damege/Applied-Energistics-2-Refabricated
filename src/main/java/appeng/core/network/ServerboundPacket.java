package appeng.core.network;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking.Context;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking.PlayPayloadHandler;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public interface ServerboundPacket<T extends CustomPacketPayload> extends CustomAppEngPayload, PlayPayloadHandler<T> {
    @Override
    void receive(T payload, Context context);
}
