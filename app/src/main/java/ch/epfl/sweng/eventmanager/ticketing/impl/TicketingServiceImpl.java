package ch.epfl.sweng.eventmanager.ticketing.impl;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import ch.epfl.sweng.eventmanager.repository.data.EventTicketingConfiguration;
import ch.epfl.sweng.eventmanager.ticketing.JavaObjectRequest;
import ch.epfl.sweng.eventmanager.ticketing.NotAuthenticatedException;
import ch.epfl.sweng.eventmanager.ticketing.TicketingService;
import ch.epfl.sweng.eventmanager.ticketing.data.LoginResponse;
import ch.epfl.sweng.eventmanager.ticketing.data.ScanConfiguration;
import ch.epfl.sweng.eventmanager.ticketing.data.ScanResult;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.auth0.android.jwt.DecodeException;
import com.auth0.android.jwt.JWT;
import com.google.gson.reflect.TypeToken;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.List;

/**
 * @author Louis Vialar
 */
public class TicketingServiceImpl implements TicketingService {
    private static final String PREFERENCE_KEY = "saved_login_tokens";

    private final EventTicketingConfiguration configuration;
    private final RequestQueue queue;
    private final SharedPreferences preferences;
    private final int eventId;
    private String token;

    TicketingServiceImpl(EventTicketingConfiguration configuration, int eventId, Context context) {
        this.configuration = configuration;
        this.queue = Volley.newRequestQueue(context.getApplicationContext());
        this.preferences = context.getSharedPreferences(PREFERENCE_KEY, Context.MODE_PRIVATE);
        this.eventId = eventId;

        readTokenFromPreferences();
    }

    private void readTokenFromPreferences() {
        // Retrieve token if it exists
        if (preferences.contains(String.valueOf(eventId))) {
            this.token = preferences.getString(String.valueOf(eventId), null);
        }
    }

    private void writeTokenToPreferences() {
        preferences.edit().putString(String.valueOf(eventId), token).apply();
    }

    @Override
    public boolean requiresLogin() {
        return configuration.getLoginUrl() != null;
    }

    @Override
    public boolean hasMultipleConfigurations() {
        return configuration.getConfigurationsUrl() != null;
    }

    @Override
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
                            this.writeTokenToPreferences();
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

    @Override
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
            request.setAuthToken(token);
        }
    }

    @Override
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

    @Override
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


}
