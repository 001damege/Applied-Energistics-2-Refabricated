
package appeng.core.network.clientbound;

import appeng.core.network.ClientboundPacket;
import appeng.core.network.CustomAppEngPayload;
import appeng.menu.me.crafting.CraftingStatus;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public record CraftingStatusPacket(int containerId, CraftingStatus status) implements ClientboundPacket {
    public static final StreamCodec<RegistryFriendlyByteBuf, CraftingStatusPacket> STREAM_CODEC = StreamCodec.ofMember(
            CraftingStatusPacket::write,
            CraftingStatusPacket::decode);

    public static final Type<CraftingStatusPacket> TYPE = CustomAppEngPayload.createType("crafting_status");

    @Override
    public Type<CraftingStatusPacket> type() {
        return TYPE;
    }

    public static CraftingStatusPacket decode(RegistryFriendlyByteBuf buffer) {
        return new CraftingStatusPacket(
                buffer.readInt(),
                CraftingStatus.read(buffer));
    }

    public void write(RegistryFriendlyByteBuf data) {
        data.writeInt(containerId);
        status.write(data);
    }

}
