package ch.epfl.sweng.eventmanager.users;

import dagger.Binds;
import dagger.Module;

import javax.inject.Singleton;

/**
 * @author Louis Vialar
 */
@Module
public abstract class SessionModule {
    @Binds
    @Singleton
    abstract InMemorySession providesMemorySession(InMemoryFirebaseSession session);
}
