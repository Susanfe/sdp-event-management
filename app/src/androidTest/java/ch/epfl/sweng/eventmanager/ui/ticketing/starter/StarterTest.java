package ch.epfl.sweng.eventmanager.ui.ticketing.starter;

import android.app.Activity;
import android.app.Instrumentation;
import android.os.SystemClock;
import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.espresso.contrib.NavigationViewActions;
import android.support.test.espresso.intent.Intents;
import android.support.test.espresso.intent.matcher.IntentMatchers;
import android.view.Gravity;
import ch.epfl.sweng.eventmanager.R;
import ch.epfl.sweng.eventmanager.test.EventTestRule;
import ch.epfl.sweng.eventmanager.ui.eventShowcase.EventShowcaseActivity;
import ch.epfl.sweng.eventmanager.ui.ticketing.ScanningTest;
import ch.epfl.sweng.eventmanager.ui.ticketing.TicketingActivity;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.DrawerMatchers.isClosed;
import static android.support.test.espresso.intent.matcher.IntentMatchers.isInternal;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

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
        // Navigate
        SystemClock.sleep(200);

        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT)))
                .perform(DrawerActions.open());

        SystemClock.sleep(200);

        Intents.init();

        Intents.intending(isInternal()).respondWith(new Instrumentation.ActivityResult(Activity.RESULT_OK, null));
    }


    @After
    public void removeIntents() {
        mActivityRule.finishActivity();

        Intents.release();
    }

    @Test
    public void testOpen() {
        testOpen(this.expectedClass);
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

}
