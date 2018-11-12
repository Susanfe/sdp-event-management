package ch.epfl.sweng.eventmanager.ticketing;

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
    public abstract LoginResponse generateLoginResponse(String userName, String password) throws TicketingApiException;

    public abstract ScanResult generateScanResult(String barcode, @Nullable Integer configId, @Nullable String authToken) throws TicketingApiException;

    public abstract List<ScanConfiguration> generateConfigurations(@Nullable String authToken) throws TicketingApiException;

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

            if (url.equalsIgnoreCase(TicketingHelper.LOGIN_URL)) {
                String email = object.getString("email");
                String password = object.getString("password");

                out = gson.toJson(generateLoginResponse(email, password));
            } else if (url.equalsIgnoreCase(TicketingHelper.CONFIGS_URL)) {
                out = gson.toJson(generateConfigurations(authToken));
            } else if (url.equalsIgnoreCase(TicketingHelper.SCAN_URL)) {
                out = gson.toJson(generateScanResult(object.getString("barcode"), null, authToken));
            } else if (url.startsWith(TicketingHelper.SCAN_CONFIG_URL)) {
                int scanConfigId = Integer.parseInt(url.substring(TicketingHelper.SCAN_CONFIG_URL.length()));

                out = gson.toJson(generateScanResult(object.getString("barcode"), scanConfigId, authToken));
            } else {
                return new HttpResponse(404, Collections.emptyList());
            }

            Log.i("TicketingHelper", "Replying with " + out);

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
