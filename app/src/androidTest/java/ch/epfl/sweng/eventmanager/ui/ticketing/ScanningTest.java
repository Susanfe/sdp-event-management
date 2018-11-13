package ch.epfl.sweng.eventmanager.ui.ticketing;

import android.content.Context;
import ch.epfl.sweng.eventmanager.repository.data.EventTicketingConfiguration;
import ch.epfl.sweng.eventmanager.test.TestApplication;
import ch.epfl.sweng.eventmanager.test.repository.MockEventsRepository;
import ch.epfl.sweng.eventmanager.test.ticketing.MockTicketingService;
import ch.epfl.sweng.eventmanager.test.ticketing.MockTicketingServiceManager;
import ch.epfl.sweng.eventmanager.ticketing.TicketingService;
import org.junit.Before;

import javax.inject.Inject;

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
        return repository.getEvent(eventId).getValue().getTicketingConfiguration();
    }

    public MockTicketingService getTicketingService() {
        return manager.getService(eventId, null, null);
    }

    public MockTicketingService getOrCreateTicketingService(Context ctx) {
        return manager.getService(eventId, getConfiguration(), ctx);
    }

}
