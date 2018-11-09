package ch.epfl.sweng.eventmanager.ticketing;

import android.support.annotation.Nullable;
import ch.epfl.sweng.eventmanager.ticketing.data.ApiResult;
import com.android.volley.*;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;
import com.squareup.moshi.Moshi;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Louis Vialar
 */
public class JavaObjectRequest<RepType> extends JsonRequest<RepType> {
    private static final Moshi moshi = new Moshi.Builder().build();
    private String token;
    private Type repType;

    private JavaObjectRequest(int method, String url, String body, Response.Listener<RepType> listener, @Nullable Response.ErrorListener errorListener, Type type) {
        super(method, url, body, listener, errorListener);

        this.repType = type;
    }

    public JavaObjectRequest(int method, String url, Response.Listener<RepType> listener, @Nullable Response.ErrorListener errorListener, Type repType) {
        this(method, url, null, listener, errorListener, repType);
    }

    public JavaObjectRequest(int method, String url, Response.Listener<RepType> listener, @Nullable Response.ErrorListener errorListener, Class<RepType> repType) {
        this(method, url, null, listener, errorListener, repType);
    }

    public static <T, U> JavaObjectRequest<T> withBody(int method, String url, U body, Response.Listener<T> listener, @Nullable Response.ErrorListener errorListener, Class<T> repTypeClass) {
        String strBody = moshi.<U>adapter(body.getClass()).toJson(body);
        return new JavaObjectRequest<T>(method, url, strBody, listener, errorListener, repTypeClass);
    }

    public static <T, U> JavaObjectRequest<T> withBody(int method, String url, U body, Response.Listener<T> listener, @Nullable Response.ErrorListener errorListener, Type repTypeClass) {
        String strBody = moshi.<U>adapter(body.getClass()).toJson(body);
        return new JavaObjectRequest<T>(method, url, strBody, listener, errorListener, repTypeClass);
    }

    public static <T> JavaObjectRequest<T> withBody(int method, String url, JSONObject body, Response.Listener<T> listener, @Nullable Response.ErrorListener errorListener, Class<T> repTypeClass) {
        String strBody = body.toString();
        return new JavaObjectRequest<T>(method, url, strBody, listener, errorListener, repTypeClass);
    }

    public static <T> JavaObjectRequest<T> withBody(int method, String url, JSONObject body, Response.Listener<T> listener, @Nullable Response.ErrorListener errorListener, Type repTypeClass) {
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
            String jsonString = new String(error.networkResponse.data, HttpHeaderParser.parseCharset(error.networkResponse.headers, PROTOCOL_CHARSET));

            return moshi.adapter(ApiResult.class).fromJson(jsonString);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected Response<RepType> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers, PROTOCOL_CHARSET));

            return Response.success(moshi.<RepType>adapter(repType).fromJson(jsonString), HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (IOException e) {
            return Response.error(new ParseError(e));
        }
    }

    public static class Builder<RepType> {
        private int method;
        private String url;
        private String body;
        private Response.Listener<RepType> listener;
        private Response.ErrorListener errorListener;
        private Class<RepType> repTypeClass;

        private boolean built = false;

        public Builder setMethod(int method) {
            this.method = method;
            return this;
        }

        public Builder setUrl(String url) {
            this.url = url;
            return this;
        }

        public <ReqType> Builder setBody(ReqType body) {
            this.body = moshi.adapter((Class<ReqType>) body.getClass()).toJson(body);
            return this;
        }

        public Builder setListener(Response.Listener<RepType> listener) {
            this.listener = listener;
            return this;
        }

        public Builder setErrorListener(Response.ErrorListener errorListener) {
            this.errorListener = errorListener;
            return this;
        }

        public Builder setRepTypeClass(Class<RepType> repTypeClass) {
            this.repTypeClass = repTypeClass;
            return this;
        }

        public JavaObjectRequest<RepType> create() {
            if (built) {
                throw new IllegalStateException("object was already built");
            }

            if (url == null) {
                throw new IllegalStateException("missing API url");
            }

            if (listener == null) {
                throw new IllegalStateException("missing listener");
            }

            if (repTypeClass == null) {
                throw new IllegalStateException("missing repTypeClass");
            }

            JavaObjectRequest<RepType> ret = new JavaObjectRequest<>(method, url, body, listener, errorListener, repTypeClass);
            built = true;
            return ret;
        }
    }
}
