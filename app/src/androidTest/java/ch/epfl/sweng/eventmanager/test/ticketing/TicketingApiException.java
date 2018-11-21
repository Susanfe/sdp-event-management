package ch.epfl.sweng.eventmanager.test.ticketing;

import ch.epfl.sweng.eventmanager.ticketing.data.ApiResult;

/**
 * @author Louis Vialar
 */
public class TicketingApiException extends Exception {
    private final ApiResult result;
    private final int code;

    TicketingApiException(ApiResult result, int code) {
        this.result = result;
        this.code = code;
    }

    ApiResult getResult() {
        return result;
    }

    public int getCode() {
        return code;
    }
}
