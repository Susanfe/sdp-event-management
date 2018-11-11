package ch.epfl.sweng.eventmanager.inject;

import ch.epfl.sweng.eventmanager.ui.event.selection.EventPickingModule;
import ch.epfl.sweng.eventmanager.ui.event.interaction.EventInteractionModule;
import ch.epfl.sweng.eventmanager.ui.user.UserManagerModule;
import dagger.Module;
import dagger.android.AndroidInjectionModule;

/**
 * This is a class that builds activity modules for dependency injection purposes.<br>
 * When creating a new activity, its module should be registered here.
 * @author Louis Vialar
 */
@Module(includes = {
        AndroidInjectionModule.class,
        EventPickingModule.class,
        EventInteractionModule.class,
        UserManagerModule.class
})
public abstract class ActivityBuilder {

    /*
    If your Subcomponent does nothing, you can omit it and just use this (don't add the module in the top annotation either):


    @ActivityScope
    @ContributesAndroidInjector(modules = { /* modules to install into the subcomponent  })
    abstract YourActivity contributeYourActivityInjector();
     */

}
