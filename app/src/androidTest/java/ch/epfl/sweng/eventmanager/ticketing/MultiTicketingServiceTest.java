package ch.epfl.sweng.eventmanager.ticketing;

import ch.epfl.sweng.eventmanager.test.ticketing.MockStacks;
import ch.epfl.sweng.eventmanager.test.ticketing.TestingCallback;
import ch.epfl.sweng.eventmanager.ticketing.data.ApiResult;
import ch.epfl.sweng.eventmanager.ticketing.data.ScanConfiguration;
import ch.epfl.sweng.eventmanager.ticketing.data.ScanResult;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static ch.epfl.sweng.eventmanager.test.ticketing.MockStacks.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Louis Vialar
 */
public class MultiTicketingServiceTest {
    private TicketingService service;

    @Before
    public void setUp() throws Exception {
        service = TicketingHelper.getService(MockStacks.MULTI_CONFIGURATION);
    }

    @Test
    public void hasMultipleConfigurationsTest() throws Exception {
        assertTrue(service.hasMultipleConfigurations());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void scanWithoutConfigId() throws Exception {
        service.scan(-1, "ABCDEFGHI", TestingCallback.alwaysFail());
    }

    @Test
    public void getConfigurationsTest() throws Exception {
        TestingCallback<List<ScanConfiguration>> callback = TestingCallback.expectSuccess(list -> {
            assertTrue("all configs are in the result", list.containsAll(CONFIGS));
            assertTrue("no additional configs are in the result", CONFIGS.containsAll(list));
        });
        service.getConfigurations(callback);
        callback.assertOk("create configurations");
    }

    @Test
    public void scanTest() throws Exception {
        testScan("ABCDEFGHI", 1, TestingCallback.expectSuccess(result -> {
            assertTrue(result.isSuccess());
            assertEquals(PRODUCT, result.getProduct());
            assertEquals(CLIENT, result.getUser());
        }));

        testScan("123456789", 1, TestingCallback.expectErrors(errors -> {
            ApiResult.ApiError err = errors.get(0);
            assertEquals(ErrorCodes.PRODUCT_NOT_ALLOWED.getCode(), err.getMessages().get(0));
        }));

        testScan("ABCDEFGHI", 3, TestingCallback.expectErrors(errors -> {
            ApiResult.ApiError err = errors.get(0);
            assertEquals(ErrorCodes.ALREADY_SCANNED.getCode(), err.getMessages().get(0));
        }));
    }

    private void testScan(String code, int config, TestingCallback<ScanResult> callback) throws NotAuthenticatedException {
        service.scan(config, code, callback);

        callback.assertOk("code " + code);
    }

    @Test
    public void isLoggedInTest() throws Exception {
        assertTrue(service.isLoggedIn());
    }


}