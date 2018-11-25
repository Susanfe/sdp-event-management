package ch.epfl.sweng.eventmanager.ui.ticketing.activities;

import android.os.SystemClock;
import androidx.test.espresso.intent.Intents;
import ch.epfl.sweng.eventmanager.R;
import ch.epfl.sweng.eventmanager.test.ticketing.MockStacks;
import org.hamcrest.Matchers;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

public class ScanningActivityTest extends BaseScanningActivityTest {
    public ScanningActivityTest() {
        super(2);
    }

    @Test
    public void testScanningActivity() {
        sendScanSuccess(MockStacks.SINGLE_BARCODE);
        SystemClock.sleep(1000);

        onView(withId(R.id.barcodePreview)).check(matches(withText(Matchers.allOf(Matchers.containsString(mActivityRule.getActivity().getString(R.string.ticketing_scan_success)), Matchers.containsString(mActivityRule.getActivity().getString(R.string.ticketing_scan_user)), Matchers.containsString(mActivityRule.getActivity().getString(R.string.ticketing_scan_bought_product)), Matchers.containsString(MockStacks.CLIENT.getFirstname() + " " + MockStacks.CLIENT.getLastname()), Matchers.containsString(MockStacks.PRODUCT.getName())))));


        sendScanSuccess("THIS CODE DOESNT EXIST");
        SystemClock.sleep(1000);

        onView(withId(R.id.barcodePreview)).check(matches(withText(Matchers.containsString(mActivityRule.getActivity().getString(R.string.ticketing_scan_failure)))));

        sendScanSuccess(MockStacks.MULTIPLE_BARCODE);
        SystemClock.sleep(1000);

        onView(withId(R.id.barcodePreview)).check(matches(withText(Matchers.allOf(Matchers.containsString(mActivityRule.getActivity().getString(R.string.ticketing_scan_success)), Matchers.containsString(mActivityRule.getActivity().getString(R.string.ticketing_scan_user)), Matchers.containsString(mActivityRule.getActivity().getString(R.string.ticketing_scan_bought_product)), Matchers.containsString(MockStacks.CLIENT.getFirstname() + " " + MockStacks.CLIENT.getLastname()), Matchers.containsString(MockStacks.AMOUNT + " * " + MockStacks.PRODUCT.getName())))));

        Intents.assertNoUnverifiedIntents();
    }
}