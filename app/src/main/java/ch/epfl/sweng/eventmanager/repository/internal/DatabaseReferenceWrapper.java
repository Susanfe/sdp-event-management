package ch.epfl.sweng.eventmanager.repository.internal;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.gms.tasks.Task;
import com.google.firebase.annotations.PublicApi;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

/**
 * This interface wraps a {@link DatabaseReference} to enable mocking it in tests. If a method is missing but present
 * in {@link DatabaseReference}, you can add it here and in the implementations.
 *
 * @author Louis Vialar
 */
public interface DatabaseReferenceWrapper {
    ValueEventListener addValueEventListener(ValueEventListener listener);

    @PublicApi
    @NonNull
    DatabaseReferenceWrapper child(@NonNull String pathString);

    @PublicApi
    @NonNull
    DatabaseReferenceWrapper push();

    @PublicApi
    @NonNull
    Task<Void> setValue(@Nullable Object value);

    @PublicApi
    @Nullable
    DatabaseReferenceWrapper getParent();

    @PublicApi
    @Nullable
    String getKey();
}
