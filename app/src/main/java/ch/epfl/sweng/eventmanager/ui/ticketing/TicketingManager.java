package ch.epfl.sweng.eventmanager.ui.ticketing;

import android.content.Context;
import android.content.Intent;
import ch.epfl.sweng.eventmanager.repository.data.Event;
import ch.epfl.sweng.eventmanager.ticketing.TicketingService;
import ch.epfl.sweng.eventmanager.ticketing.TicketingServiceManager;

import javax.inject.Inject;

/**
 * @author Louis Vialar
 */
public final class TicketingManager {
    private TicketingServiceManager manager;

    @Inject
    TicketingManager(TicketingServiceManager manager) {
        this.manager = manager;
    }

    public Intent start(Event event, Context context) {
        if (event.getTicketingConfiguration() == null) {
            throw new UnsupportedOperationException("this event doesn't support ticketing");
        }

        TicketingService service = manager.getService(event.getId(), event.getTicketingConfiguration(), context);

        Intent openIntent = new Intent(context, TicketingActivity.getNextActivityForState(service));
        openIntent.putExtra(TicketingActivity.SELECTED_EVENT_ID, event.getId());
        openIntent.putExtra(TicketingActivity.TICKETING_CONFIGURATION, event.getTicketingConfiguration());
        openIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return openIntent;
    }
}
