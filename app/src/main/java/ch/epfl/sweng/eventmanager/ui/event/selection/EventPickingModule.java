package ch.epfl.sweng.eventmanager.ui.event.selection;

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
public abstract class EventPickingModule {
    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelFactory factory);

    @Binds
    @IntoMap
    @ViewModelKey(EventPickingModel.class)
    abstract ViewModel provideEventListModel(EventPickingModel eventPickingModel);

    @ContributesAndroidInjector
    abstract EventPickingActivity contributeEventPickingActivityInjector();
}