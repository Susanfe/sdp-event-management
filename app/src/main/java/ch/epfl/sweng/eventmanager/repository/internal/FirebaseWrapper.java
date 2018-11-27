package ch.epfl.sweng.eventmanager.repository.internal;

import androidx.annotation.NonNull;

/**
 * @author Louis Vialar
 */
public interface FirebaseWrapper<RefType> {
    RefType getReference();

    RefType getReference(@NonNull String path);

    RefType getReferenceFromUrl(@NonNull String url);
}
