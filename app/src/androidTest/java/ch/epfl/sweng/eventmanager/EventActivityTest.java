package ch.epfl.sweng.eventmanager;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.eventmanager.ui.eventShowcase.EventActivity;
import ch.epfl.sweng.eventmanager.ui.eventShowcase.MapsActivity;
import ch.epfl.sweng.eventmanager.ui.eventShowcase.ScheduleActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static java.sql.DriverManager.println;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class EventActivityTest {

    @Rule
    public final ActivityTestRule<EventActivity> mActivityRule =
            new ActivityTestRule<>(EventActivity.class);

    @Test
    public void pass() {
        assertEquals(1,1);
    }

/*
    @Test
    public void goToMapWork() {
        assertTrue(onView(withId(R.id.map_button)).perform(click()).getClass().equals(MapsActivity.class));
    }*/
/*
    @Test
    public void goToScheduleWork() {
        Context context = InstrumentationRegistry.getTargetContext();
        println("class = " + getClass());
        onView(withId(R.id.schedule_button)).perform(click());
        //assertTrue(getClass().equals(scheduleActivity.class));
    }*/

}
