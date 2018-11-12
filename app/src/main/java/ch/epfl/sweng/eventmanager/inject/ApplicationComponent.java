package ch.epfl.sweng.eventmanager.inject;

import android.app.Application;
import ch.epfl.sweng.eventmanager.EventManagerApplication;
import ch.epfl.sweng.eventmanager.repository.RepositoriesModule;
import ch.epfl.sweng.eventmanager.repository.room.RoomModule;
import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjectionModule;
import dagger.android.support.AndroidSupportInjectionModule;

import javax.inject.Singleton;

/**
 * This is the main Dagger2 component of the application. It provides a way for the Application to be used by the
 * dependency injection environment. It probably should not be touched anymore.
 */
@Component(modules = {
        AndroidInjectionModule.class,
        AndroidSupportInjectionModule.class,
        ActivityBuilder.class,
        ApplicationModule.class,
        RepositoriesModule.class,
        RoomModule.class})
@Singleton
public interface ApplicationComponent {
    void inject(EventManagerApplication application);

    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(Application application);

        Builder room(RoomModule roomModule);

        Builder repositories(RepositoriesModule module);

        ApplicationComponent build();
    }
}