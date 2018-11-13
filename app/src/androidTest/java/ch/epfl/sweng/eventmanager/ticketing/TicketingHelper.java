package ch.epfl.sweng.eventmanager.ticketing;

import ch.epfl.sweng.eventmanager.repository.data.EventTicketingConfiguration;
import ch.epfl.sweng.eventmanager.test.ticketing.MockStacks;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BaseHttpStack;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.NoCache;

/**
 * @author Louis Vialar
 */
public class TicketingHelper {

    public static RequestQueue getRequestQueue(BaseHttpStack stack) {
        RequestQueue q = new RequestQueue(new NoCache(), new BasicNetwork(stack));
        q.start();
        return q;
    }

    public static TicketingService getService(EventTicketingConfiguration config) {
        return new TicketingService(config, new NotCachedTokenStorage(), getRequestQueue(MockStacks.STACKS.get(config).create()));
    }

    public static class NotCachedTokenStorage extends TokenStorage {
        public NotCachedTokenStorage() {
            super(-1, null);
        }

        @Override
        protected void readTokenFromPreferences() {
        }

        @Override
        protected void writeTokenToPreferences() {
        }
    }

}
