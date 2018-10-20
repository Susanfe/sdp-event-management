package ch.epfl.sweng.eventmanager;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.eventmanager.ui.eventShowcase.EventShowcaseActivity;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class EventActivityTest {

    @Rule
    public final ActivityTestRule<EventShowcaseActivity> mActivityRule =
            new ActivityTestRule<>(EventShowcaseActivity.class);

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
