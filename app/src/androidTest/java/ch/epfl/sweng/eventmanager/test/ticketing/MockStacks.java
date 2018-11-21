package ch.epfl.sweng.eventmanager.test.ticketing;

import ch.epfl.sweng.eventmanager.repository.data.EventTicketingConfiguration;
import ch.epfl.sweng.eventmanager.ticketing.data.ScanConfiguration;
import ch.epfl.sweng.eventmanager.ticketing.data.ScanResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Louis Vialar
 */
public class MockStacks {
    public static final String SINGLE_BARCODE = "ABCDEFGHI";
    public static final String MULTIPLE_BARCODE = "123456789";

    public static final Map<String, ScanResult> CONFIG_1 = new HashMap<>();
    public static final Map<String, ScanResult> CONFIG_2 = new HashMap<>();
    public static final Map<String, ScanResult> CONFIG_3 = new HashMap<>();
    public static final ScanResult.Product PRODUCT = new ScanResult.Product("FP", "Descr");
    public static final Map<ScanResult.Product, Integer> MULTIPLE_PRODUCTS = new HashMap<>();
    public static final ScanResult.Client CLIENT = new ScanResult.Client("Dupont", "Jean", "jean.dupont@france.fr");
    public static final int AMOUNT = 10;
    public static final List<ScanConfiguration> CONFIGS = new ArrayList<>();
    public static final String AUTHORIZED_USER = "authorized_user@domain.tld";
    public static final String UNAUTHORIZED_USER = "unauthorized_user@domain.tld";
    public static final String PASSWORD = "P@ssword";
    public static final Map<String, AuthentifiedHttpStack.User> USER_MAP = new HashMap<>();

    public static final StackCreator BASIC_STACK;
    public static final StackCreator MULTI_STACK;
    public static final StackCreator AUTH_BASIC_STACK;
    public static final StackCreator AUTH_MULTI_STACK;

    public static final EventTicketingConfiguration BASIC_CONFIGURATION = new EventTicketingConfiguration(null, null, TicketingHttpStack.SCAN_URL);
    public static final EventTicketingConfiguration MULTI_CONFIGURATION = new EventTicketingConfiguration(null, TicketingHttpStack.CONFIGS_URL, TicketingHttpStack.SCAN_CONFIG_URL);
    public static final EventTicketingConfiguration AUTH_BASIC_CONFIGURATION = new EventTicketingConfiguration(TicketingHttpStack.LOGIN_URL, null, TicketingHttpStack.SCAN_URL);
    public static final EventTicketingConfiguration AUTH_MULTI_CONFIGURATION = new EventTicketingConfiguration(TicketingHttpStack.LOGIN_URL, TicketingHttpStack.CONFIGS_URL, TicketingHttpStack.SCAN_CONFIG_URL);

    public static final Map<EventTicketingConfiguration, StackCreator> STACKS = new HashMap<>();

    static {
        for (int i = 1; i <= 3; ++i) {
            CONFIGS.add(new ScanConfiguration(i, "Config" + i));
        }


        CONFIG_1.put(SINGLE_BARCODE, new ScanResult(
                PRODUCT, null, CLIENT
        ));

        MULTIPLE_PRODUCTS.put(PRODUCT, AMOUNT);

        CONFIG_2.put(MULTIPLE_BARCODE, new ScanResult(
                null, MULTIPLE_PRODUCTS, CLIENT
        ));

        CONFIG_3.putAll(CONFIG_1);
        CONFIG_3.putAll(CONFIG_2);

        MULTI_STACK = () -> new MultiHttpStack(
                new MultiHttpStack.ScanConfigurationStack(CONFIG_1, CONFIGS.get(0)),
                new MultiHttpStack.ScanConfigurationStack(CONFIG_2, CONFIGS.get(1)),
                new MultiHttpStack.ScanConfigurationStack(CONFIG_3, CONFIGS.get(2))
        );

        BASIC_STACK = () -> new BasicTicketingHttpStack(CONFIG_3);

        USER_MAP.put(AUTHORIZED_USER, new AuthentifiedHttpStack.User(PASSWORD, true));
        USER_MAP.put(UNAUTHORIZED_USER, new AuthentifiedHttpStack.User(PASSWORD, false));

        AUTH_BASIC_STACK = () -> new AuthentifiedHttpStack(BASIC_STACK.create(), USER_MAP);
        AUTH_MULTI_STACK = () -> new AuthentifiedHttpStack(MULTI_STACK.create(), USER_MAP);

        STACKS.put(BASIC_CONFIGURATION, BASIC_STACK);
        STACKS.put(MULTI_CONFIGURATION, MULTI_STACK);
        STACKS.put(AUTH_BASIC_CONFIGURATION, AUTH_BASIC_STACK);
        STACKS.put(AUTH_MULTI_CONFIGURATION, AUTH_MULTI_STACK);
    }

    public interface StackCreator {
        TicketingHttpStack create();
    }
}
