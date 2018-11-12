package ch.epfl.sweng.eventmanager.mock.inject;

import ch.epfl.sweng.eventmanager.EventManagerApplication;
import ch.epfl.sweng.eventmanager.inject.ApplicationComponent;
import ch.epfl.sweng.eventmanager.mock.repository.MockRepositoriesModule;
import ch.epfl.sweng.eventmanager.mock.ui.schedule.MockScheduleModule;
import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjectionModule;

import javax.inject.Singleton;

/**
 * @deprecated doesn't work
 */
@Component(modules = {
        AndroidInjectionModule.class,
        MockScheduleModule.class,
        MockRepositoriesModule.class})
@Singleton
@Deprecated
public interface MockApplicationComponent extends ApplicationComponent {
    @Override
    void inject(EventManagerApplication eventManagerApplication);

    @Component.Builder
    interface Builder {
        @BindsInstance
        public Builder setEventManagerApplication(EventManagerApplication eventManagerApplication);

        public MockApplicationComponent build();
    }
}