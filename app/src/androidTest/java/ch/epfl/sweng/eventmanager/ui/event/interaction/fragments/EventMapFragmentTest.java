package ch.epfl.sweng.eventmanager.ui.event.interaction.fragments;

import android.Manifest;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;

import androidx.test.espresso.contrib.DrawerActions;
import androidx.test.espresso.contrib.NavigationViewActions;
import androidx.test.rule.GrantPermissionRule;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;
import ch.epfl.sweng.eventmanager.R;
import ch.epfl.sweng.eventmanager.test.EventTestRule;
import ch.epfl.sweng.eventmanager.ui.event.interaction.EventShowcaseActivity;

import static android.os.SystemClock.sleep;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.hamcrest.core.AllOf.allOf;
import static org.junit.Assert.fail;

public class EventMapFragmentTest {
    @Rule public GrantPermissionRule permissionRule = GrantPermissionRule.grant(Manifest.permission.ACCESS_FINE_LOCATION);

    @Rule
    public final EventTestRule<EventShowcaseActivity> mActivityRule =
            new EventTestRule<>(EventShowcaseActivity.class, 1);

    @Before
    public void setup() {
        sleep(100);

        onView(withId(R.id.drawer_layout))
                .perform(DrawerActions.open());

        sleep(100);

        onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.nav_map));
    }

    @Test
    public void eventMapTest() {
        onView(withId(R.id.drawer_layout))
                .perform(DrawerActions.open());
        onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.nav_main));
        sleep(800);
        onView(withId(R.id.drawer_layout))
                .perform(DrawerActions.open());
        onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.nav_map));
        sleep(800);
    }

    /**
     * Test the right behavior of the markers i.e. when click on the snippet of a marker that have
     * a corresponding schedule, goes to the schedule.
     *
     * We ignore this test, because Travis doesn't support (can't access to the markers, because map not displayed)
     */
    @Test
    @Ignore
    public void clickAndGoToSchedule(){
        UiDevice device = UiDevice.getInstance(getInstrumentation());
        try {
            UiObject sceneMarker = device.findObject(new UiSelector().descriptionContains("scene"));
            sceneMarker.click();

            sleep(800);
            int x = sceneMarker.getBounds().centerX();
            int y = sceneMarker.getBounds().centerY();

            //click on the snippet of the marker
            //does nothing here
            device.click(x,y - 200);
            sleep(800);

            onView(allOf(withId(R.id.mapview))).check(matches(isDisplayed()));

            UiObject polyvMarker = device.findObject(new UiSelector().descriptionContains("Polyv"));
            polyvMarker.click();

            sleep(800);
            int xPolyv = polyvMarker.getBounds().centerX();
            int yPolyv = polyvMarker.getBounds().centerY();

            //click on the snippet of the marker
            //go to the corresponding schedule
            device.click(xPolyv,yPolyv - 200);
            sleep(800);

            onView(allOf(isDisplayed(), withId(R.id.viewpager)));

        } catch (UiObjectNotFoundException e) {
            e.printStackTrace();
            fail("Fail on finding the marker");
        }
    }

    @After
    public void finish() {
        mActivityRule.finishActivity();
    }
}
