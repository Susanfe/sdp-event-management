package ch.epfl.sweng.eventmanager.ticketing;

import android.support.annotation.Nullable;
import android.util.Log;
import ch.epfl.sweng.eventmanager.repository.data.EventTicketingConfiguration;
import ch.epfl.sweng.eventmanager.ticketing.data.ApiResult;
import ch.epfl.sweng.eventmanager.ticketing.data.LoginResponse;
import ch.epfl.sweng.eventmanager.ticketing.data.ScanConfiguration;
import ch.epfl.sweng.eventmanager.ticketing.data.ScanResult;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Map;

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

    public static class ApiException extends Exception {
        private final ApiResult result;
        private final int code;

        public ApiException(ApiResult result, int code) {
            this.result = result;
            this.code = code;
        }

        public ApiResult getResult() {
            return result;
        }

        public int getCode() {
            return code;
        }
    }

    public static abstract class HttpStack extends BaseHttpStack {
        public abstract LoginResponse generateLoginResponse(String userName, String password) throws ApiException;

        public abstract ScanResult generateScanResult(String barcode, @Nullable Integer configId, @Nullable String authToken) throws ApiException;

        public abstract List<ScanConfiguration> generateConfigurations(@Nullable String authToken) throws ApiException;

        @Override
        public final HttpResponse executeRequest(Request<?> request, Map<String, String> additionalHeaders) throws IOException, AuthFailureError {
            String url = request.getUrl();

            String jsonString = new String(request.getBody(), HttpHeaderParser.parseCharset(request.getHeaders()));
            JSONObject object;
            try {
                object = new JSONObject(jsonString);

                String out = "";
                Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();
                String authToken = null;
                if (additionalHeaders.containsKey("authorization")) {
                    authToken = additionalHeaders.get("authorization").substring("Bearer ".length());
                }

                if (url.equalsIgnoreCase(LOGIN_URL)) {
                    String email = object.getString("email");
                    String password = object.getString("password");

                    out = gson.toJson(generateLoginResponse(email, password));
                } else if (url.equalsIgnoreCase(CONFIGS_URL)) {
                    out = gson.toJson(generateConfigurations(authToken));
                } else if (url.equalsIgnoreCase(SCAN_URL)) {
                    out = gson.toJson(generateScanResult(object.getString("barcode"), null, authToken));
                } else if (url.startsWith(SCAN_CONFIG_URL)) {
                    int scanConfigId = Integer.parseInt(url.substring(SCAN_CONFIG_URL.length()));

                    out = gson.toJson(generateScanResult(object.getString("barcode"), scanConfigId, authToken));
                } else {
                    return new HttpResponse(404, Collections.emptyList());
                }

                Log.i("TicketingHelper", "Replying with " + out);

                byte[] arr = out.getBytes();
                InputStream in = new ByteArrayInputStream(arr);

                return new HttpResponse(200, Collections.emptyList(), arr.length, in);
            } catch (ApiException e) {
                String out = new Gson().toJson(e.getResult());
                byte[] arr = out.getBytes();
                InputStream in = new ByteArrayInputStream(arr);
                return new HttpResponse(e.getCode(), Collections.emptyList(), arr.length, in);
            } catch (Exception e) {
                e.printStackTrace();
                return new HttpResponse(500, Collections.emptyList());
            }
        }
    }
}
