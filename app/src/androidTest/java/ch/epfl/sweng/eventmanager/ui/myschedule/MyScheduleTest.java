package ch.epfl.sweng.eventmanager.ui.myschedule;

import android.content.Intent;
import android.os.SystemClock;
import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.espresso.contrib.NavigationViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.Gravity;
import android.view.View;
import ch.epfl.sweng.eventmanager.R;
import ch.epfl.sweng.eventmanager.ui.eventSelector.EventPickingActivity;
import ch.epfl.sweng.eventmanager.ui.eventShowcase.EventShowcaseActivity;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.*;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.DrawerMatchers.isClosed;
import static android.support.test.espresso.matcher.ViewMatchers.*;
import static org.hamcrest.core.AllOf.allOf;

@RunWith(AndroidJUnit4.class)
public class MyScheduleTest {
    @Rule
    public final ActivityTestRule<EventShowcaseActivity> mActivityRule =
            new ActivityTestRule<>(EventShowcaseActivity.class);

    public static Matcher<View> withIndex(final Matcher<View> matcher, final int index) {
        return new TypeSafeMatcher<View>() {
            int currentIndex = 0;

            @Override
            public void describeTo(Description description) {
                description.appendText("with index: ");
                description.appendValue(index);
                matcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                return matcher.matches(view) && currentIndex++ == index;
            }
        };
    }

    @Test
    public void addScheduleItemAndDeleteItTest() {
        Intent intent = new Intent();
        // Opens Sysmic Event
        intent.putExtra(EventPickingActivity.SELECTED_EVENT_ID, 2);
        mActivityRule.launchActivity(intent);

        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT)))
                .perform(DrawerActions.open());

        // Display Schedule Events
        onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.nav_schedule));

        SystemClock.sleep(800);

        onView(withId(R.id.viewpager)).perform(swipeLeft()).check(matches(isCompletelyDisplayed()));
        SystemClock.sleep(800);

        // Add first element of scheduledItems to MySchedule
        onView(allOf(isDisplayed(),withIndex(withId(R.id.text_timeline_description),0))).perform(longClick());

        onView(withId(R.id.viewpager)).perform(swipeRight()).check(matches(isCompletelyDisplayed()));

        SystemClock.sleep(800);

        onView(allOf(isDisplayed(), withId(R.id.text_timeline_description))).perform(longClick());

        SystemClock.sleep(800);
    }

        onView(withText("My Schedule")).perform(click());
        SystemClock.sleep(800);


        onView(withId(R.id.addToCalendar)).check(matches(isCompletelyDisplayed())).perform(click());

        // SystemClock.sleep(500);
        // intended(hasAction(Intent.ACTION_VIEW));


    }
}
