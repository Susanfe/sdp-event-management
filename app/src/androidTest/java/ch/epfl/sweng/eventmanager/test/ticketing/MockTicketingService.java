package ch.epfl.sweng.eventmanager.test.ticketing;

import com.android.volley.RequestQueue;

import java.util.List;

import ch.epfl.sweng.eventmanager.repository.data.EventTicketingConfiguration;
import ch.epfl.sweng.eventmanager.ticketing.NotAuthenticatedException;
import ch.epfl.sweng.eventmanager.ticketing.TicketingService;
import ch.epfl.sweng.eventmanager.ticketing.TokenStorage;
import ch.epfl.sweng.eventmanager.ticketing.data.ApiResult;
import ch.epfl.sweng.eventmanager.ticketing.data.ScanConfiguration;
import ch.epfl.sweng.eventmanager.ticketing.data.ScanResult;

/**
 * @author Louis Vialar
 */
public class MockTicketingService extends TicketingService {
    private List<ApiResult.ApiError> failWith = null;

    MockTicketingService(EventTicketingConfiguration configuration, TokenStorage storage, RequestQueue queue) {
        super(configuration, storage, queue);
    }

    public void failNextWith(List<ApiResult.ApiError> failWith) {
        this.failWith = failWith;
    }

    private boolean tryToFail(ApiCallback callback) {
        if (failWith != null) {
            callback.onFailure(failWith);
            failWith = null;
            return true;
        }
        return false;
    }

    @Override
    public void login(String email, String password, ApiCallback<Void> callback) {
        if (tryToFail(callback))
            return;

        super.login(email, password, callback);
    }

    @Override
    public void getConfigurations(ApiCallback<List<ScanConfiguration>> callback) throws NotAuthenticatedException {
        if (tryToFail(callback))
            return;

        super.getConfigurations(callback);
    }

    @Override
    public void scan(int configId, String barcode, ApiCallback<ScanResult> callback) throws NotAuthenticatedException {
        if (tryToFail(callback))
            return;

        super.scan(configId, barcode, callback);
    }
}
