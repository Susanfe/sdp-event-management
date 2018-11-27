package ch.epfl.sweng.eventmanager.test.repository.internal;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import ch.epfl.sweng.eventmanager.repository.internal.DatabaseReferenceWrapper;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.utilities.PushIdGenerator;

import java.util.Arrays;

/**
 * @author Louis Vialar
 */
public class MockDatabaseReferenceWrapper implements DatabaseReferenceWrapper {
    private final String[] path;
    private final MockFirebaseDatabase database;

    MockDatabaseReferenceWrapper(String[] path, MockFirebaseDatabase database) {
        this.path = path;
        this.database = database;
    }

    @Override
    public ValueEventListener addValueEventListener(ValueEventListener listener) {
        return null;
    }

    @NonNull
    @Override
    public DatabaseReferenceWrapper child(@NonNull String pathString) {
        String[] add = pathString.split("/");
        String[] newPath = Arrays.copyOf(path, path.length + add.length);
        System.arraycopy(add, 0, newPath, path.length, add.length);
        return new MockDatabaseReferenceWrapper(newPath, database);
    }

    @NonNull
    @Override
    public DatabaseReferenceWrapper push() {
        String child = PushIdGenerator.generatePushChildName(System.currentTimeMillis());
        return child(child);
    }

    @NonNull
    @Override
    public Task<Void> setValue(@Nullable Object value) {
        return Tasks.call(() -> null);
    }

    @Nullable
    @Override
    public DatabaseReferenceWrapper getParent() {
        if (path.length == 0)
            return null;
        return new MockDatabaseReferenceWrapper(Arrays.copyOfRange(path, 0, path.length - 1), database); // Drop last part
    }

    @Nullable
    @Override
    public String getKey() {
        return path.length > 0 ? path[path.length - 1] : null;
    }
}
