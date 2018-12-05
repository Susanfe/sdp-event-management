package ch.epfl.sweng.eventmanager.test.users;

import ch.epfl.sweng.eventmanager.users.InMemorySession;
import dagger.Binds;
import dagger.Module;

import javax.inject.Singleton;

/**
 * @author Louis Vialar
 */
@Module
public abstract class MockUsersModule {
    @Binds
    @Singleton
    public abstract InMemorySession providesInMemorySession(DummyInMemorySession repository);
}
