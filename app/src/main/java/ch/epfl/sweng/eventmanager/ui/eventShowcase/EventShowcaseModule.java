package ch.epfl.sweng.eventmanager.ui.eventShowcase;

import android.arch.lifecycle.ViewModel;
import ch.epfl.sweng.eventmanager.ui.eventShowcase.fragments.SendNewsFragment;
import ch.epfl.sweng.eventmanager.ui.eventShowcase.models.EventShowcaseModel;
import ch.epfl.sweng.eventmanager.ui.eventShowcase.models.NewsViewModel;
import ch.epfl.sweng.eventmanager.ui.eventShowcase.models.ScheduleViewModel;
import ch.epfl.sweng.eventmanager.ui.eventShowcase.models.SpotsModel;
import ch.epfl.sweng.eventmanager.ui.eventShowcase.models.ZoneModel;
import ch.epfl.sweng.eventmanager.viewmodel.ViewModelKey;
import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import dagger.multibindings.IntoMap;

/**
 * @author Louis Vialar
 */
@Module
public abstract class EventShowcaseModule {
    @Binds
    @IntoMap
    @ViewModelKey(EventShowcaseModel.class)
    abstract ViewModel provideEventListModel(EventShowcaseModel eventListModel);

    @Binds
    @IntoMap
    @ViewModelKey(ScheduleViewModel.class)
    abstract ViewModel provideScheduleViewModel(ScheduleViewModel scheduleViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(NewsViewModel.class)
    abstract ViewModel provideNewsViewModel(NewsViewModel newsViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(SpotsModel.class)
    abstract ViewModel provideSpotsViewModel(SpotsModel spotsModel);

    @Binds
    @IntoMap
    @ViewModelKey(ZoneModel.class)
    abstract ViewModel provideZonesViewModel(ZoneModel zonesModel);

    @ContributesAndroidInjector
    abstract EventShowcaseActivity contributeEventShowcaseActivityInjector();

    @ContributesAndroidInjector
    abstract SendNewsFragment contributeSendNewsFragmentInjector();
}

