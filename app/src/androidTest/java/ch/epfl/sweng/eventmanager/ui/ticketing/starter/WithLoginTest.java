package ch.epfl.sweng.eventmanager.ui.ticketing.starter;

import org.junit.After;
import org.junit.Test;

import java.util.Collections;
import java.util.Date;

import ch.epfl.sweng.eventmanager.repository.data.Event;
import ch.epfl.sweng.eventmanager.repository.data.EventLocation;
import ch.epfl.sweng.eventmanager.repository.data.EventOrganizer;
import ch.epfl.sweng.eventmanager.repository.data.Position;
import ch.epfl.sweng.eventmanager.test.ticketing.TestingCallback;
import ch.epfl.sweng.eventmanager.ui.ticketing.TicketingConfigurationPickerActivity;
import ch.epfl.sweng.eventmanager.ui.ticketing.TicketingLoginActivity;

import static ch.epfl.sweng.eventmanager.test.ticketing.MockStacks.AUTHORIZED_USER;
import static ch.epfl.sweng.eventmanager.test.ticketing.MockStacks.PASSWORD;

public class WithLoginTest extends StarterTest {


    public WithLoginTest() {
        super(3, TicketingLoginActivity.class);
    }

    @Test
    public void testAlreadyLoggedIn() {
        TestingCallback<Void> callback = TestingCallback.expectSuccess(TestingCallback.accept());
        getOrCreateTicketingService(mActivityRule.getActivity()).login(AUTHORIZED_USER, PASSWORD, callback);
        callback.assertOk("login should succeed");

        testOpen(TicketingConfigurationPickerActivity.class);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testTicketingManagerOnEventWithNoConfig() {
        ticketingManager.start(
                new Event(3, "Event without items B", "Description", new Date(1550307600L), new Date(1550422800L),
                        new EventOrganizer(1, "a", "b", null, "c"), null, new EventLocation("EPFL", Position.EPFL), Collections.emptyList(), Collections.emptyMap(), "JapanImpact",
                        null), mActivityRule.getActivity()
        );
    }

    @After
    public void cleanUp() {
        getOrCreateTicketingService(mActivityRule.getActivity()).logout();
    }
}