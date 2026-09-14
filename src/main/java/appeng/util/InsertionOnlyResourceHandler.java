package appeng.util;

import lombok.RequiredArgsConstructor;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.TransferVariant;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;

@RequiredArgsConstructor
public abstract class InsertionOnlyResourceHandler<T extends TransferVariant<?>> implements Storage<T> {
    private final T emptyResource;

    @Override
    public long insert(T resource, long maxAmount, TransactionContext transaction) {
        return 0;
    }

    @Override
    public long extract(T resource, long maxAmount, TransactionContext transaction) {
        return 0;
    }

    @Override
    public boolean supportsExtraction() {
        return false;
    }
}
