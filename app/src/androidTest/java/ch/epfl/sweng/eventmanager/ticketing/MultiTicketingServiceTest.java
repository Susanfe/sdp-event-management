package ch.epfl.sweng.eventmanager.ticketing;

import ch.epfl.sweng.eventmanager.repository.data.EventTicketingConfiguration;
import ch.epfl.sweng.eventmanager.ticketing.data.ApiResult;
import ch.epfl.sweng.eventmanager.ticketing.data.ScanConfiguration;
import ch.epfl.sweng.eventmanager.ticketing.data.ScanResult;
import com.android.volley.toolbox.BaseHttpStack;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * @author Louis Vialar
 */
public class MultiTicketingServiceTest {
    private EventTicketingConfiguration configuration = new EventTicketingConfiguration(
            null, TicketingHelper.CONFIGS_URL, TicketingHelper.SCAN_CONFIG_URL + ":configId"
    );

    private TicketingService service;
    private BaseHttpStack stack;

    public static final ScanResult.Product PRODUCT = new ScanResult.Product("FP", "Descr");
    public static final ScanResult.Client CLIENT = new ScanResult.Client("Dupont", "Jean", "jean.dupont@france.fr");
    public static final List<ScanConfiguration> CONFIGS = new ArrayList<>();

    static {
        for (int i = 1; i <= 3; ++i) {
            CONFIGS.add(new ScanConfiguration(i, "Config" + i));
        }
    }

    @Before
    public void setUp() throws Exception {
        Map<String, ScanResult> CONFIG_1 = new HashMap<>();
        CONFIG_1.put("ABCDEFGHI", new ScanResult(
                PRODUCT, null, CLIENT
        ));

        Map<String, ScanResult> CONFIG_2 = new HashMap<>();
        CONFIG_2.put("123456789", new ScanResult(
                PRODUCT, null, CLIENT
        ));

        Map<String, ScanResult> CONFIG_3 = new HashMap<>();
        CONFIG_3.putAll(CONFIG_1);
        CONFIG_3.putAll(CONFIG_2);

        stack = new MultiHttpStack(
                new MultiHttpStack.ScanConfigurationStack(CONFIG_1, CONFIGS.get(0)),
                new MultiHttpStack.ScanConfigurationStack(CONFIG_2, CONFIGS.get(1)),
                new MultiHttpStack.ScanConfigurationStack(CONFIG_3, CONFIGS.get(2))
        );

        service = TicketingHelper.getService(configuration, stack);
    }

    @Test
    public void hasMultipleConfigurationsTest() throws Exception {
        assertTrue(service.hasMultipleConfigurations());
    }

    @Test
    public void getConfigurationsTest() throws Exception {
        TestingCallback<List<ScanConfiguration>> callback = TestingCallback.expectSuccess(list -> {
            assertEquals(CONFIGS, list);
        });
        service.getConfigurations(callback);
        callback.assertOk("get configurations");
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