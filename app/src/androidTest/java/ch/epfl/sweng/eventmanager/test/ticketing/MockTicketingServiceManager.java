package ch.epfl.sweng.eventmanager.test.ticketing;

import android.content.Context;
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

    @Inject
    public MockTicketingServiceManager() {
    }

    @Override
    protected RequestQueue getRequestQueue(Context context) {
        return Volley.newRequestQueue(context, stack);
    }

    public void setStack(BaseHttpStack stack) {
        this.stack = stack;
    }
}
