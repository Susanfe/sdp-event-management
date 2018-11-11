package ch.epfl.sweng.eventmanager.ui.event.interaction.fragments.schedule;

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
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.DrawerMatchers.isClosed;
import static android.support.test.espresso.matcher.ViewMatchers.Visibility;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class ScheduleParentFragmentEmptyScheduleTest {

    @Rule
    public final EventTestRule<EventShowcaseActivity> mActivityRule =
            new EventTestRule<>(EventShowcaseActivity.class, 2);


    @Test
    public void testEmptyViewPager() {
        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT)))
                .perform(DrawerActions.open());

        // Display Schedule Events
        onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.nav_schedule));

        //check that tabs for navigation are hided
        onView(withId(R.id.tabs)).check(matches(withEffectiveVisibility(Visibility.GONE)));
    }

}

