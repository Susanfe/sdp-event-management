package ch.epfl.sweng.eventmanager.ticketing.impl;

import android.content.Context;
import ch.epfl.sweng.eventmanager.ticketing.TicketingServiceManager;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author Louis Vialar
 */
@Singleton
public class TicketingServiceManagerImpl extends TicketingServiceManager {
    @Inject
    TicketingServiceManagerImpl() {
    }

    @Override
    protected RequestQueue getRequestQueue(Context context) {
        return Volley.newRequestQueue(context.getApplicationContext());
    }
}
