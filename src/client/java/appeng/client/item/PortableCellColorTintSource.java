package appeng.client.item;

import appeng.core.AppEng;
import appeng.items.tools.powered.AbstractPortableCell;
import com.mojang.serialization.MapCodec;
import net.minecraft.client.color.item.ItemTintSource;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public record PortableCellColorTintSource() implements ItemTintSource {
    public static final Identifier ID = AppEng.makeId("portable_cell_color");

    public static final MapCodec<PortableCellColorTintSource> MAP_CODEC = MapCodec
            .unit(PortableCellColorTintSource::new);

    @Override
    public int calculate(ItemStack stack, @Nullable ClientLevel level, @Nullable LivingEntity entity) {
        if (stack.getItem() instanceof AbstractPortableCell portableCell) {
            return ARGB.opaque(portableCell.getColor(stack));
        }

        return -1;
    }

    @Override
    public MapCodec<? extends ItemTintSource> type() {
        return MAP_CODEC;
    }
}
