package ch.epfl.sweng.eventmanager.test.repository.internal;

import androidx.annotation.NonNull;
import ch.epfl.sweng.eventmanager.repository.internal.DatabaseReferenceWrapper;
import ch.epfl.sweng.eventmanager.repository.internal.FirebaseDatabaseWrapper;
import ch.epfl.sweng.eventmanager.test.repository.DbRefHandler;

/**
 * @author Louis Vialar
 */
public class MockFirebaseDatabase implements FirebaseDatabaseWrapper, DbRefHandler {
    @Override
    public DatabaseReferenceWrapper getReference() {
        return null;
    }

    @Override
    public DatabaseReferenceWrapper getReference(@NonNull String path) {
        return null;
    }

    @Override
    public DatabaseReferenceWrapper getReferenceFromUrl(@NonNull String url) {
        return null;
    }
}
