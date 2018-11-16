package ch.epfl.sweng.eventmanager.ui.ticketing.activities;

import android.support.test.espresso.intent.Intents;
import android.support.test.espresso.intent.matcher.IntentMatchers;
import ch.epfl.sweng.eventmanager.R;
import ch.epfl.sweng.eventmanager.ToastMatcher;
import ch.epfl.sweng.eventmanager.test.ticketing.MockStacks;
import ch.epfl.sweng.eventmanager.ui.ticketing.TicketingActivity;
import ch.epfl.sweng.eventmanager.ui.ticketing.TicketingLoginActivity;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.*;

/**
 * @author Louis Vialar
 */
public class AuthBasedScanningActivityTest extends BaseScanningActivityTest {
    public AuthBasedScanningActivityTest() {
        super(3);
    }

    @Test @Ignore
    public void testLoggedOut() {
        waitCameraReady();

        onView(withId(R.id.barcode_scanner)).check(matches(isDisplayed()));
        Assert.assertFalse(getTicketingService().isLoggedIn());

        sendScanSuccess(MockStacks.SINGLE_BARCODE);

        onView(withText(R.string.ticketing_requires_login)).inRoot(new ToastMatcher()).check(matches(isDisplayed()));

        Intents.intended(Matchers.allOf(
                IntentMatchers.hasComponent(TicketingLoginActivity.class.getName()),
                IntentMatchers.hasExtra(TicketingActivity.SELECTED_EVENT_ID, eventId),
                IntentMatchers.hasExtra(TicketingActivity.TICKETING_CONFIGURATION, getConfiguration())
        ));

        Intents.assertNoUnverifiedIntents();
    }
}
