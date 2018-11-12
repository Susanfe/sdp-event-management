package ch.epfl.sweng.eventmanager.ticketing.impl;

import android.content.Context;
import android.util.SparseArray;
import ch.epfl.sweng.eventmanager.repository.data.EventTicketingConfiguration;
import ch.epfl.sweng.eventmanager.ticketing.TicketingService;
import ch.epfl.sweng.eventmanager.ticketing.TicketingServiceManager;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author Louis Vialar
 */
@Singleton
public class TicketingServiceManagerImpl implements TicketingServiceManager {
    private final SparseArray<TicketingService> services = new SparseArray<TicketingService>();

    @Inject
    TicketingServiceManagerImpl() {
    }

    @Override
    public TicketingService getService(int eventId, EventTicketingConfiguration configuration, Context context) {
        if (services.get(eventId) == null)
            services.put(eventId, new TicketingServiceImpl(configuration, eventId, context));
        return services.get(eventId);
    }
}
