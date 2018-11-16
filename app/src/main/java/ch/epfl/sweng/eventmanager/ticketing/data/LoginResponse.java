package ch.epfl.sweng.eventmanager.ticketing.data;

/**
 * @author Louis Vialar
 */
public class LoginResponse extends ApiResult {
    private String token;

    public LoginResponse() {
    }

    public LoginResponse(String token) {
        super(true);
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
