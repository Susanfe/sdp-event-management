package ch.epfl.sweng.eventmanager.ui.eventShowcase;

import android.content.Intent;
import android.os.SystemClock;
import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.espresso.contrib.NavigationViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.Gravity;
import ch.epfl.sweng.eventmanager.R;
import ch.epfl.sweng.eventmanager.ui.event.interaction.EventShowcaseActivity;
import ch.epfl.sweng.eventmanager.ui.event.selection.EventPickingActivity;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.DrawerMatchers.isClosed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.Espresso.onIdle;

@RunWith(AndroidJUnit4.class)
public class NewsFragmentTest {
    @Rule
    public final ActivityTestRule<EventShowcaseActivity> mActivityRule =
            new ActivityTestRule<>(EventShowcaseActivity.class);

    @Test
    public void testNavigation() throws InterruptedException {
        Intent intent = new Intent();
        intent.putExtra(EventPickingActivity.SELECTED_EVENT_ID, 1);
        mActivityRule.launchActivity(intent);

        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT)))
                .perform(DrawerActions.open());

        // Switch displayed fragment
        onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.nav_news));

        onIdle();

        // Run twitter code and all
        SystemClock.sleep(1000);
    }
}