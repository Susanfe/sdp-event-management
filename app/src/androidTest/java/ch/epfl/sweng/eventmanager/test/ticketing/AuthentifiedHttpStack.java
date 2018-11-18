package ch.epfl.sweng.eventmanager.test.ticketing;

import androidx.annotation.Nullable;
import ch.epfl.sweng.eventmanager.ticketing.ErrorCodes;
import ch.epfl.sweng.eventmanager.ticketing.data.ApiResult;
import ch.epfl.sweng.eventmanager.ticketing.data.LoginResponse;
import ch.epfl.sweng.eventmanager.ticketing.data.ScanConfiguration;
import ch.epfl.sweng.eventmanager.ticketing.data.ScanResult;

import java.util.*;

/**
 * @author Louis Vialar
 */
public class AuthentifiedHttpStack extends TicketingHttpStack {
    private TicketingHttpStack baseStack;
    private Map<String, User> users = new HashMap<>();
    private Map<String, User> sessions = new HashMap<>();

    public AuthentifiedHttpStack(TicketingHttpStack baseStack, Map<String, User> users) {
        this.baseStack = baseStack;
        this.users.putAll(users);
    }

    @Override
    public LoginResponse generateLoginResponse(String userName, String password) throws TicketingApiException {
        if (users.containsKey(userName) && users.get(userName).password.equals(password)) {
            String token = UUID.randomUUID().toString();
            sessions.put(token, users.get(userName));

            return new LoginResponse(token);
        }

        throw new TicketingApiException(new ApiResult(Collections.singletonList(new ApiResult.ApiError(ErrorCodes.NOT_FOUND.getCode()))), 404);
    }

    private void checkAuthorization(String authToken) throws TicketingApiException {
        if (authToken == null || !sessions.containsKey(authToken))
            throw new TicketingApiException(new ApiResult(Collections.singletonList(new ApiResult.ApiError(ErrorCodes.AUTH_MISSING.getCode()))), 401);

        if (!sessions.get(authToken).authorized)
            throw new TicketingApiException(new ApiResult(Collections.singletonList(new ApiResult.ApiError(ErrorCodes.PERMS_MISSING.getCode()))), 403);

    }

    @Override
    public ScanResult generateScanResult(String barcode, @Nullable Integer configId, @Nullable String authToken) throws TicketingApiException {
        checkAuthorization(authToken);

        return baseStack.generateScanResult(barcode, configId, null);
    }

    @Override
    public List<ScanConfiguration> generateConfigurations(@Nullable String authToken) throws TicketingApiException {
        checkAuthorization(authToken);

        return baseStack.generateConfigurations(null);
    }

    public static class User {
        private String password;
        private boolean authorized;

        public User(String password, boolean authorized) {
            this.password = password;
            this.authorized = authorized;
        }
    }
}
