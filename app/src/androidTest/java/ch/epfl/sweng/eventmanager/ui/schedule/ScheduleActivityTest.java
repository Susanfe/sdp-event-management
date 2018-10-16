package ch.epfl.sweng.eventmanager.ui.schedule;

import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;

import ch.epfl.sweng.eventmanager.EventManagerApplication;
import ch.epfl.sweng.eventmanager.inject.ApplicationComponent;
import ch.epfl.sweng.eventmanager.inject.DaggerApplicationComponent;
import ch.epfl.sweng.eventmanager.mock.inject.DaggerMockApplicationComponent;
import ch.epfl.sweng.eventmanager.mock.inject.MockApplicationComponent;
import ch.epfl.sweng.eventmanager.mock.repository.MockConcertRepository;
import ch.epfl.sweng.eventmanager.mock.ui.schedule.MockScheduleModule;
import ch.epfl.sweng.eventmanager.mock.ui.schedule.MockScheduleViewModel;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class ScheduleActivityTest {

    @Rule
    public final ActivityTestRule<ScheduleActivity> mActivityRule =
            new ActivityTestRule<>(ScheduleActivity.class);


    @Before
    public void init() {
        //getting the application class
        EventManagerApplication eventManagerApplication = (EventManagerApplication) InstrumentationRegistry
                .getInstrumentation()
                .getTargetContext()
                .getApplicationContext();

        MockApplicationComponent applicationComponent = DaggerMockApplicationComponent.builder().setEventManagerApplication(eventManagerApplication).build();
        applicationComponent.inject(eventManagerApplication);
        mActivityRule.launchActivity(new Intent());
    }

    @Test
    public void mainTest() {

    }
}