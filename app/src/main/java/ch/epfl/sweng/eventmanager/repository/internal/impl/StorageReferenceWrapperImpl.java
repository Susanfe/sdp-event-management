package ch.epfl.sweng.eventmanager.repository.internal.impl;

import ch.epfl.sweng.eventmanager.repository.internal.StorageReferenceWrapper;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.StorageReference;

/**
 * @author Louis Vialar
 */
public class StorageReferenceWrapperImpl implements StorageReferenceWrapper {
    private final StorageReference ref;

    StorageReferenceWrapperImpl(StorageReference ref) {
        this.ref = ref;
    }

    @Override
    public Task<byte[]> getBytes(long maxDownloadSizeBytes) {
        return ref.getBytes(maxDownloadSizeBytes);
    }

    @Override
    public StorageReferenceWrapper child(String path) {
        return new StorageReferenceWrapperImpl(ref.child(path));
    }
}
