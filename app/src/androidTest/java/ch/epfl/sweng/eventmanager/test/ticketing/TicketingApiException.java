package ch.epfl.sweng.eventmanager.test.ticketing;

import ch.epfl.sweng.eventmanager.ticketing.data.ApiResult;

/**
 * @author Louis Vialar
 */
public class TicketingApiException extends Exception {
    private final ApiResult result;
    private final int code;

    public TicketingApiException(ApiResult result, int code) {
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
