package ch.epfl.sweng.eventmanager.ticketing;

import ch.epfl.sweng.eventmanager.ticketing.impl.TicketingServiceManagerImpl;
import dagger.Binds;
import dagger.Module;

import javax.inject.Singleton;

/**
 * @author Louis Vialar
 */
@Module
public abstract class TicketingModule {
    @Binds
    @Singleton
    abstract TicketingServiceManager providesTicketingServiceManager(TicketingServiceManagerImpl impl);
}
