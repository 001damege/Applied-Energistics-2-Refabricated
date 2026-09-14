package appeng.util;

import lombok.RequiredArgsConstructor;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.TransferVariant;
import net.fabricmc.fabric.api.transfer.v1.transaction.base.SnapshotParticipant;

@RequiredArgsConstructor
public abstract class InsertionOnlyResourceHandlerWithJournal<T extends TransferVariant<?>, S> extends SnapshotParticipant<S> implements Storage<T> {
    private final T emptyResource;
    protected S pendingSideEffect;

    @Override
    public boolean supportsExtraction() {
        return false;
    }

    @Override
    protected S createSnapshot() {
        return pendingSideEffect;
    }

    @Override
    protected void readSnapshot(S snapshot) {
        pendingSideEffect = snapshot;
    }
}
