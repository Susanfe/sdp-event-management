package ch.epfl.sweng.eventmanager.mock.ui.schedule;

import android.arch.lifecycle.ViewModel;

import ch.epfl.sweng.eventmanager.ui.event.interaction.EventInteractionModule;
import ch.epfl.sweng.eventmanager.viewmodel.ViewModelKey;
import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class MockScheduleModule extends EventInteractionModule {

    @Binds
    @IntoMap
    @ViewModelKey(MockScheduleViewModel.class)
    abstract ViewModel provideScheduleViewModel(MockScheduleViewModel mockScheduleViewModel);
}
