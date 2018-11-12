package ch.epfl.sweng.eventmanager.ticketing;

import ch.epfl.sweng.eventmanager.repository.data.EventTicketingConfiguration;
import ch.epfl.sweng.eventmanager.ticketing.data.ApiResult;
import ch.epfl.sweng.eventmanager.ticketing.data.ScanResult;
import com.android.volley.toolbox.BaseHttpStack;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * @author Louis Vialar
 */
public class MultipleTicketingServiceTest {
    private EventTicketingConfiguration configuration = new EventTicketingConfiguration(
            null, null, TicketingHelper.SCAN_URL
    );

    private TicketingService service;
    private BaseHttpStack stack;

    public static final ScanResult.Product PRODUCT = new ScanResult.Product("FP", "Descr");
    public static final ScanResult.Client CLIENT = new ScanResult.Client("Dupont", "Jean", "jean.dupont@france.fr");
    public static final int AMOUNT = 10;

    @Before
    public void setUp() throws Exception {
        Map<String, ScanResult> VALID_CODES = new HashMap<>();
        VALID_CODES.put("ABCDEFGHI", new ScanResult(
                PRODUCT, null, CLIENT
        ));

        Map<ScanResult.Product, Integer> map = new HashMap<>();
        map.put(PRODUCT, AMOUNT);

        VALID_CODES.put("123456789", new ScanResult(
                null, map, CLIENT
        ));

        stack = new BasicTicketingHttpStack(VALID_CODES);
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
        TestingCallback<Void> callback = TestingCallback.expectSuccess(v -> {});
        service.login(null, null, callback);

        callback.assertOk("login");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void getConfigurationsTest() throws Exception {
        service.getConfigurations(null);
    }

    @Test
    public void scanTest() throws Exception {
        testScan("ABCDEFGHI", TestingCallback.expectSuccess(result -> {
            assertTrue(result.isSuccess());
                assertEquals(PRODUCT, result.getProduct());
                assertEquals(CLIENT, result.getUser());
        }));
        testScan("123456789", TestingCallback.expectSuccess(result -> {
            assertTrue(result.isSuccess());
            assertEquals(10, (int) result.getProducts().get(PRODUCT));
                assertEquals(CLIENT, result.getUser());
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

        callback.assertOk("code " + code);
    }

    @Test
    public void isLoggedInTest() throws Exception {
        assertTrue(service.isLoggedIn());
    }


}