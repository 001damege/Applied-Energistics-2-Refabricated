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

import lombok.RequiredArgsConstructor;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.world.item.ItemStack;

/**
 * Wraps an inventory implementing the platforms standard inventory interface (i.e. IItemHandler on Forge) such that it
 * can be used as an {@link InternalInventory}.
 */
@RequiredArgsConstructor
public class PlatformInventoryWrapper implements InternalInventory {
    private final Storage<ItemVariant> handler;

    @Override
    public Storage<ItemVariant> toResourceHandler() {
        return handler;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public int getSlotLimit(int slot) {
        return (int) handler.iterator().next().getCapacity();
    }

    @Override
    public ItemStack getStackInSlot(int slotIndex) {
        // TODO 1.21.9: this is obviously not mutable
        var resource = handler.iterator().next().getResource().toStack(slotIndex);
        return !resource.isEmpty() ? resource : ItemStack.EMPTY;
    }

    @Override
    public void setItemDirect(int slotIndex, ItemStack stack) {
        try (var tx = Transaction.openNested(null)) {
            var current = handler.iterator().next();
            if (!current.isResourceBlank()) {
                handler.extract(current.getResource(), current.getAmount(), tx);
            }
            handler.insert(ItemVariant.of(stack), stack.getCount(), tx);
            tx.commit();
        }
    }

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
        return handler.iterator().hasNext();
    }

    @Override
    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
        try (var tx = Transaction.openNested(null)) {
            var inserted = handler.insert(ItemVariant.of(stack), stack.getCount(), tx);
            if (!simulate) {
                tx.commit();
            }
            return stack.copyWithCount((int) (stack.getCount() - inserted));
        }
    }

    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        try (var tx = Transaction.openNested(null)) {
            var resource = handler.iterator().next();
            if (resource.isResourceBlank()) {
                return ItemStack.EMPTY;
            }
            var extracted = handler.extract(resource.getResource(), amount, tx);
            if (!simulate) {
                tx.commit();
            }
            return resource.getResource().toStack((int) extracted);
        }
    }
}
