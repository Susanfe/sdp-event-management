package ch.epfl.sweng.eventmanager.ui.eventShowcase;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import ch.epfl.sweng.eventmanager.viewmodel.ViewModelFactory;
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
    abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelFactory factory);

    @Binds
    @IntoMap
    @ViewModelKey(EventShowcaseModel.class)
    abstract ViewModel provideEventListModel(EventShowcaseModel eventListModel);

    @ContributesAndroidInjector
    abstract EventActivity contributeEventActivityInjector();

    @ContributesAndroidInjector
    abstract MapActivity contributeMapActivityInjector();

    @ContributesAndroidInjector
    abstract TicketActivity contributeTicketActivityInjector();

}

