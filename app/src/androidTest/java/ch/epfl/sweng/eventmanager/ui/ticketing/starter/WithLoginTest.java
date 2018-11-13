package ch.epfl.sweng.eventmanager.ui.ticketing.starter;

import ch.epfl.sweng.eventmanager.test.ticketing.TestingCallback;
import ch.epfl.sweng.eventmanager.ui.ticketing.TicketingConfigurationPickerActivity;
import ch.epfl.sweng.eventmanager.ui.ticketing.TicketingLoginActivity;
import org.junit.After;
import org.junit.Test;

import static ch.epfl.sweng.eventmanager.test.ticketing.MockStacks.AUTHORIZED_USER;
import static ch.epfl.sweng.eventmanager.test.ticketing.MockStacks.PASSWORD;

public class WithLoginTest extends StarterTest {


    public WithLoginTest() {
        super(3, TicketingLoginActivity.class);
    }

    @Test
    public void testAlreadyLoggedIn() {
        TestingCallback<Void> callback = TestingCallback.expectSuccess(a -> {
        });
        getOrCreateTicketingService(mActivityRule.getActivity()).login(AUTHORIZED_USER, PASSWORD, callback);
        callback.assertOk("login should succeed");

        testOpen(TicketingConfigurationPickerActivity.class);
    }

    @After
    public void cleanUp() {
        getOrCreateTicketingService(mActivityRule.getActivity()).logout();

        super.removeIntents();
    }
}