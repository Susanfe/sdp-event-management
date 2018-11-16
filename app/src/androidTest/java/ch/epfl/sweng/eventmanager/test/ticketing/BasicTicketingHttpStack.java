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
public class BasicTicketingHttpStack extends TicketingHttpStack {
    private final Set<String> invalidated = new HashSet<>();
    private final Map<String, ScanResult> validCodes = new HashMap<>();

    public BasicTicketingHttpStack(Map<String, ScanResult> validCodes) {
        this.validCodes.putAll(validCodes);
    }

    @Override
    public LoginResponse generateLoginResponse(String userName, String password) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ScanResult generateScanResult(String barcode, @Nullable Integer configId, @Nullable String authToken) throws TicketingApiException {
        if (invalidated.contains(barcode)) {
            throw new TicketingApiException(new ApiResult(Collections.singletonList(new ApiResult.ApiError(
                    "", Collections.singletonList(ErrorCodes.ALREADY_SCANNED.getCode()), Collections.emptyList()
            ))), 404);
        }

        ScanResult result = validCodes.remove(barcode);

        if (result == null)
            throw new TicketingApiException(new ApiResult(Collections.singletonList(new ApiResult.ApiError(
                    "", Collections.singletonList(ErrorCodes.NOT_FOUND.getCode()), Collections.emptyList()
            ))), 404);

        invalidated.add(barcode);
        return result;
    }

    @Override
    public List<ScanConfiguration> generateConfigurations(@Nullable String authToken) {
        throw new UnsupportedOperationException();
    }
}
