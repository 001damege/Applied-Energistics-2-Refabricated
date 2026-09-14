package appeng.api.behaviors;

import appeng.api.config.Actionable;
import appeng.api.stacks.AEFluidKey;
import appeng.api.stacks.GenericStack;
import appeng.util.GenericContainerHelper;
import appeng.util.fluid.FluidSoundHelper;
import com.google.common.primitives.Ints;
import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.item.base.SingleItemStorage;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageUtil;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

class FluidContainerItemStrategy implements ContainerItemStrategy<AEFluidKey, Storage<FluidVariant>> {
    @Override
    public @Nullable GenericStack getContainedStack(ItemStack stack) {
        return GenericContainerHelper.getContainedFluidStack(stack);
    }

    @Override
    public @Nullable Storage<FluidVariant> findCarriedContext(Player player, AbstractContainerMenu menu) {
        var itemAccess = ContainerItemContext.ofPlayerCursor(player, menu);
        return FluidStorage.ITEM.find(itemAccess.getItemVariant().toStack(), itemAccess);
    }

    @Override
    public @Nullable Storage<FluidVariant> findPlayerSlotContext(Player player, int slot) {
        var itemAccess = ContainerItemContext.ofPlayerSlot(player, new SingleItemStorage() {
            @Override
            protected long getCapacity(ItemVariant variant) {
                return slot;
            }
        });
        return FluidStorage.ITEM.find(itemAccess.getItemVariant().toStack(), itemAccess);
    }

    @Override
    public long extract(Storage<FluidVariant> context, AEFluidKey what, long amount, Actionable mode) {
        try (var tx = Transaction.openNested(null)) {
            var extracted = context.extract(what.toResource(), Ints.saturatedCast(amount), tx);
            if (mode == Actionable.MODULATE) {
                tx.commit();
            }
            return extracted;
        }
    }

    @Override
    public long insert(Storage<FluidVariant> context, AEFluidKey what, long amount, Actionable mode) {
        try (var tx = Transaction.openNested(null)) {
            var inserted = context.insert(what.toResource(), Ints.saturatedCast(amount), tx);
            if (mode == Actionable.MODULATE) {
                tx.commit();
            }
            return inserted;
        }
    }

    @Override
    public void playFillSound(Player player, AEFluidKey what) {
        FluidSoundHelper.playFillSound(player, what);
    }

    @Override
    public void playEmptySound(Player player, AEFluidKey what) {
        FluidSoundHelper.playEmptySound(player, what);
    }

    @Override
    public @Nullable GenericStack getExtractableContent(Storage<FluidVariant> context) {
        try (var tx = Transaction.openNested(null)) {
            var stack = StorageUtil.findExtractableContent(context, r -> true, tx);
            if (stack != null) {
                return new GenericStack(AEFluidKey.of(stack.resource()), stack.amount());
            }
        }
        return null;
    }
}
