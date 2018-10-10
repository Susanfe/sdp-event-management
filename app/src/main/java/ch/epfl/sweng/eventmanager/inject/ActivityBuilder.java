package ch.epfl.sweng.eventmanager.inject;

import ch.epfl.sweng.eventmanager.ui.eventSelector.EventPickingModule;
import ch.epfl.sweng.eventmanager.ui.eventShowcase.EventShowcaseModule;
import ch.epfl.sweng.eventmanager.ui.schedule.ScheduleModule;
import dagger.Module;
import dagger.android.AndroidInjectionModule;

/**
 * This is a class that builds activity modules for dependency injection purposes.<br>
 * When creating a new activity, its module should be registered here.
 * @author Louis Vialar
 */
@Module(includes = {AndroidInjectionModule.class, EventPickingModule.class, EventShowcaseModule.class, ScheduleModule.class})
public abstract class ActivityBuilder {

    /*
    If your Subcomponent does nothing, you can omit it and just use this (don't add the module in the top annotation either):


    @ActivityScope
    @ContributesAndroidInjector(modules = { /* modules to install into the subcomponent  })
    abstract YourActivity contributeYourActivityInjector();
     */

}
