package ch.epfl.sweng.eventmanager.ui.event.interaction;

import androidx.lifecycle.ViewModel;
import ch.epfl.sweng.eventmanager.ui.event.interaction.fragments.EventMapFragment;
import ch.epfl.sweng.eventmanager.ui.event.interaction.fragments.SendNewsFragment;
import ch.epfl.sweng.eventmanager.ui.event.interaction.models.EventInteractionModel;
import ch.epfl.sweng.eventmanager.ui.event.interaction.models.NewsViewModel;
import ch.epfl.sweng.eventmanager.ui.event.interaction.models.ScheduleViewModel;
import ch.epfl.sweng.eventmanager.ui.event.interaction.models.SpotsModel;
import ch.epfl.sweng.eventmanager.ui.event.interaction.models.ZoneModel;
import ch.epfl.sweng.eventmanager.viewmodel.ViewModelKey;
import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import dagger.multibindings.IntoMap;

/**
 * @author Louis Vialar
 */
@Module
public abstract class EventInteractionModule {
    @Binds
    @IntoMap
    @ViewModelKey(EventInteractionModel.class)
    abstract ViewModel provideEventListModel(EventInteractionModel eventListModel);

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

    @ContributesAndroidInjector
    abstract EventMapFragment contributeEventMapFragmentInjector();

    @ContributesAndroidInjector
    abstract EventAdministrationActivity contributeEventAdminsitrationActivityInjector();
}
