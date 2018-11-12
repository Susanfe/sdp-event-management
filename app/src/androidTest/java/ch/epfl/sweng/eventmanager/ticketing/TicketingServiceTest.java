package ch.epfl.sweng.eventmanager.ticketing;

import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;
import ch.epfl.sweng.eventmanager.repository.data.EventTicketingConfiguration;
import ch.epfl.sweng.eventmanager.ticketing.data.ApiResult;
import ch.epfl.sweng.eventmanager.ticketing.data.LoginResponse;
import ch.epfl.sweng.eventmanager.ticketing.data.ScanConfiguration;
import ch.epfl.sweng.eventmanager.ticketing.data.ScanResult;
import com.android.volley.toolbox.BaseHttpStack;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.function.Function;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Louis Vialar
 */
public class TicketingServiceTest {
    private EventTicketingConfiguration configuration = new EventTicketingConfiguration(
            null, null, "https://local/scan"
    );

    private TicketingService service;
    private BaseHttpStack stack;

    @Before
    public void setUp() throws Exception {
        stack = new ServiceStack();
        service = TicketingHelper.getService(configuration, stack);
    }

    @Test
    public void requiresLoginTest() throws Exception {
        assertFalse(service.requiresLogin());
    }

    @Test
    public void hasMultipleConfigurationsTest() throws Exception {
        assertFalse(service.hasMultipleConfigurations());
    }

    @Test
    public void loginTest() throws Exception {
        AtomicBoolean ok = new AtomicBoolean(false);
        service.login(null, null, new TicketingService.ApiCallback<Void>() {
            @Override
            public void onSuccess(Void data) {
                ok.set(true);
            }

            @Override
            public void onFailure(List<ApiResult.ApiError> errors) {

            }
        });

        assertTrue(ok.get());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void getConfigurationsTest() throws Exception {
        service.getConfigurations(null);
    }

    @Test
    public void scanTest() throws Exception {

        JSONObject body = new JSONObject();
        body.put("barcode", "coucou");
        System.out.println(body);
        System.out.println(body == null);

        testScan("ABCDEFGHI", TestingCallback.expectSuccess(result -> {
            assertTrue(result.isSuccess());
                assertEquals(ServiceStack.PRODUCT, result.getProduct());
                assertEquals(ServiceStack.CLIENT, result.getUser());
        }));
        testScan("123456789", TestingCallback.expectSuccess(result -> {
            assertTrue(result.isSuccess());
            assertEquals(10, (int) result.getProducts().get(ServiceStack.PRODUCT));
                assertEquals(ServiceStack.CLIENT, result.getUser());
        }));
        testScan("NOT EXISTING", TestingCallback.expectErrors(errors -> {
            ApiResult.ApiError err = errors.get(0);
            assertEquals("", err.getKey());
            assertEquals(ErrorCodes.NOT_FOUND.getCode(), err.getMessages().get(0));
        }));
        testScan("ABCDEFGHI", TestingCallback.expectErrors(errors -> {
            ApiResult.ApiError err = errors.get(0);
            assertEquals("", err.getKey());
            assertEquals(ErrorCodes.ALREADY_SCANNED.getCode(), err.getMessages().get(0));
        }));
    }

    private void testScan(String code, TestingCallback<ScanResult> callback) throws NotAuthenticatedException {
        service.scan(-1, code, callback);

        int total = 0;
        while (total++ < 10 && !callback.isFinished())
            SystemClock.sleep(200);

        assertTrue("scan failed " + code, callback.isOk());
    }

    @Test
    public void isLoggedInTest() throws Exception {
        assertTrue(service.isLoggedIn());
    }

    static class TestingCallback<T> implements TicketingService.ApiCallback<T> {
        private AtomicBoolean ok = new AtomicBoolean(false);
        private AtomicBoolean finished = new AtomicBoolean(false);
        private Consumer<T> validateSuccess;
        private Consumer<List<ApiResult.ApiError>> validateErrors;

        private TestingCallback(Consumer<T> validateSuccess, Consumer<List<ApiResult.ApiError>> validateErrors) {
            this.validateSuccess = validateSuccess;
            this.validateErrors = validateErrors;
        }

        static <T> TestingCallback<T> expectSuccess(Consumer<T> validateSuccess) {
            return new TestingCallback<>(validateSuccess, s -> {
                Assert.fail("Expected success but got error");
            });
        }

        static <T> TestingCallback<T> expectErrors(Consumer<List<ApiResult.ApiError>> validateErrors) {
            return new TestingCallback<>(s -> {
                Assert.fail("Expected error but got success");
            }, validateErrors);
        }

        boolean isOk() {
            return ok.get();
        }

        boolean isFinished() {
            return finished.get();
        }

        @Override
        public void onSuccess(T data) {
            Log.i("TicketingServiceTest", "Got result " + data);

            try {
                validateSuccess.accept(data);
                ok.set(true);
            } catch (AssertionError e) {
                e.printStackTrace();
            } finally {
                finished.set(true);
            }
        }

        @Override
        public void onFailure(List<ApiResult.ApiError> errors) {
            Log.i("TicketingServiceTest", "Got failure " + errors);

            try {
                validateErrors.accept(errors);
                ok.set(true);
            } catch (AssertionError e) {
                e.printStackTrace();
            } finally {
                finished.set(true);
            }
        }
    }

    public static class ServiceStack extends TicketingHelper.HttpStack {
        public static final ScanResult.Product PRODUCT = new ScanResult.Product("FP", "Descr");
        public static final ScanResult.Client CLIENT = new ScanResult.Client("Dupont", "Jean", "jean.dupont@france.fr");
        public static final int AMOUNT = 10;

        private static final Map<String, ScanResult> VALID_CODES = new HashMap<>();
        private final Set<String> invalidated = new HashSet<>();

        static {
            VALID_CODES.put("ABCDEFGHI", new ScanResult(
                    PRODUCT, null, CLIENT
            ));

            Map<ScanResult.Product, Integer> map = new HashMap<>();
            map.put(PRODUCT, AMOUNT);

            VALID_CODES.put("123456789", new ScanResult(
                    null, map, CLIENT
            ));
        }

        private final Map<String, ScanResult> validCodes = new HashMap<>();

        {
            validCodes.putAll(VALID_CODES);
        }

        public static Set<String> getValidCodes() {
            return VALID_CODES.keySet();
        }

        @Override
        public LoginResponse generateLoginResponse(String userName, String password) {
            throw new UnsupportedOperationException();
        }

        @Override
        public ScanResult generateScanResult(String barcode, @Nullable Integer configId, @Nullable String authToken) throws TicketingHelper.ApiException {
            if (invalidated.contains(barcode)) {
                throw new TicketingHelper.ApiException(new ApiResult(Collections.singletonList(new ApiResult.ApiError(
                        "", Collections.singletonList(ErrorCodes.ALREADY_SCANNED.getCode()), Collections.emptyList()
                ))), 404);
            }

            ScanResult result = validCodes.remove(barcode);

            if (result == null)
                throw new TicketingHelper.ApiException(new ApiResult(Collections.singletonList(new ApiResult.ApiError(
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
}