package ch.epfl.sweng.eventmanager.ticketing;

import android.content.Context;
import ch.epfl.sweng.eventmanager.repository.data.EventTicketingConfiguration;
import ch.epfl.sweng.eventmanager.ticketing.data.ApiResult;
import ch.epfl.sweng.eventmanager.ticketing.data.LoginResponse;
import ch.epfl.sweng.eventmanager.ticketing.data.ScanConfiguration;
import ch.epfl.sweng.eventmanager.ticketing.data.ScanResult;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.squareup.moshi.Types;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * @author Louis Vialar
 */
public class BackendService {
    private final EventTicketingConfiguration configuration;
    private final RequestQueue queue;
    private String token;

    public BackendService(EventTicketingConfiguration configuration, Context context) {
        this.configuration = configuration;
        this.queue = Volley.newRequestQueue(context.getApplicationContext());
    }

    public boolean requiresLogin() {
        return configuration.getLoginUrl() != null;
    }

    public boolean hasMultipleConfigurations() {
        return configuration.getConfigurationsUrl() != null;
    }

    public void login(String email, String password, ApiCallback<Void> callback) throws JSONException {
        if (!requiresLogin()) {
            callback.onSuccess(null);
            return;
        }

        JavaObjectRequest<LoginResponse> request =
                JavaObjectRequest.withBody(Request.Method.POST, configuration.getLoginUrl(),
                        new JSONObject().put("email", email).put("password", password),
                        success -> {
                            if (success.isSuccess()) {
                                this.token = success.getToken();
                                callback.onSuccess(null);
                            } else {
                                callback.onFailure(success.getErrors());
                            }
                        }, callback::onFailure, LoginResponse.class);

        queue.add(request);
    }

    public void getConfigurations(ApiCallback<List<ScanConfiguration>> callback) {
        if (!hasMultipleConfigurations()) {
            throw new UnsupportedOperationException("this event doesn't support multiple scan configurations");
        }

        Type type = Types.newParameterizedType(List.class, ScanConfiguration.class);
        JavaObjectRequest<List<ScanConfiguration>> request =
                new JavaObjectRequest<>(Request.Method.GET, configuration.getConfigurationsUrl(),
                        callback::onSuccess, callback::onFailure, type);

        if (requiresLogin()) {
            if (!isLoggedIn())
                // TODO: replace with error message
                throw new UnsupportedOperationException("login required");
            request.setAuthToken(token);
        }

        queue.add(request);
    }

    public void scan(String barcode, ApiCallback<ScanResult> callback) throws JSONException {
        scan(-1, barcode, callback);
    }

    public void scan(int configId, String barcode, ApiCallback<ScanResult> callback) throws JSONException {
        if (configId != -1 && !hasMultipleConfigurations()) {
            throw new UnsupportedOperationException("this event doesn't support multiple scan configurations");
        } else if (configId == -1 && hasMultipleConfigurations()) {
            throw new UnsupportedOperationException("a configId is required");
        }

        String scanUrl = configuration.getScanUrl();
        scanUrl = scanUrl.replaceAll(":configId", configId + "");

        JavaObjectRequest<ScanResult> request =
                JavaObjectRequest.withBody(Request.Method.POST, scanUrl, new JSONObject().put("barcode", barcode),
                        callback::onSuccess, callback::onFailure, ScanResult.class);

        if (requiresLogin()) {
            if (!isLoggedIn())
                // TODO: replace with error message
                throw new UnsupportedOperationException("login required");
            request.setAuthToken(token);
        }

        queue.add(request);
    }

    public boolean isLoggedIn() {
        if (requiresLogin()) {
            try {
                DecodedJWT jwt = JWT.decode(token);
                Date exp = jwt.getExpiresAt();

                return exp == null || exp.after(new Date()); // token is not expired yet
            } catch (JWTDecodeException exception) {
                // Not a JWT token
                // We consider the token as valid for ever
                return true;
            }
        } else return true;
    }

    public static interface ApiCallback<T> {
        void onSuccess(T data);

        void onFailure(List<ApiResult.ApiError> errors);

        default void onFailure(String error) {
            onFailure(Collections.singletonList(new ApiResult.ApiError(error)));
        }

        default void onFailure(VolleyError error) {
            ApiResult result = JavaObjectRequest.parseVolleyError(error);

            if (result == null) onFailure(error.getMessage());
            else onFailure(result.getErrors());
        }
    }
}
