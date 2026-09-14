package appeng.api.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;

import java.util.List;

@SuppressWarnings("deprecation")
public record ExportedUpgrades(List<ItemStack> upgrades) {
    // Defined using xmap since we previously used a List directly.
    public static Codec<ExportedUpgrades> CODEC = RecordCodecBuilder.create(builder -> builder.group(
            ItemStack.CODEC.listOf().fieldOf("upgrades").forGetter(ExportedUpgrades::upgrades))
            .apply(builder, ExportedUpgrades::new));

    public static StreamCodec<RegistryFriendlyByteBuf, ExportedUpgrades> STREAM_CODEC = StreamCodec.composite(
            ItemStack.OPTIONAL_LIST_STREAM_CODEC, ExportedUpgrades::upgrades,
            ExportedUpgrades::new);

    @Override
    public boolean equals(Object object) {
        return this == object || object instanceof ExportedUpgrades(List<ItemStack> upgrades1) && ItemStack.listMatches(upgrades, upgrades1);
    }

    @Override
    public int hashCode() {
        return ItemStack.hashStackList(upgrades);
    }
}
