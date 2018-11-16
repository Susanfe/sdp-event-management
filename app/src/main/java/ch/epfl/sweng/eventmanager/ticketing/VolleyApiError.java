package ch.epfl.sweng.eventmanager.ticketing;

import ch.epfl.sweng.eventmanager.ticketing.data.ApiResult;
import com.android.volley.VolleyError;

/**
 * @author Louis Vialar
 */
public class VolleyApiError extends VolleyError {
    private ApiResult result;

    public VolleyApiError(ApiResult result) {
        this.result = result;
    }
}
