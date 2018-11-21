package ch.epfl.sweng.eventmanager.ticketing;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import ch.epfl.sweng.eventmanager.test.ticketing.TestingCallback;
import ch.epfl.sweng.eventmanager.ticketing.data.ApiResult;
import ch.epfl.sweng.eventmanager.ticketing.data.ScanResult;

import static ch.epfl.sweng.eventmanager.test.ticketing.MockStacks.AUTHORIZED_USER;
import static ch.epfl.sweng.eventmanager.test.ticketing.MockStacks.AUTH_BASIC_CONFIGURATION;
import static ch.epfl.sweng.eventmanager.test.ticketing.MockStacks.CLIENT;
import static ch.epfl.sweng.eventmanager.test.ticketing.MockStacks.PASSWORD;
import static ch.epfl.sweng.eventmanager.test.ticketing.MockStacks.PRODUCT;
import static ch.epfl.sweng.eventmanager.test.ticketing.MockStacks.UNAUTHORIZED_USER;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Louis Vialar
 */
public class AuthentifiedTicketingServiceTest {
    private TicketingService service;

    @Before
    public void setUp() {
        service = TicketingHelper.getService(AUTH_BASIC_CONFIGURATION);
    }

    @Test
    public void requiresLoginTest() {
        assertTrue(service.requiresLogin());
    }

    @Test
    public void hasMultipleConfigurationsTest() {
        assertFalse(service.hasMultipleConfigurations());
    }

    @Test
    public void loginTest() {
        assertFalse(service.isLoggedIn());

        TestingCallback<Void> callback = TestingCallback.expectSuccess(TestingCallback.accept());
        service.login(AUTHORIZED_USER, PASSWORD, callback);

        callback.assertOk("login first account");

        assertTrue(service.isLoggedIn());

        service.logout();
        assertFalse(service.isLoggedIn());

        callback = TestingCallback.expectSuccess(TestingCallback.accept());
        service.login(UNAUTHORIZED_USER, PASSWORD, callback);

        callback.assertOk("login second account");
    }

    @Test(expected = NotAuthenticatedException.class)
    public void scanLoggedOut() throws Exception {
        service.scan(-1, "anything", new TicketingService.ApiCallback<ScanResult>() {
            @Override
            public void onSuccess(ScanResult data) {

            }

            @Override
            public void onFailure(List<ApiResult.ApiError> errors) {

            }
        });
    }

    @Test
    public void scanTest() throws Exception {
        TestingCallback<Void> callback = TestingCallback.expectSuccess(TestingCallback.accept());
        service.login(UNAUTHORIZED_USER, PASSWORD, callback);
        callback.assertOk("login unauthorized");

        testScan("ABCDEFGHI", TestingCallback.expectErrors(errors -> {
            ApiResult.ApiError err = errors.get(0);
            assertEquals(ErrorCodes.PERMS_MISSING.getCode(), err.getMessages().get(0));
        }));

        service.logout();
        callback = TestingCallback.expectSuccess(TestingCallback.accept());
        service.login(AUTHORIZED_USER, PASSWORD, callback);
        callback.assertOk("login authorized");

        testScan("ABCDEFGHI", TestingCallback.expectSuccess(result -> {
            assertTrue(result.isSuccess());
            assertEquals(PRODUCT, result.getProduct());
            assertEquals(CLIENT, result.getUser());
        }));
    }

    private void testScan(String code, TestingCallback<ScanResult> callback) throws NotAuthenticatedException {
        service.scan(-1, code, callback);

        callback.assertOk("scan " + code);
    }
}