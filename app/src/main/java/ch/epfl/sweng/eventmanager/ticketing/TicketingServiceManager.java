package ch.epfl.sweng.eventmanager.ticketing;

import android.content.Context;
import android.util.SparseArray;
import ch.epfl.sweng.eventmanager.repository.data.EventTicketingConfiguration;
import com.android.volley.RequestQueue;

/**
 * @author Louis Vialar
 */
public abstract class TicketingServiceManager {
    private static final String PREFERENCE_KEY = "saved_login_tokens";
    protected final SparseArray<TicketingService> services = new SparseArray<TicketingService>();

    protected abstract RequestQueue getRequestQueue(Context context);

    protected TokenStorage getTokenStorage(int eventId, Context context) {
        return new TokenStorage(eventId, context.getSharedPreferences(PREFERENCE_KEY, Context.MODE_PRIVATE));
    }

    public TicketingService getService(int eventId, EventTicketingConfiguration configuration, Context context) {
        if (services.get(eventId) == null) {
            services.put(eventId, new TicketingService(configuration, getTokenStorage(eventId, context), getRequestQueue(context)));
        }
        return services.get(eventId);
    }
}
