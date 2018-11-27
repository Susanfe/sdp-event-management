package ch.epfl.sweng.eventmanager.repository.internal.impl;

import ch.epfl.sweng.eventmanager.repository.internal.FirebaseDatabaseWrapper;
import ch.epfl.sweng.eventmanager.repository.internal.FirebaseStorageWrapper;
import ch.epfl.sweng.eventmanager.repository.internal.TwitterWrapper;
import ch.epfl.sweng.eventmanager.repository.internal.impl.FirebaseDatabaseWrapperImpl;
import ch.epfl.sweng.eventmanager.repository.internal.impl.FirebaseStorageWrapperImpl;
import ch.epfl.sweng.eventmanager.repository.internal.impl.TwitterWrapperImpl;
import dagger.Binds;
import dagger.Module;

import javax.inject.Singleton;

/**
 * This module provides all the implementation to the (database/external services) wrappers
 * @author Louis Vialar
 */
@Module
public abstract class RepositoriesInternalModule {
    @Binds
    @Singleton
    abstract FirebaseDatabaseWrapper providesFirebaseDatabaseWrapper(FirebaseDatabaseWrapperImpl wrapper);
    @Binds
    @Singleton
    abstract FirebaseStorageWrapper providesFirebaseStorageWrapper(FirebaseStorageWrapperImpl wrapper);

    @Binds
    @Singleton
    abstract TwitterWrapper providesTwitterWrapper(TwitterWrapperImpl wrapper);
}
