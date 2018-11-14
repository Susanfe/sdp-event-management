package ch.epfl.sweng.eventmanager.ui.ticketing.activities;

import android.os.SystemClock;
import android.support.test.espresso.intent.Intents;
import android.support.test.espresso.intent.matcher.IntentMatchers;
import ch.epfl.sweng.eventmanager.R;
import ch.epfl.sweng.eventmanager.test.repository.MockEventsRepository;
import ch.epfl.sweng.eventmanager.test.ticketing.TestingCallback;
import ch.epfl.sweng.eventmanager.ui.ticketing.*;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static ch.epfl.sweng.eventmanager.test.ticketing.MockStacks.PASSWORD;
import static ch.epfl.sweng.eventmanager.test.ticketing.MockStacks.UNAUTHORIZED_USER;

public class LoggedOutPickerTest extends ScanningTest {


    @Rule
    public TicketingTestRule<TicketingConfigurationPickerActivity> rule = new TicketingTestRule<TicketingConfigurationPickerActivity>(TicketingConfigurationPickerActivity.class, eventId, MockEventsRepository.CONFIG_BY_EVENT.get(eventId)) {
        @Override
        protected void beforeActivityLaunched() {
            Intents.init();
            super.beforeActivityLaunched();
        }

        @Override
        protected void afterActivityFinished() {
            super.afterActivityFinished();
            Intents.release();
        }
    };

    public LoggedOutPickerTest() {
        super(3);
    }


    @Before
    public void prepare() {
        getTicketingService().logout();
    }

    @Test
    public void testNotAuthenticated() {
        Assert.assertFalse(getTicketingService().isLoggedIn());

        SystemClock.sleep(100);

        Intents.intended(Matchers.allOf(
                IntentMatchers.hasComponent(TicketingLoginActivity.class.getName()),
                IntentMatchers.hasExtra(TicketingActivity.SELECTED_EVENT_ID, eventId),
                IntentMatchers.hasExtra(TicketingActivity.TICKETING_CONFIGURATION, getConfiguration())
        ));
    }
}