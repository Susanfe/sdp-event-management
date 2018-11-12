package ch.epfl.sweng.eventmanager.ticketing;

import ch.epfl.sweng.eventmanager.repository.data.EventTicketingConfiguration;
import ch.epfl.sweng.eventmanager.ticketing.data.ApiResult;
import ch.epfl.sweng.eventmanager.ticketing.data.ScanResult;
import com.android.volley.toolbox.BaseHttpStack;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * @author Louis Vialar
 */
public class AuthentifiedTicketingServiceTest {
    private static final ScanResult.Product PRODUCT = new ScanResult.Product("FP", "Descr");
    private static final ScanResult.Client CLIENT = new ScanResult.Client("Dupont", "Jean", "jean.dupont@france.fr");
    private static String AUTHORIZED_USER = "authorized_user";
    private static String UNAUTHORIZED_USER = "unauthorized_user";
    private static String PASSWORD = "P@ssword";
    private EventTicketingConfiguration configuration = new EventTicketingConfiguration(
            TicketingHelper.LOGIN_URL, null, TicketingHelper.SCAN_URL
    );
    private TicketingService service;
    private BaseHttpStack stack;

    @Before
    public void setUp() throws Exception {
        Map<String, ScanResult> VALID_CODES = new HashMap<>();
        VALID_CODES.put("ABCDEFGHI", new ScanResult(
                PRODUCT, null, CLIENT
        ));

        Map<String, AuthentifiedHttpStack.User> userMap = new HashMap<>();
        userMap.put(AUTHORIZED_USER, new AuthentifiedHttpStack.User(PASSWORD, true));
        userMap.put(UNAUTHORIZED_USER, new AuthentifiedHttpStack.User(PASSWORD, false));

        stack = new AuthentifiedHttpStack(new BasicTicketingHttpStack(VALID_CODES), userMap);
        service = TicketingHelper.getService(configuration, stack);
    }

    @Test
    public void requiresLoginTest() throws Exception {
        assertTrue(service.requiresLogin());
    }

    @Test
    public void hasMultipleConfigurationsTest() throws Exception {
        assertFalse(service.hasMultipleConfigurations());
    }

    @Test
    public void loginTest() {
        assertFalse(service.isLoggedIn());

        TestingCallback<Void> callback = TestingCallback.expectSuccess(v -> {
        });
        service.login(AUTHORIZED_USER, PASSWORD, callback);

        callback.assertOk("login first account");

        assertTrue(service.isLoggedIn());

        service.logout();
        assertFalse(service.isLoggedIn());

        callback = TestingCallback.expectSuccess(v -> {
        });
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
        TestingCallback<Void> callback = TestingCallback.expectSuccess(v -> {
        });
        service.login(UNAUTHORIZED_USER, PASSWORD, callback);
        callback.assertOk("login unauthorized");

        testScan("ABCDEFGHI", TestingCallback.expectErrors(errors -> {
            ApiResult.ApiError err = errors.get(0);
            assertEquals(ErrorCodes.PERMS_MISSING.getCode(), err.getMessages().get(0));
        }));

        service.logout();
        callback = TestingCallback.expectSuccess(v -> {
        });
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