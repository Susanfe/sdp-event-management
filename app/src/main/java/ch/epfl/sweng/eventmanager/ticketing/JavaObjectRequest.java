package ch.epfl.sweng.eventmanager.ticketing;

import androidx.annotation.Nullable;
import ch.epfl.sweng.eventmanager.ticketing.data.ApiResult;
import com.android.volley.*;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;
import com.google.gson.Gson;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Louis Vialar
 */
public class JavaObjectRequest<RepType> extends JsonRequest<RepType> {
    private static final Gson gson = new Gson();
    private String token;
    private Type repType;

    private JavaObjectRequest(int method, String url, String body, Response.Listener<RepType> listener, @Nullable Response.ErrorListener errorListener, Type type) {
        super(method, url, body, listener, errorListener);

        this.repType = type;
    }

    public JavaObjectRequest(int method, String url, Response.Listener<RepType> listener, @Nullable Response.ErrorListener errorListener, Type repType) {
        this(method, url, null, listener, errorListener, repType);
    }

    public static <T> JavaObjectRequest<T> withBody(int method, String url, JSONObject body, Response.Listener<T> listener, @Nullable Response.ErrorListener errorListener, Class<T> repTypeClass) {
        String strBody = body.toString();
        return new JavaObjectRequest<T>(method, url, strBody, listener, errorListener, repTypeClass);
    }

    public JavaObjectRequest<RepType> setAuthToken(String token) {
        this.token = token;
        return this;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> headers = new HashMap<>(super.getHeaders());
        if (token != null)
            headers.put("authorization", "Bearer " + token);
        return headers;
    }


    public static ApiResult parseVolleyError(VolleyError error) {
        try {
            if (error == null || error.networkResponse == null || error.networkResponse.data == null)
                return null;

            String jsonString = new String(error.networkResponse.data, HttpHeaderParser.parseCharset(error.networkResponse.headers, PROTOCOL_CHARSET));

            return gson.fromJson(jsonString, ApiResult.class);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected Response<RepType> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers, PROTOCOL_CHARSET));

            return Response.success(gson.fromJson(jsonString, repType), HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        }
    }
}
