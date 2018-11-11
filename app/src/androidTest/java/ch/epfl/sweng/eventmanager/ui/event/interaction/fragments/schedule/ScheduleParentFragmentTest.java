package ch.epfl.sweng.eventmanager.ui.event.interaction.fragments.schedule;

import android.os.SystemClock;
import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.espresso.contrib.NavigationViewActions;
import android.support.test.runner.AndroidJUnit4;
import android.view.Gravity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.eventmanager.R;
import ch.epfl.sweng.eventmanager.test.EventTestRule;
import ch.epfl.sweng.eventmanager.ui.event.interaction.EventShowcaseActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.action.ViewActions.swipeRight;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.DrawerMatchers.isClosed;
import static android.support.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

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

