package ch.epfl.sweng.eventmanager.ui.eventShowcase;

import android.app.Activity;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import ch.epfl.sweng.eventmanager.viewmodel.ViewModelFactory;
import ch.epfl.sweng.eventmanager.viewmodel.ViewModelKey;
import dagger.Binds;
import dagger.Module;
import dagger.android.ActivityKey;
import dagger.android.AndroidInjector;
import dagger.android.ContributesAndroidInjector;
import dagger.multibindings.IntoMap;

/**
 * @author Louis Vialar
 */
@Module(subcomponents = {EventActivitySubcomponent.class})
public abstract class EventShowcaseModule {
    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelFactory factory);

    @Binds
    @IntoMap
    @ViewModelKey(EventShowcaseModel.class)
    abstract ViewModel provideEventListModel(EventShowcaseModel eventListModel);


    @Binds
    @IntoMap
    @ActivityKey(EventActivity.class)
    abstract AndroidInjector.Factory<? extends Activity>
    bindEventActivityInjectorFactory(EventActivitySubcomponent.Builder builder);

    @ContributesAndroidInjector
    abstract MapActivity contributeMapActivityInjector();

    @ContributesAndroidInjector
    abstract ScheduleActivity contributeScheduleActivityInjector();

    @ContributesAndroidInjector
    abstract TicketActivity contributeTicketActivityInjector();

}

