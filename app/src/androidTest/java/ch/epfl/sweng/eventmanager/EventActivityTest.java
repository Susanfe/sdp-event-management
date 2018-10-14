package ch.epfl.sweng.eventmanager;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.eventmanager.ui.eventShowcase.EventActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.v4.media.session.MediaSessionCompatApi21.isActive;

@RunWith(AndroidJUnit4.class)
public class EventActivityTest {

    @Rule
    public final ActivityTestRule<EventActivity> mActivityRule =
            new ActivityTestRule<>(EventActivity.class);

    @Test
    public void goToMapWork() {
        onView(withId(R.id.map_button)).perform(click()).check();
    }

}
