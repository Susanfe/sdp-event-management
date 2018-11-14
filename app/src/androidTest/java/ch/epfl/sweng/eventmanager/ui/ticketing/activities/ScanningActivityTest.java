package ch.epfl.sweng.eventmanager.ui.ticketing.activities;

import android.os.SystemClock;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.intent.Intents;
import android.support.test.espresso.intent.matcher.IntentMatchers;
import ch.epfl.sweng.eventmanager.R;
import ch.epfl.sweng.eventmanager.test.ticketing.MockStacks;
import ch.epfl.sweng.eventmanager.ui.eventSelector.EventPickingActivity;
import ch.epfl.sweng.eventmanager.ui.eventShowcase.EventShowcaseActivity;
import org.hamcrest.Matchers;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

public class ScanningActivityTest extends BaseScanningActivityTest {
    public ScanningActivityTest() {
        super(2);
    }

    @Test
    public void testScanningActivity() {
        waitCameraReady();

        sendScanSuccess(MockStacks.SINGLE_BARCODE);
        //onView(withText(R.string.ticketing_scan_success)).inRoot(new ToastMatcher()).check(matches(isDisplayed()));
        onView(withId(R.id.barcodePreview)).check(matches(withText(
                Matchers.allOf(
                        Matchers.containsString(mActivityRule.getActivity().getString(R.string.ticketing_scan_success)),
                        Matchers.containsString(mActivityRule.getActivity().getString(R.string.ticketing_scan_user)),
                        Matchers.containsString(mActivityRule.getActivity().getString(R.string.ticketing_scan_bought_product)),
                        Matchers.containsString(MockStacks.CLIENT.getFirstname() + " " + MockStacks.CLIENT.getLastname()),
                        Matchers.containsString(MockStacks.PRODUCT.getName())
                ))));


        SystemClock.sleep(5000);

        sendScanSuccess("THIS CODE DOESNT EXIST");
        //onView(withText(R.string.ticketing_scan_failure)).inRoot(new ToastMatcher()).check(matches(isDisplayed()));
        onView(withId(R.id.barcodePreview)).check(matches(withText(
                Matchers.containsString(mActivityRule.getActivity().getString(R.string.ticketing_scan_failure))
        )));

        SystemClock.sleep(5000);

        sendScanSuccess(MockStacks.MULTIPLE_BARCODE);
        //onView(withText(R.string.ticketing_scan_success)).inRoot(new ToastMatcher()).check(matches(isDisplayed()));
        onView(withId(R.id.barcodePreview)).check(matches(withText(
                Matchers.allOf(
                        Matchers.containsString(mActivityRule.getActivity().getString(R.string.ticketing_scan_success)),
                        Matchers.containsString(mActivityRule.getActivity().getString(R.string.ticketing_scan_user)),
                        Matchers.containsString(mActivityRule.getActivity().getString(R.string.ticketing_scan_bought_product)),
                        Matchers.containsString(MockStacks.CLIENT.getFirstname() + " " + MockStacks.CLIENT.getLastname()),
                        Matchers.containsString(MockStacks.AMOUNT + " * " + MockStacks.PRODUCT.getName())
                ))));

        SystemClock.sleep(500);

        Intents.assertNoUnverifiedIntents();
    }

    @Test
    public void testBackBehaviour() {
        waitCameraReady();

        onView(withText(R.string.back_button)).perform(ViewActions.click());


        Intents.intended(Matchers.allOf(
                IntentMatchers.hasComponent(EventShowcaseActivity.class.getName()),
                IntentMatchers.hasExtra(EventPickingActivity.SELECTED_EVENT_ID, eventId)
        ));

        Intents.assertNoUnverifiedIntents();
    }
}