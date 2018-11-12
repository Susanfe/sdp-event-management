package ch.epfl.sweng.eventmanager.test.ticketing;

import android.content.Context;
import ch.epfl.sweng.eventmanager.repository.data.EventTicketingConfiguration;
import ch.epfl.sweng.eventmanager.ticketing.NotAuthenticatedException;
import ch.epfl.sweng.eventmanager.ticketing.TicketingService;
import ch.epfl.sweng.eventmanager.ticketing.TicketingServiceManager;
import ch.epfl.sweng.eventmanager.ticketing.data.ScanConfiguration;
import ch.epfl.sweng.eventmanager.ticketing.data.ScanResult;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

/**
 * @author Louis Vialar
 */
@Singleton
public class MockTicketingServiceManager implements TicketingServiceManager {
    @Inject
    public MockTicketingServiceManager() {
    }

    @Override
    public TicketingService getService(int eventId, EventTicketingConfiguration configuration, Context context) {
        return null;
    }

    public static class MockTicketingService implements TicketingService {
        private final EventTicketingConfiguration configuration;

        public MockTicketingService(EventTicketingConfiguration configuration) {
            this.configuration = configuration;
        }

        @Override
        public boolean requiresLogin() {
            return false;
        }

        @Override
        public boolean hasMultipleConfigurations() {
            return false;
        }

        @Override
        public void login(String email, String password, ApiCallback<Void> callback) {

        }

        @Override
        public void getConfigurations(ApiCallback<List<ScanConfiguration>> callback) throws NotAuthenticatedException {

        }

        @Override
        public void scan(int configId, String barcode, ApiCallback<ScanResult> callback) throws NotAuthenticatedException {

        }

        @Override
        public boolean isLoggedIn() {
            return false;
        }
    }
}
