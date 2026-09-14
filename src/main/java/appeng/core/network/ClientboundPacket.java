package appeng.core.network;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking.Context;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking.PlayPayloadHandler;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public interface ClientboundPacket<T extends CustomPacketPayload> extends CustomAppEngPayload, PlayPayloadHandler<T> {
    @Override
    void receive(T payload, Context context);
}
