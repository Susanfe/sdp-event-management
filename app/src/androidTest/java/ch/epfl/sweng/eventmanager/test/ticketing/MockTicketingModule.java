package ch.epfl.sweng.eventmanager.test.ticketing;

import ch.epfl.sweng.eventmanager.ticketing.TicketingServiceManager;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

/**
 * @author Louis Vialar
 */
@Module
public class MockTicketingModule {
    @Provides
    @Singleton
    MockTicketingServiceManager providesMockTicketingServiceManager() {
        return new MockTicketingServiceManager();
    }

    @Provides
    @Singleton
    TicketingServiceManager providesTicketingServiceManager(MockTicketingServiceManager impl) {
        return impl;
    }
}
