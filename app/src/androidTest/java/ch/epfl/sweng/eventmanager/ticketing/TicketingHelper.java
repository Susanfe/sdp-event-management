package ch.epfl.sweng.eventmanager.ticketing;

import ch.epfl.sweng.eventmanager.repository.data.EventTicketingConfiguration;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BaseHttpStack;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.NoCache;

/**
 * @author Louis Vialar
 */
public class TicketingHelper {
    public static String LOGIN_URL = "https://local/login";
    public static String SCAN_URL = "https://local/scan";
    public static String SCAN_CONFIG_URL = SCAN_URL + "/";
    public static String CONFIGS_URL = "https://local/configs";

    public static RequestQueue getRequestQueue(BaseHttpStack stack) {
        RequestQueue q = new RequestQueue(new NoCache(), new BasicNetwork(stack));
        q.start();
        return q;
    }

    public static TicketingService getService(EventTicketingConfiguration config, BaseHttpStack stack) {
        return new TicketingService(config, new NotCachedTokenStorage(), getRequestQueue(stack));
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
