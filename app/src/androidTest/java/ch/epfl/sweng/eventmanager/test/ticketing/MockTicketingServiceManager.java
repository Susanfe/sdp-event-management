package ch.epfl.sweng.eventmanager.test.ticketing;

import android.content.Context;
import ch.epfl.sweng.eventmanager.repository.data.EventTicketingConfiguration;
import ch.epfl.sweng.eventmanager.ticketing.TicketingService;
import ch.epfl.sweng.eventmanager.ticketing.TicketingServiceManager;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BaseHttpStack;
import com.android.volley.toolbox.Volley;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author Louis Vialar
 */
@Singleton
public class MockTicketingServiceManager extends TicketingServiceManager {
    private BaseHttpStack stack = null;
    private boolean setAutomatically = true;

    @Inject
    public MockTicketingServiceManager() {
    }

    @Override
    protected RequestQueue getRequestQueue(Context context) {
        return Volley.newRequestQueue(context, stack);
    }

    public void setStack(BaseHttpStack stack) {
        this.stack = stack;
        this.setAutomatically = false;
    }

    public void resetService(int eventId) {
        services.remove(eventId);

        System.out.println("reset service for event " + eventId);
    }

    public void setSetAutomatically(boolean setAutomatically) {
        this.setAutomatically = setAutomatically;
    }

    @Override
    public MockTicketingService getService(int eventId, EventTicketingConfiguration configuration, Context context) {
        System.out.println("get service for event " + eventId);

        if (setAutomatically && MockStacks.STACKS.containsKey(configuration)) {
            stack = MockStacks.STACKS.get(configuration).create();
        }

        if (services.get(eventId) == null) {
            services.put(eventId, new MockTicketingService(configuration, getTokenStorage(eventId, context), getRequestQueue(context)));
        }
        return (MockTicketingService) services.get(eventId);
    }
}
