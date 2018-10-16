package ch.epfl.sweng.eventmanager.mock.inject;

import android.app.Application;
import ch.epfl.sweng.eventmanager.EventManagerApplication;
import ch.epfl.sweng.eventmanager.inject.ActivityBuilder;
import ch.epfl.sweng.eventmanager.inject.ApplicationComponent;
import ch.epfl.sweng.eventmanager.inject.ApplicationModule;
import ch.epfl.sweng.eventmanager.mock.ui.schedule.MockScheduleModule;
import ch.epfl.sweng.eventmanager.repository.room.RoomModule;
import ch.epfl.sweng.eventmanager.ui.schedule.ScheduleModule;
import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjectionModule;

import javax.inject.Singleton;

@Component(modules = {
        AndroidInjectionModule.class,
        MockScheduleModule.class})
@Singleton
public interface MockApplicationComponent extends ApplicationComponent {
    @Override
    void inject (EventManagerApplication eventManagerApplication);

    @Component.Builder
    interface Builder {
        @BindsInstance
        public Builder setEventManagerApplication (EventManagerApplication eventManagerApplication);
        public MockApplicationComponent build();
    }
}