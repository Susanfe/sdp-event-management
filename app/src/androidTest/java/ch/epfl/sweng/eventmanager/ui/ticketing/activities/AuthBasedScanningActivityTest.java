package ch.epfl.sweng.eventmanager.ui.ticketing.activities;

import android.support.test.espresso.intent.Intents;
import android.support.test.espresso.intent.matcher.IntentMatchers;
import ch.epfl.sweng.eventmanager.R;
import ch.epfl.sweng.eventmanager.test.ticketing.MockStacks;
import ch.epfl.sweng.eventmanager.ui.ticketing.TicketingActivity;
import ch.epfl.sweng.eventmanager.ui.ticketing.TicketingLoginActivity;
import ch.epfl.sweng.eventmanager.ui.ticketing.TicketingScanActivity;
import ch.epfl.sweng.eventmanager.ui.ticketing.TicketingTestRule;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * @author Louis Vialar
 */
public class AuthBasedScanningActivityTest extends BaseScanningActivityTest {

    public AuthBasedScanningActivityTest() {
        super(3);
    }

    @Override
    protected TicketingTestRule<TicketingScanActivity> prepareRule(Class<TicketingScanActivity> testClass) {
        return super.prepareRule(testClass).withConfigId(3);
    }

    @Test
    @Ignore("Generates a fucking permission box because I can't get the bloody testrules to work, so we won't test that")
    public void testLoggedOut() {
        Intents.assertNoUnverifiedIntents();

        waitCameraReady();

        onView(withId(R.id.barcode_scanner)).check(matches(isDisplayed()));
        Assert.assertFalse(getTicketingService().isLoggedIn());

        Intents.release();
        Intents.init();

        sendScanSuccess(MockStacks.SINGLE_BARCODE);

        Intents.intended(Matchers.allOf(
                IntentMatchers.hasComponent(TicketingLoginActivity.class.getName()),
                IntentMatchers.hasExtra(TicketingActivity.SELECTED_EVENT_ID, eventId),
                IntentMatchers.hasExtra(TicketingActivity.TICKETING_CONFIGURATION, getConfiguration())
        ));

        Intents.assertNoUnverifiedIntents();
    }
}
