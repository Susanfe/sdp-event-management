package ch.epfl.sweng.eventmanager.ui;

import android.content.Intent;
import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.espresso.contrib.NavigationViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.eventmanager.R;
import ch.epfl.sweng.eventmanager.ui.eventSelector.EventPickingActivity;
import ch.epfl.sweng.eventmanager.ui.eventShowcase.EventShowcaseActivity;

import static android.os.SystemClock.sleep;
import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.fail;

@RunWith(AndroidJUnit4.class)
public class EventMapFragmentTest {

    private final static int DIFFERENCE_TO_SNIPPET = 80;

    @Rule
    public final ActivityTestRule<EventShowcaseActivity> mActivityRule =
            new ActivityTestRule<>(EventShowcaseActivity.class);

    @Test
    public void eventMapTest() {
        Intent intent = new Intent();
        // Opens Sysmic Event
        intent.putExtra(EventPickingActivity.SELECTED_EVENT_ID, 2);
        mActivityRule.launchActivity(intent);

        onView(withId(R.id.drawer_layout))
                .perform(DrawerActions.open());

        // Display Map Events
        onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.nav_map));
        onView(withId(R.id.text_test)).check(matches(withText("everything is ready")));

        // Clicks on the EPFL marker
        UiDevice device = UiDevice.getInstance(getInstrumentation());
        UiObject marker = device.findObject(new UiSelector().descriptionContains("EPFL"));
        try {
            marker.click();
            int x = marker.getBounds().centerX();
            int y = marker.getBounds().centerY();
            device.click(x, y-DIFFERENCE_TO_SNIPPET);
        } catch (UiObjectNotFoundException e) {
            e.printStackTrace();
        }
    }
}
