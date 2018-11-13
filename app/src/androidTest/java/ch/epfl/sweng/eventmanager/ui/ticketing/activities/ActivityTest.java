package ch.epfl.sweng.eventmanager.ui.ticketing.activities;

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
import ch.epfl.sweng.eventmanager.test.repository.MockEventsRepository;
import ch.epfl.sweng.eventmanager.ticketing.TicketingService;
import ch.epfl.sweng.eventmanager.ui.eventShowcase.EventShowcaseActivity;
import ch.epfl.sweng.eventmanager.ui.ticketing.ScanningTest;
import ch.epfl.sweng.eventmanager.ui.ticketing.TicketingActivity;
import ch.epfl.sweng.eventmanager.ui.ticketing.TicketingTestRule;
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
public abstract class ActivityTest<T extends TicketingActivity> extends ScanningTest {
    @Rule
    public final TicketingTestRule<T> mActivityRule;

    protected ActivityTest(int eventId, Class<T> testClass) {
        super(eventId);
        this.mActivityRule = new TicketingTestRule<>(testClass, eventId, MockEventsRepository.CONFIG_BY_EVENT.get(eventId));
    }

    @Before
    public void setUp() {
        Intents.init();
        Intents.intending(isInternal()).respondWith(new Instrumentation.ActivityResult(Activity.RESULT_OK, null));
    }

    @After
    public void cleanup() {
        Intents.release();
        mActivityRule.finishActivity();
    }

    @Override
    public TicketingService getTicketingService() {
        return getOrCreateTicketingService(mActivityRule.getActivity());
    }
}
