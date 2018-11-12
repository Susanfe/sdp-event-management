package ch.epfl.sweng.eventmanager.mock.ui.schedule;

import android.arch.lifecycle.ViewModel;
import ch.epfl.sweng.eventmanager.mock.repository.MockNewsRepository;
import ch.epfl.sweng.eventmanager.repository.FirebaseNewsRepository;
import ch.epfl.sweng.eventmanager.repository.NewsRepository;
import ch.epfl.sweng.eventmanager.ui.eventShowcase.EventShowcaseModule;
import ch.epfl.sweng.eventmanager.viewmodel.ViewModelKey;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoMap;

@Module
public abstract class MockScheduleModule extends EventShowcaseModule {

    @Binds
    @IntoMap
    @ViewModelKey(MockScheduleViewModel.class)
    abstract ViewModel provideScheduleViewModel(MockScheduleViewModel mockScheduleViewModel);
}
