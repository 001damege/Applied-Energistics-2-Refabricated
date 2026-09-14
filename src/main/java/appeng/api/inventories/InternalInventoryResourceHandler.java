/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2021 TeamAppliedEnergistics
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package appeng.api.inventories;

import appeng.core.definitions.AEItems;
import lombok.RequiredArgsConstructor;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.StoragePreconditions;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.fabricmc.fabric.api.transfer.v1.transaction.base.SnapshotParticipant;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;

@RequiredArgsConstructor
class InternalInventoryResourceHandler extends SnapshotParticipant<InternalInventoryResourceHandler.Snapshot> implements Storage<ItemVariant> {
    private final InternalInventory inventory;
    @Nullable
    private Snapshot lastReleasedSnapshot;

    @Override
    public long insert(ItemVariant resource, long maxAmount, TransactionContext transaction) {
        StoragePreconditions.notBlankNotNegative(resource, maxAmount);

        var stack = resource.toStack((int) maxAmount);
        updateSnapshots(transaction);
        var overflow = inventory.addItems(stack);
        return maxAmount - overflow.getCount();
    }

    @Override
    public long extract(ItemVariant resource, long maxAmount, TransactionContext transaction) {
        StoragePreconditions.notBlankNotNegative(resource, maxAmount);

        // Do not allow extraction of wrapped fluid stacks because they're an internal detail
        if (resource.getItem() == AEItems.WRAPPED_GENERIC_STACK.asItem()) {
            return 0;
        }

        updateSnapshots(transaction);
        var extracted = inventory.removeItems((int) maxAmount, resource.toStack(), null);
        return extracted.getCount();
    }

    @Override
    protected Snapshot createSnapshot() {
        Snapshot snapshot;
        if (this.lastReleasedSnapshot != null && this.lastReleasedSnapshot.items.length == inventory.size()) {
            snapshot = this.lastReleasedSnapshot;
            this.lastReleasedSnapshot = null;
        } else {
            snapshot = new Snapshot();
        }

        for (int i = 0; i < inventory.size(); i++) {
            var stack = inventory.getStackInSlot(i);
            snapshot.items[i] = stack;
            snapshot.counts[i] = stack.getCount();
        }
        return snapshot;
    }

    @Override
    protected void readSnapshot(Snapshot snapshot) {
        var items = snapshot.items;
        var counts = snapshot.counts;
        for (int i = 0; i < items.length; i++) {
            var stack = items[i];
            // Restore the previous count as well, the inventory might mutate the stack count for extract/insert
            // We do not restore NBT since the Storage API does not give access to the original NBT and the inventory
            // doesn't mutate it itself
            if (stack.getCount() != counts[i]) {
                stack.setCount(counts[i]);
            }
            inventory.setItemDirect(i, stack);
        }
    }

    @Override
    protected void releaseSnapshot(Snapshot snapshot) {
        this.lastReleasedSnapshot = snapshot;
    }

    public class Snapshot {
        ItemStack[] items;
        int[] counts;

        public Snapshot() {
            this.items = new ItemStack[inventory.size()];
            this.counts = new int[inventory.size()];
        }
    }

    @Override
    protected void onFinalCommit() {
        Snapshot original = new Snapshot();
        for (int i = 0; i < original.items.length; i++) {
            var current = inventory.getStackInSlot(i);
            if (current != original.items[i] || current.getCount() != original.counts[i]) {
                inventory.sendChangeNotification(i);
            }
        }
    }

    @Override
    public Iterator<StorageView<ItemVariant>> iterator() {
        return null;
    }
}
