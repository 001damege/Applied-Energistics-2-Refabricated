package appeng.core.network.bidirectional;

import appeng.api.config.Setting;
import appeng.api.util.IConfigManager;
import appeng.api.util.IConfigurableObject;
import appeng.core.network.ClientboundPacket;
import appeng.core.network.CustomAppEngPayload;
import appeng.core.network.ServerboundPacket;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public record ConfigValuePacket(String name, String value) implements ClientboundPacket<ConfigValuePacket>, ServerboundPacket<ConfigValuePacket> {
    public static final StreamCodec<RegistryFriendlyByteBuf, ConfigValuePacket> STREAM_CODEC = StreamCodec.ofMember(ConfigValuePacket::write, ConfigValuePacket::decode);

    public static final Type<ConfigValuePacket> TYPE = CustomAppEngPayload.createType("config_value");

    @Override
    public Type<ConfigValuePacket> type() {
        return TYPE;
    }

    public static ConfigValuePacket decode(RegistryFriendlyByteBuf stream) {
        var name = stream.readUtf();
        var value = stream.readUtf();
        return new ConfigValuePacket(name, value);
    }

    public void write(RegistryFriendlyByteBuf data) {
        data.writeUtf(name);
        data.writeUtf(value);
    }

    public <T extends Enum<T>> ConfigValuePacket(Setting<T> setting, T value) {
        this(setting.getName(), value.name());
        if (!setting.getValues().contains(value)) {
            throw new IllegalStateException(value + " not a valid value for " + setting);
        }
    }

    public <T extends Enum<T>> ConfigValuePacket(Setting<T> setting, IConfigManager configManager) {
        this(setting, setting.getValue(configManager));
    }

    @Override
    public void receive(ConfigValuePacket payload, ClientPlayNetworking.Context context) {
        var player = context.player();
        if (player.containerMenu instanceof IConfigurableObject configurableObject) {
            loadSetting(configurableObject);
        }
    }

    @Override
    public void receive(ConfigValuePacket payload, ServerPlayNetworking.Context context) {
        var player = context.player();
        if (player.containerMenu instanceof IConfigurableObject configurableObject) {
            loadSetting(configurableObject);
        }
    }

    public void loadSetting(IConfigurableObject configurableObject) {
        var cm = configurableObject.getConfigManager();

        for (var setting : cm.getSettings()) {
            if (setting.getName().equals(this.name)) {
                setting.setFromString(cm, value);
                break;
            }
        }
    }
}
