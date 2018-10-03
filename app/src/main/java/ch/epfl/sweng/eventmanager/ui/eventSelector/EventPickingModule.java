package ch.epfl.sweng.eventmanager.ui.eventSelector;

import android.app.Activity;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import ch.epfl.sweng.eventmanager.viewmodel.ViewModelFactory;
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
public abstract class EventPickingModule {
    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelFactory factory);

    @Binds
    @IntoMap
    @ViewModelKey(EventPickingModel.class)
    abstract ViewModel provideEventListModel(EventPickingModel eventPickingModel);

    @Binds
    @IntoMap
    @ActivityKey(EventPickingActivity.class)
    abstract AndroidInjector.Factory<? extends Activity>
    bindEventPickingActivityInjectorFactory(EventPickingActivitySubcomponent.Builder builder);
}