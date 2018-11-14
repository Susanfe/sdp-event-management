package ch.epfl.sweng.eventmanager.ui.ticketing.activities;

import android.os.SystemClock;
import android.support.test.espresso.intent.Intents;
import android.support.test.espresso.intent.matcher.IntentMatchers;
import ch.epfl.sweng.eventmanager.R;
import ch.epfl.sweng.eventmanager.ui.ticketing.TicketingActivity;
import ch.epfl.sweng.eventmanager.ui.ticketing.TicketingConfigurationPickerActivity;
import ch.epfl.sweng.eventmanager.ui.ticketing.TicketingScanActivity;
import org.hamcrest.Matchers;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.*;

public class PickerTest extends ActivityTest<TicketingConfigurationPickerActivity> {


    public PickerTest() {
        super(2, TicketingConfigurationPickerActivity.class);
    }

    @Test
    public void testDisplaysConfigs() {
        SystemClock.sleep(200);

        onView(withId(R.id.recylcer)).check(matches(Matchers.allOf(
                hasDescendant(withText("Config1")),
                hasDescendant(withText("Config2"))
        )));

        // Click on one
        onView(withText("Config1")).perform(click());


        Intents.intended(Matchers.allOf(
                IntentMatchers.hasComponent(TicketingScanActivity.class.getName()),
                IntentMatchers.hasExtra(TicketingActivity.SELECTED_EVENT_ID, eventId),
                IntentMatchers.hasExtra(TicketingScanActivity.SELECTED_CONFIG_ID, 1),
                IntentMatchers.hasExtra(TicketingActivity.TICKETING_CONFIGURATION, getConfiguration())
        ));

        Intents.assertNoUnverifiedIntents();
    }
}