package ch.epfl.sweng.eventmanager.ui.schedule;

import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;

import ch.epfl.sweng.eventmanager.EventManagerApplication;
import ch.epfl.sweng.eventmanager.inject.DaggerApplicationComponent;
import ch.epfl.sweng.eventmanager.mock.inject.MockComponent;
import ch.epfl.sweng.eventmanager.repository.data.Event;
import org.junit.Before;
import org.junit.Rule;

public class ScheduleActivityTest {

    @Rule
    public final ActivityTestRule<ScheduleActivity> mActivityRule =
            new ActivityTestRule<>(ScheduleActivity.class);


    @Before
    public void init() {
        //getting the application class
        EventManagerApplication myApp = (EventManagerApplication) InstrumentationRegistry
                .getInstrumentation()
                .getTargetContext()
                .getApplicationContext();

    }
}