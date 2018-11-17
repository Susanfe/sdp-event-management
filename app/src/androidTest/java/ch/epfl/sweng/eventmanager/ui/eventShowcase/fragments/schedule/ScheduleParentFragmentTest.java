package ch.epfl.sweng.eventmanager.ui.eventShowcase.fragments.schedule;

import android.os.SystemClock;
import androidx.test.espresso.contrib.DrawerActions;
import androidx.test.espresso.contrib.NavigationViewActions;
import androidx.test.runner.AndroidJUnit4;
import android.view.Gravity;
import ch.epfl.sweng.eventmanager.R;
import ch.epfl.sweng.eventmanager.test.EventTestRule;
import ch.epfl.sweng.eventmanager.ui.eventShowcase.EventShowcaseActivity;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.DrawerMatchers.isClosed;
import static androidx.test.espresso.matcher.ViewMatchers.*;

@RunWith(AndroidJUnit4.class)
public class ScheduleParentFragmentTest {

    @Rule
    public final EventTestRule<EventShowcaseActivity> mActivityRule =
            new EventTestRule<>(EventShowcaseActivity.class);

    @Test
    public void navigateInViewPager() {
        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT)))
                .perform(DrawerActions.open());

        // Display Schedule Events
        onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.nav_schedule));

        SystemClock.sleep(800);

        onView(withId(R.id.viewpager)).check(matches(isCompletelyDisplayed()));
        SystemClock.sleep(500);
        onView(withId(R.id.viewpager)).perform(swipeLeft());
        SystemClock.sleep(500);
        onView(withId(R.id.viewpager)).perform(swipeLeft());
        SystemClock.sleep(500);
        onView(withId(R.id.viewpager)).perform(swipeRight()).check(matches(isCompletelyDisplayed()));

        SystemClock.sleep(800);

        onView(withText("My Schedule")).perform(click()).check(matches(isCompletelyDisplayed()));

        mActivityRule.finishActivity();
    }
}

