package ch.epfl.sweng.eventmanager.ticketing;

import android.content.Context;
import android.util.Log;
import ch.epfl.sweng.eventmanager.repository.data.EventTicketingConfiguration;
import ch.epfl.sweng.eventmanager.ticketing.data.ApiResult;
import ch.epfl.sweng.eventmanager.ticketing.data.LoginResponse;
import ch.epfl.sweng.eventmanager.ticketing.data.ScanConfiguration;
import ch.epfl.sweng.eventmanager.ticketing.data.ScanResult;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.auth0.android.jwt.DecodeException;
import com.auth0.android.jwt.JWT;
import com.google.gson.reflect.TypeToken;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.*;

/**
 * @author Louis Vialar
 */
public class TicketingService {
    private static final Map<EventTicketingConfiguration, TicketingService> services = new HashMap<>();

    private final EventTicketingConfiguration configuration;
    private final RequestQueue queue;
    private String token;

    private TicketingService(EventTicketingConfiguration configuration, Context context) {
        this.configuration = configuration;
        this.queue = Volley.newRequestQueue(context.getApplicationContext());
    }

    public static TicketingService getService(EventTicketingConfiguration configuration, Context context) {
        if (!services.containsKey(configuration))
            services.put(configuration, new TicketingService(configuration, context));
        return services.get(configuration);
    }

    public boolean requiresLogin() {
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
                            this.token = success.getToken();
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

    public void getConfigurations(ApiCallback<List<ScanConfiguration>> callback) {
        if (!hasMultipleConfigurations()) {
            throw new UnsupportedOperationException("this event doesn't support multiple scan configurations");
        }

        TypeToken<List<ScanConfiguration>> tt = new TypeToken<List<ScanConfiguration>>() {};
        JavaObjectRequest<List<ScanConfiguration>> request =
                new JavaObjectRequest<>(Request.Method.GET, configuration.getConfigurationsUrl(),
                        callback::onSuccess, f -> ApiCallback.failure(callback, f), tt.getType());

        if (requiresLogin()) {
            if (!isLoggedIn())
                // TODO: replace with error message
                throw new UnsupportedOperationException("login required");
            request.setAuthToken(token);
        }

        queue.add(request);
    }

    public void scan(String barcode, ApiCallback<ScanResult> callback) {
        scan(-1, barcode, callback);
    }

    public void scan(int configId, String barcode, ApiCallback<ScanResult> callback) {
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

            if (requiresLogin()) {
                if (!isLoggedIn())
                    ApiCallback.failure(callback, "requires login"); // TODO: proper error message
                else
                    request.setAuthToken(token);
            }

            queue.add(request);
        } catch (JSONException e) {
            e.printStackTrace();
            ApiCallback.failure(callback, e.getMessage());
        }
    }

    public boolean isLoggedIn() {
        if (requiresLogin()) {
            if (token == null) {
                return false; // No token == not logged in
            }

            try {
                JWT jwt = new JWT(token);
                Date exp = jwt.getExpiresAt();

                Log.i("TicketingService", "Logged in with token expiring at " + exp + " / " + jwt.toString());

                return exp == null || exp.after(new Date()); // token is not expired yet
            } catch (DecodeException exception) {
                // Not a JWT token
                // We consider the token as valid for ever
                return true;
            }
        } else return true;
    }


    public static interface ApiCallback<T> {
        void onSuccess(T data);

        void onFailure(List<ApiResult.ApiError> errors);


        static <T> void failure(ApiCallback<T> callback, String error) {
            callback.onFailure(Collections.singletonList(new ApiResult.ApiError(error)));
        }

        static <T> void failure(ApiCallback<T> callback, VolleyError error) {
            ApiResult result = JavaObjectRequest.parseVolleyError(error);

            if (result == null) failure(callback, error.getMessage());
            else callback.onFailure(result.getErrors());
        }

    }
}
