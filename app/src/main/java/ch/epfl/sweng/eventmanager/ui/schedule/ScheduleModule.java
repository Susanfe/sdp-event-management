package ch.epfl.sweng.eventmanager.ui.schedule;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * @author Louis Vialar
 */
@Module
public abstract class ScheduleModule {
    @ContributesAndroidInjector
    abstract ScheduleActivity contributeScheduleActivityInjector();
}
