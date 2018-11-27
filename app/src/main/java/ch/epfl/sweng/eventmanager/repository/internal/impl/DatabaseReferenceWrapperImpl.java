package ch.epfl.sweng.eventmanager.repository.internal.impl;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import ch.epfl.sweng.eventmanager.repository.internal.DatabaseReferenceWrapper;
import com.google.android.gms.tasks.Task;
import com.google.firebase.annotations.PublicApi;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

/**
 * @author Louis Vialar
 */
public class DatabaseReferenceWrapperImpl implements DatabaseReferenceWrapper {
    private final DatabaseReference dbRef;

    DatabaseReferenceWrapperImpl(DatabaseReference dbRef) {
        this.dbRef = dbRef;
    }

    @Override
    public ValueEventListener addValueEventListener(ValueEventListener listener) {
        return dbRef.addValueEventListener(listener);
    }

    @Override
    @PublicApi
    @NonNull
    public DatabaseReferenceWrapper child(@NonNull String pathString) {
        return new DatabaseReferenceWrapperImpl(dbRef.child(pathString));
    }

    @Override
    @PublicApi
    @NonNull
    public DatabaseReferenceWrapper push() {
        return new DatabaseReferenceWrapperImpl(dbRef.push());
    }

    @Override
    @PublicApi
    @NonNull
    public Task<Void> setValue(@Nullable Object value) {
        return dbRef.setValue(value);
    }

    @Override
    @PublicApi
    @Nullable
    public DatabaseReferenceWrapper getParent() {
        return new DatabaseReferenceWrapperImpl(dbRef.getParent());
    }

    @Override
    @PublicApi
    @Nullable
    public String getKey() {
        return dbRef.getKey();
    }
}
