package ch.epfl.sweng.eventmanager.ui.ticketing;

import android.content.Context;

import javax.inject.Inject;

import ch.epfl.sweng.eventmanager.repository.data.EventTicketingConfiguration;
import ch.epfl.sweng.eventmanager.test.TestApplication;
import ch.epfl.sweng.eventmanager.test.repository.MockEventsRepository;
import ch.epfl.sweng.eventmanager.test.ticketing.MockTicketingService;
import ch.epfl.sweng.eventmanager.test.ticketing.MockTicketingServiceManager;

/**
 * @author Louis Vialar
 */
public abstract class ScanningTest {
    @Inject
    protected MockEventsRepository repository;

    @Inject
    protected MockTicketingServiceManager manager;
    @Inject
    protected TicketingManager ticketingManager;

    protected final int eventId;

    protected ScanningTest(int eventId) {
        this.eventId = eventId;

        TestApplication.component.inject(this);
    }

    public EventTicketingConfiguration getConfiguration() {
        // TODO handle null exception
        return repository.getEvent(eventId).getValue().getTicketingConfiguration();
    }

    public MockTicketingService getTicketingService() {
        return manager.getService(eventId, null, null);
    }

    protected MockTicketingService getOrCreateTicketingService(Context ctx) {
        return manager.getService(eventId, getConfiguration(), ctx);
    }

}
