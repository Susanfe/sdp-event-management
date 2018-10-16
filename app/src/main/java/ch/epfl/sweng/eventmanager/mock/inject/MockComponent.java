package ch.epfl.sweng.eventmanager.mock.inject;

import ch.epfl.sweng.eventmanager.mock.ui.schedule.MockScheduleModule;
import ch.epfl.sweng.eventmanager.ui.schedule.ScheduleActivity;
import dagger.Component;

import javax.inject.Singleton;

@Singleton
@Component(modules = MockScheduleModule.class)
public interface MockComponent {
void inject(ScheduleActivity scheduleActivity);
}
