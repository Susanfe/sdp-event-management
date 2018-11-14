package ch.epfl.sweng.eventmanager.ui;

import android.os.SystemClock;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.intent.Intents;
import android.support.test.espresso.intent.matcher.IntentMatchers;
import android.support.test.rule.ActivityTestRule;

import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import ch.epfl.sweng.eventmanager.ui.eventSelector.EventPickingActivity;
import ch.epfl.sweng.eventmanager.ui.ticketing.TicketingActivity;
import ch.epfl.sweng.eventmanager.ui.ticketing.TicketingConfigurationPickerActivity;
import ch.epfl.sweng.eventmanager.ui.userManager.LoginActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

public class EventPickerActivityTest {

    @Rule
    public final ActivityTestRule<EventPickingActivity> mActivityRule =
            new ActivityTestRule<>(EventPickingActivity.class);

    @Before
    public void startIntents() {
        Intents.init();
    }

    @After
    public void stopIntents() {
        Intents.release();
    }

    @Test
    public void testGoToLogin() {

        openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getTargetContext());

        SystemClock.sleep(100);

        onView(withText("Sign in"))
                .perform(click());

        Intents.intended(IntentMatchers.hasComponent(LoginActivity.class.getName()));
    }
}