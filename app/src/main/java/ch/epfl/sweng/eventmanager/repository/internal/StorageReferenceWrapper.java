package ch.epfl.sweng.eventmanager.repository.internal;

import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.StorageReference;

/**
 * This interface wraps a {@link StorageReference} to enable mocking it in tests.
 * If a method is missing but present in {@link StorageReference}, you can add it here and in the implementations.
 *
 * @author Louis Vialar
 */
public interface StorageReferenceWrapper {
    Task<byte[]> getBytes(long maxDownloadSizeBytes);

    StorageReferenceWrapper child(String path);
}
