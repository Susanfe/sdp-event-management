package ch.epfl.sweng.eventmanager.ticketing;

import ch.epfl.sweng.eventmanager.repository.data.EventTicketingConfiguration;
import ch.epfl.sweng.eventmanager.ticketing.data.ApiResult;
import ch.epfl.sweng.eventmanager.ticketing.data.LoginResponse;
import ch.epfl.sweng.eventmanager.ticketing.data.ScanConfiguration;
import ch.epfl.sweng.eventmanager.ticketing.data.ScanResult;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.google.gson.reflect.TypeToken;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collections;
import java.util.List;

/**
 * @author Louis Vialar
 */
public class TicketingService {

    private final EventTicketingConfiguration configuration;
    private final RequestQueue queue;
    private final TokenStorage storage;

    protected TicketingService(EventTicketingConfiguration configuration, TokenStorage storage, RequestQueue queue) {
        this.configuration = configuration;
        this.storage = storage;
        this.queue = queue;
    }

    boolean requiresLogin() {
        return configuration.getLoginUrl() != null;
    }

    public boolean hasMultipleConfigurations() {
        return configuration.getConfigurationsUrl() != null;
    }

    public void login(String email, String password, ApiCallback<Void> callback) {
        if (!requiresLogin()) {
            callback.onSuccess(null);
            return;
        }

        JavaObjectRequest<LoginResponse> request;

        try {
            request = JavaObjectRequest.withBody(Request.Method.POST, configuration.getLoginUrl(),
                    new JSONObject().put("email", email).put("password", password),
                    success -> {
                        if (success.isSuccess()) {
                            this.storage.setToken(success.getToken());
                            callback.onSuccess(null);
                        } else {
                            callback.onFailure(success.getErrors());
                        }
                    }, f -> ApiCallback.failure(callback, f), LoginResponse.class);

            queue.add(request);
        } catch (JSONException e) {
            e.printStackTrace();
            ApiCallback.failure(callback, e.getMessage());
        }

    }

    public void getConfigurations(ApiCallback<List<ScanConfiguration>> callback) throws NotAuthenticatedException {
        if (!hasMultipleConfigurations()) {
            throw new UnsupportedOperationException("this event doesn't support multiple scan configurations");
        }

        TypeToken<List<ScanConfiguration>> tt = new TypeToken<List<ScanConfiguration>>() {
        };
        JavaObjectRequest<List<ScanConfiguration>> request =
                new JavaObjectRequest<>(Request.Method.GET, configuration.getConfigurationsUrl(),
                        callback::onSuccess, f -> ApiCallback.failure(callback, f), tt.getType());

        setToken(request);

        queue.add(request);
    }

    private void setToken(JavaObjectRequest request) throws NotAuthenticatedException {
        if (requiresLogin()) {
            if (!isLoggedIn())
                throw new NotAuthenticatedException();
            request.setAuthToken(storage.getToken());
        }
    }

    public void scan(int configId, String barcode, ApiCallback<ScanResult> callback) throws NotAuthenticatedException {
        if (configId != -1 && !hasMultipleConfigurations()) {
            throw new UnsupportedOperationException("this event doesn't support multiple scan configurations");
        } else if (configId == -1 && hasMultipleConfigurations()) {
            throw new UnsupportedOperationException("a configId is required");
        }

        String scanUrl = configuration.getScanUrl();
        scanUrl = scanUrl.replaceAll(":configId", configId + "");

        JavaObjectRequest<ScanResult> request;
        try {
            request = JavaObjectRequest.withBody(Request.Method.POST, scanUrl, new JSONObject().put("barcode", barcode),
                    callback::onSuccess, f -> ApiCallback.failure(callback, f), ScanResult.class);

            setToken(request);
            queue.add(request);
        } catch (JSONException e) {
            e.printStackTrace();
            ApiCallback.failure(callback, e.getMessage());
        }
    }

    public boolean isLoggedIn() {
        return !requiresLogin() || storage.isLoggedIn();
    }

    public interface ApiCallback<T> {
        static <T> void failure(ApiCallback callback, String error) {
            callback.onFailure(Collections.singletonList(new ApiResult.ApiError(error)));
        }

        static <T> void failure(ApiCallback callback, VolleyError error) {
            ApiResult result = JavaObjectRequest.parseVolleyError(error);

            if (result == null) failure(callback, error.getMessage());
            else callback.onFailure(result.getErrors());
        }

        void onSuccess(T data);

        void onFailure(List<ApiResult.ApiError> errors);

    }

    public void logout() {
        storage.setToken(null);
    }

}
