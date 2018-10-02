package ch.epfl.sweng.eventmanager.ui.eventSelector;

import android.app.Activity;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import ch.epfl.sweng.eventmanager.viewmodel.DaggerViewModelFactory;
import ch.epfl.sweng.eventmanager.viewmodel.ViewModelKey;
import dagger.Binds;
import dagger.Module;
import dagger.android.ActivityKey;
import dagger.android.AndroidInjector;
import dagger.multibindings.IntoMap;

/**
 * @author Louis Vialar
 */
@Module(subcomponents = {EventPickingActivitySubcomponent.class})
public abstract class EventPickingActivityModule {
    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(DaggerViewModelFactory factory);

    @Binds
    @IntoMap
    @ViewModelKey(EventListModel.class)
    abstract ViewModel provideEventListModel(EventListModel eventListModel);

    @Binds
    @IntoMap
    @ActivityKey(EventPickingActivity.class)
    abstract AndroidInjector.Factory<? extends Activity>
    bindEventPickingActivityInjectorFactory(EventPickingActivitySubcomponent.Builder builder);
}