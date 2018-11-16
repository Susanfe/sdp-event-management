package ch.epfl.sweng.eventmanager.ticketing;

import ch.epfl.sweng.eventmanager.test.ticketing.TestingCallback;
import ch.epfl.sweng.eventmanager.ticketing.data.ApiResult;
import ch.epfl.sweng.eventmanager.ticketing.data.ScanResult;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static ch.epfl.sweng.eventmanager.test.ticketing.MockStacks.*;
/**
 * @author Louis Vialar
 */
public class TicketingServiceTest {
    private TicketingService service;
    @Before
    public void setUp() throws Exception {
        service = TicketingHelper.getService(BASIC_CONFIGURATION);
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
        TestingCallback<Void> callback = TestingCallback.expectSuccess(TestingCallback.accept());
        service.login(null, null, callback);

        callback.assertOk("login");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void getConfigurationsTest() throws Exception {
        service.getConfigurations(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void scanWithConfigurationTest() throws Exception {
        service.scan(2, "ABCDEFGHI", TestingCallback.alwaysFail());
    }

    @Test
    public void scanTest() throws Exception {
        testScan(SINGLE_BARCODE, TestingCallback.expectSuccess(result -> {
            assertTrue(result.isSuccess());
                assertEquals(PRODUCT, result.getProduct());
                assertEquals(CLIENT, result.getUser());
        }));
        testScan(MULTIPLE_BARCODE, TestingCallback.expectSuccess(result -> {
            assertTrue(result.isSuccess());
            assertEquals(10, (int) result.getProducts().get(PRODUCT));
                assertEquals(CLIENT, result.getUser());
        }));
        testScan("NOT EXISTING", TestingCallback.expectErrors(errors -> {
            ApiResult.ApiError err = errors.get(0);
            assertEquals("", err.getKey());
            assertEquals(ErrorCodes.NOT_FOUND.getCode(), err.getMessages().get(0));
        }));
        testScan(SINGLE_BARCODE, TestingCallback.expectErrors(errors -> {
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