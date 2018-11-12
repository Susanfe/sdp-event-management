package ch.epfl.sweng.eventmanager.test.ticketing;

import ch.epfl.sweng.eventmanager.ticketing.TicketingServiceManager;
import ch.epfl.sweng.eventmanager.ticketing.impl.TicketingServiceManagerImpl;
import dagger.Binds;
import dagger.Module;

import javax.inject.Singleton;

/**
 * @author Louis Vialar
 */
@Module
public abstract class MockTicketingModule {
    @Binds
    @Singleton
    abstract TicketingServiceManager providesTicketingServiceManager(MockTicketingServiceManager impl);
}
