package appeng.menu.guisync;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import org.jetbrains.annotations.Nullable;

public final class NullableSynchronizedValue<T> {
    private final StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec;
    @Nullable
    private T value;

    private NullableSynchronizedValue(StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec) {
        this.streamCodec = streamCodec;
    }

    @Nullable
    public T get() {
        return value;
    }
}
