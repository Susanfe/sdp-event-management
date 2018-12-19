package ch.epfl.sweng.eventmanager.ui.event.interaction.fragments;

import android.os.SystemClock;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.contrib.DrawerActions;
import androidx.test.espresso.contrib.NavigationViewActions;
import ch.epfl.sweng.eventmanager.R;
import ch.epfl.sweng.eventmanager.test.EventTestRule;
import ch.epfl.sweng.eventmanager.ui.event.interaction.EventShowcaseActivity;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.os.SystemClock.sleep;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.core.AllOf.allOf;

public class LoginFacebookNewsFragmentTest {
    @Rule
    public final EventTestRule<EventShowcaseActivity> mActivityRule =
            new EventTestRule<>(EventShowcaseActivity.class, 2);
    @Before
    public void setup() {
        sleep(500);
        onView(withId(R.id.drawer_layout))
                .perform(DrawerActions.open());
        sleep(500);
        // Display Schedule Events
        onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.nav_news));
    }
    @Test
    public void loginFacebookTest() {
        SystemClock.sleep(500);
        onView(withId(R.id.menu_showcase_activity_news_facebook_login)).perform(click());
        SystemClock.sleep(500);
        onView(allOf(isDisplayed(), withId(R.id.loginButton))).check(matches(isDisplayed()));
        SystemClock.sleep(500);
        onView(withId(R.id.loginButton)).perform(click());// cannot check anything because after it is part of facebook

    }

    @Test
    public void pressedBckOnFacebookActivityWork() {
        SystemClock.sleep(500);
        onView(withId(R.id.menu_facebook_login_edit)).perform(click());
        Espresso.pressBack();
        onView(allOf(isDisplayed(), withId(R.id.menu_facebook_login_edit))).check(matches(isDisplayed()));
    }

}
