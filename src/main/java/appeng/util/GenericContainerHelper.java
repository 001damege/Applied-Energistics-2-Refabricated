package appeng.util;

import appeng.api.stacks.GenericStack;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

/**
 * Allows generalized extraction from item-based containers such as buckets or tanks.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class GenericContainerHelper {
    @Nullable
    public static GenericStack getContainedFluidStack(ItemStack stack) {
        return null;
    }
}
