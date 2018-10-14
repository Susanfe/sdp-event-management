package ch.epfl.sweng.eventmanager.ui.schedule;

import android.arch.lifecycle.ViewModel;
import ch.epfl.sweng.eventmanager.viewmodel.ViewModelKey;
import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import dagger.multibindings.IntoMap;

/**
 * @author Louis Vialar
 */
@Module
public abstract class ScheduleModule {
    @ContributesAndroidInjector
    abstract ScheduleActivity contributeScheduleActivityInjector();

    @ContributesAndroidInjector
    abstract MyScheduleActivity contributeMyScheduleActivityInjector();

    @Binds
    @IntoMap
    @ViewModelKey(ScheduleViewModel.class)
    abstract ViewModel provideScheduleViewModel(ScheduleViewModel scheduleViewModel);
}
