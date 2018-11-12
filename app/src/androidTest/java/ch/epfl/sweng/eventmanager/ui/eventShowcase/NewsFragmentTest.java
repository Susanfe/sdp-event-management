package ch.epfl.sweng.eventmanager.ui.eventShowcase;

import android.content.Intent;
import android.os.SystemClock;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.espresso.contrib.NavigationViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.view.Gravity;
import ch.epfl.sweng.eventmanager.R;
import ch.epfl.sweng.eventmanager.repository.data.User;
import ch.epfl.sweng.eventmanager.ui.eventSelector.EventPickingActivity;
import ch.epfl.sweng.eventmanager.userManagement.Session;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;

import static android.support.test.espresso.Espresso.onIdle;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.DrawerMatchers.isClosed;
import static android.support.test.espresso.matcher.ViewMatchers.*;

public class NewsFragmentTest {
    @Rule
    public final ActivityTestRule<EventShowcaseActivity> mActivityRule =
            new ActivityTestRule<>(EventShowcaseActivity.class);

    @Before
    public void setup() {
        Session.login(new User(1, "admin", "admin", new HashSet<>(Arrays.asList(User.Permission.values()))), "secret");

        Intent intent = new Intent();
        intent.putExtra(EventPickingActivity.SELECTED_EVENT_ID, 1);
        mActivityRule.launchActivity(intent);

        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT)))
                .perform(DrawerActions.open());

        // Switch displayed fragment
        onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.nav_news));
    }

    @Test
    public void testCreateNews() {
        onView(withId(R.id.news_create_button))
                .check(ViewAssertions.matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
                .perform(ViewActions.click());

        String newsTitle = "A sweet news";
        String newsContent = "This is the news content. Sweet, right?";

        onView(withId(R.id.title)).perform(typeText(newsTitle), closeSoftKeyboard());
        onView(withId(R.id.content)).perform(typeText(newsContent), closeSoftKeyboard());
        onView(withId(R.id.send)).perform(ViewActions.click());

        onView(withId(R.id.recyclerView)).check(matches(hasDescendant(Matchers.allOf(
                hasDescendant(withText(newsTitle)),
                hasDescendant(withText(newsTitle))
        ))));

    }
}