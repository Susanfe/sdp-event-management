package ch.epfl.sweng.eventmanager.test.ticketing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import androidx.annotation.Nullable;
import ch.epfl.sweng.eventmanager.ticketing.ErrorCodes;
import ch.epfl.sweng.eventmanager.ticketing.data.ApiResult;
import ch.epfl.sweng.eventmanager.ticketing.data.LoginResponse;
import ch.epfl.sweng.eventmanager.ticketing.data.ScanConfiguration;
import ch.epfl.sweng.eventmanager.ticketing.data.ScanResult;

/**
 * @author Louis Vialar
 */
public class MultiHttpStack extends TicketingHttpStack {
    private final Set<String> invalidated = new HashSet<>();
    private final Map<Integer, ScanConfigurationStack> stackMap = new HashMap<>();

    public static class ScanConfigurationStack {
        private Map<String, ScanResult> stack;
        private ScanConfiguration configuration;

        ScanConfigurationStack(Map<String, ScanResult> stack, ScanConfiguration configuration) {
            this.stack = stack;
            this.configuration = configuration;
        }

//        public ScanConfigurationStack(String name, Map<String, ScanResult> stack) {
//            this(stack, new ScanConfiguration(id++, name));
//        }
    }

    public MultiHttpStack(ScanConfigurationStack... stacks) {
        for (ScanConfigurationStack stack : stacks)
            stackMap.put(stack.configuration.getId(), stack);
    }

    @Override
    public LoginResponse generateLoginResponse(String userName, String password) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ScanResult generateScanResult(String barcode, @Nullable Integer configId, @Nullable String authToken) throws TicketingApiException {
        if (configId == null) {
            throw new TicketingApiException(new ApiResult(Collections.emptyList()), 404);
        } else if (!stackMap.containsKey(configId)) {
            throw new TicketingApiException(new ApiResult(Collections.singletonList(new ApiResult.ApiError("config", Collections.singletonList(ErrorCodes.NOT_FOUND.getCode()), Collections.emptyList()))), 404);
        } else {
            if (invalidated.contains(barcode)) {
                throw new TicketingApiException(new ApiResult(Collections.singletonList(new ApiResult.ApiError(
                        "", Collections.singletonList(ErrorCodes.ALREADY_SCANNED.getCode()), Collections.emptyList()
                ))), 404);
            }

            ScanResult result = stackMap.get(configId).stack.remove(barcode);

            if (result == null) {
                for (ScanConfigurationStack stack : stackMap.values()) {
                    if (stack.stack.containsKey(barcode)) {
                        throw new TicketingApiException(new ApiResult(Collections.singletonList(new ApiResult.ApiError(ErrorCodes.PRODUCT_NOT_ALLOWED.getCode()))), 402);
                    }
                }

                throw new TicketingApiException(new ApiResult(Collections.singletonList(new ApiResult.ApiError(
                        "", Collections.singletonList(ErrorCodes.NOT_FOUND.getCode()), Collections.emptyList()
                ))), 404);
            }

            invalidated.add(barcode);
            return result;
        }
    }

    @Override
    public List<ScanConfiguration> generateConfigurations(@Nullable String authToken) {
        List<ScanConfiguration> list = new ArrayList<>();

        for (ScanConfigurationStack stack : stackMap.values())
            list.add(stack.configuration);

        return list;
    }
}
