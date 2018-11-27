package ch.epfl.sweng.eventmanager.repository.internal.impl;

import androidx.annotation.NonNull;
import ch.epfl.sweng.eventmanager.repository.internal.DatabaseReferenceWrapper;
import ch.epfl.sweng.eventmanager.repository.internal.FirebaseDatabaseWrapper;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import javax.inject.Inject;

/**
 * @author Louis Vialar
 */
public class FirebaseDatabaseWrapperImpl implements FirebaseDatabaseWrapper {
    @Inject
    public FirebaseDatabaseWrapperImpl() {
    }


    @Override
    public DatabaseReferenceWrapper getReference() {
        return wrap(fbDb().getReference());
    }

    @Override
    public DatabaseReferenceWrapper getReference(@NonNull String path) {
        return wrap(fbDb().getReference(path));
    }

    @Override
    public DatabaseReferenceWrapper getReferenceFromUrl(@NonNull String url) {
        return wrap(fbDb().getReferenceFromUrl(url));
    }

    private FirebaseDatabase fbDb() {
        return FirebaseDatabase.getInstance();
    }

    private DatabaseReferenceWrapper wrap(DatabaseReference r) {
        return new DatabaseReferenceWrapperImpl(r);
    }
}
