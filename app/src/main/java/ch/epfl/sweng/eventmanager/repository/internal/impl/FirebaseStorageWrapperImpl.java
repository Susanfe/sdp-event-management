package ch.epfl.sweng.eventmanager.repository.internal.impl;

import androidx.annotation.NonNull;
import ch.epfl.sweng.eventmanager.repository.internal.DatabaseReferenceWrapper;
import ch.epfl.sweng.eventmanager.repository.internal.FirebaseDatabaseWrapper;
import ch.epfl.sweng.eventmanager.repository.internal.FirebaseStorageWrapper;
import ch.epfl.sweng.eventmanager.repository.internal.StorageReferenceWrapper;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import javax.inject.Inject;

/**
 * @author Louis Vialar
 */
public class FirebaseStorageWrapperImpl implements FirebaseStorageWrapper {
    @Inject
    public FirebaseStorageWrapperImpl() {
    }

    @Override
    public StorageReferenceWrapper getReference() {
        return wrap(fbDb().getReference());
    }

    @Override
    public StorageReferenceWrapper getReference(@NonNull String path) {
        return wrap(fbDb().getReference(path));
    }

    @Override
    public StorageReferenceWrapper getReferenceFromUrl(@NonNull String url) {
        return wrap(fbDb().getReferenceFromUrl(url));
    }

    private FirebaseStorage fbDb() {
        return FirebaseStorage.getInstance();
    }

    private StorageReferenceWrapper wrap(StorageReference r) {
        return new StorageReferenceWrapperImpl(r);
    }
}
