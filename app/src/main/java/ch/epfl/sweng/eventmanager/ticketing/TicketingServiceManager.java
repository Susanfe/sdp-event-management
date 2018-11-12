package ch.epfl.sweng.eventmanager.ticketing;

import android.content.Context;
import ch.epfl.sweng.eventmanager.repository.data.EventTicketingConfiguration;

/**
 * @author Louis Vialar
 */
public interface TicketingServiceManager {
    TicketingService getService(int eventId, EventTicketingConfiguration configuration, Context context);
}
