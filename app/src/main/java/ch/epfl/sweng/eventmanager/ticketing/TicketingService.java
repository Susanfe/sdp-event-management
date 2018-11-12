package ch.epfl.sweng.eventmanager.ticketing;

import ch.epfl.sweng.eventmanager.ticketing.data.ApiResult;
import ch.epfl.sweng.eventmanager.ticketing.data.ScanConfiguration;
import ch.epfl.sweng.eventmanager.ticketing.data.ScanResult;
import com.android.volley.VolleyError;

import java.util.Collections;
import java.util.List;

/**
 * @author Louis Vialar
 */
public interface TicketingService {
    boolean requiresLogin();

    boolean hasMultipleConfigurations();

    void login(String email, String password, ApiCallback<Void> callback);

    void getConfigurations(ApiCallback<List<ScanConfiguration>> callback) throws NotAuthenticatedException;

    void scan(int configId, String barcode, ApiCallback<ScanResult> callback) throws NotAuthenticatedException;

    boolean isLoggedIn();

    interface ApiCallback<T> {
        void onSuccess(T data);

        void onFailure(List<ApiResult.ApiError> errors);


        static <T> void failure(ApiCallback callback, String error) {
            callback.onFailure(Collections.singletonList(new ApiResult.ApiError(error)));
        }

        static <T> void failure(ApiCallback callback, VolleyError error) {
            ApiResult result = JavaObjectRequest.parseVolleyError(error);

            if (result == null) failure(callback, error.getMessage());
            else callback.onFailure(result.getErrors());
        }

    }
}
