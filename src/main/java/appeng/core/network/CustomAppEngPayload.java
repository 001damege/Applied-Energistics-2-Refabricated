package appeng.core.network;

import appeng.core.AppEng;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public interface CustomAppEngPayload extends CustomPacketPayload {
    static <T extends CustomPacketPayload> CustomPacketPayload.Type<T> createType(String name) {
        return new CustomPacketPayload.Type<>(AppEng.makeId(name));
    }
}
