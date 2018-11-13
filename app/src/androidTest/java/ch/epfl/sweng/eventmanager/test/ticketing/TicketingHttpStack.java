package ch.epfl.sweng.eventmanager.test.ticketing;

import android.support.annotation.Nullable;
import android.util.Log;
import ch.epfl.sweng.eventmanager.ticketing.data.LoginResponse;
import ch.epfl.sweng.eventmanager.ticketing.data.ScanConfiguration;
import ch.epfl.sweng.eventmanager.ticketing.data.ScanResult;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.BaseHttpStack;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.HttpResponse;
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
public abstract class TicketingHttpStack extends BaseHttpStack {
    private static final String TAG = TicketingHttpStack.class.getSimpleName();
    public static String LOGIN_URL = "https://local/login";
    public static String SCAN_URL = "https://local/scan";
    public static String SCAN_CONFIG_PREFIX = SCAN_URL + "/";
    public static String SCAN_CONFIG_URL = SCAN_CONFIG_PREFIX + ":configId";
    public static String CONFIGS_URL = "https://local/configs";

    public abstract LoginResponse generateLoginResponse(String userName, String password) throws TicketingApiException;

    public abstract ScanResult generateScanResult(String barcode, @Nullable Integer configId, @Nullable String authToken) throws TicketingApiException;

    public abstract List<ScanConfiguration> generateConfigurations(@Nullable String authToken) throws TicketingApiException;

    @Override
    public final HttpResponse executeRequest(Request<?> request, Map<String, String> additionalHeaders) throws IOException, AuthFailureError {
        String url = request.getUrl();

        String jsonString;
        JSONObject object;
        try {
            if (request.getBody() != null) {
                jsonString = new String(request.getBody(), HttpHeaderParser.parseCharset(request.getHeaders()));
                object = new JSONObject(jsonString);
            } else {
                object = new JSONObject();
            }

            String out = "";
            Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();
            String authToken = null;
            if (request.getHeaders().containsKey("authorization")) {
                authToken = request.getHeaders().get("authorization").substring("Bearer ".length());
            } else if (additionalHeaders.containsKey("authorization")) {
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
            } else if (url.startsWith(SCAN_CONFIG_PREFIX)) {
                int scanConfigId = Integer.parseInt(url.substring(SCAN_CONFIG_PREFIX.length()));

                Log.i(TAG, "Found config id " + scanConfigId + " in url " + url);

                out = gson.toJson(generateScanResult(object.getString("barcode"), scanConfigId, authToken));
            } else {
                Log.w(TAG, "Found invalid url " + url);

                return new HttpResponse(404, Collections.emptyList());
            }

            Log.i(TAG, "Replying with " + out);

            byte[] arr = out.getBytes();
            InputStream in = new ByteArrayInputStream(arr);

            return new HttpResponse(200, Collections.emptyList(), arr.length, in);
        } catch (TicketingApiException e) {
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
