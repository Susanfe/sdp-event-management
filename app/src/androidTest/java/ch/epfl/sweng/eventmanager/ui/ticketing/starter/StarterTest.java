package ch.epfl.sweng.eventmanager.ui.ticketing.starter;

import android.app.Activity;
import android.app.Instrumentation;
import android.os.SystemClock;
import androidx.test.espresso.contrib.DrawerActions;
import androidx.test.espresso.contrib.NavigationViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.matcher.IntentMatchers;

import android.view.Gravity;
import ch.epfl.sweng.eventmanager.R;
import ch.epfl.sweng.eventmanager.test.EventTestRule;
import ch.epfl.sweng.eventmanager.test.ticketing.MockTicketingService;
import ch.epfl.sweng.eventmanager.ui.event.interaction.EventShowcaseActivity;
import ch.epfl.sweng.eventmanager.ui.ticketing.ScanningTest;
import ch.epfl.sweng.eventmanager.ui.ticketing.TicketingActivity;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.DrawerMatchers.isClosed;
import static androidx.test.espresso.intent.matcher.IntentMatchers.isInternal;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

/**
 * @author Louis Vialar
 */
public abstract class StarterTest extends ScanningTest {
    @Rule
    public final EventTestRule<EventShowcaseActivity> mActivityRule;
    private final Class expectedClass;

    protected StarterTest(int eventId, Class expectedClass) {
        super(eventId);
        this.mActivityRule = new EventTestRule<>(EventShowcaseActivity.class, eventId);
        this.expectedClass = expectedClass;
    }

    @Before
    public void setUp() {
        Intents.init();

        // Navigate
        SystemClock.sleep(200);

        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT)))
                .perform(DrawerActions.open());

        SystemClock.sleep(200);


        Intents.intending(isInternal()).respondWith(new Instrumentation.ActivityResult(Activity.RESULT_OK, null));
    }

    @Test
    public void testOpen() {
        testOpen(this.expectedClass);
    }

    @After
    public void cleanup() {
        Intents.release();
        mActivityRule.finishActivity();
    }


    protected void testOpen(Class expectedClass) {
        onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.nav_scan));

        Intents.intended(Matchers.allOf(
                IntentMatchers.hasComponent(expectedClass.getName()),
                // IntentMatchers.hasType(TicketingScanActivity.class.getTypeName()),
                IntentMatchers.hasExtra(TicketingActivity.SELECTED_EVENT_ID, eventId),
                IntentMatchers.hasExtra(TicketingActivity.TICKETING_CONFIGURATION, repository.getEvent(eventId).getValue().getTicketingConfiguration())
        ));

        Intents.assertNoUnverifiedIntents();
    }

    @Override
    public MockTicketingService getTicketingService() {
        return getOrCreateTicketingService(mActivityRule.getActivity());
    }
}
