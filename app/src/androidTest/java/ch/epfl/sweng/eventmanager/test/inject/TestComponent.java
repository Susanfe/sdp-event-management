package ch.epfl.sweng.eventmanager.test.inject;

import ch.epfl.sweng.eventmanager.inject.ActivityBuilder;
import ch.epfl.sweng.eventmanager.inject.ApplicationComponent;
import ch.epfl.sweng.eventmanager.inject.ApplicationModule;
import ch.epfl.sweng.eventmanager.test.repository.MockRepositoriesModule;
import ch.epfl.sweng.eventmanager.repository.room.RoomModule;
import ch.epfl.sweng.eventmanager.test.users.MockUsersModule;
import ch.epfl.sweng.eventmanager.ui.eventShowcase.NewsFragmentTest;
import dagger.Component;
import dagger.android.AndroidInjectionModule;
import dagger.android.support.AndroidSupportInjectionModule;

import javax.inject.Singleton;

@Component(modules = {
        AndroidInjectionModule.class,
        AndroidSupportInjectionModule.class,
        ActivityBuilder.class,
        ApplicationModule.class,
        RoomModule.class,
        MockRepositoriesModule.class,
        MockUsersModule.class})
@Singleton
public interface TestComponent extends ApplicationComponent {
    void inject(NewsFragmentTest test);

    @Component.Builder
    interface Builder extends ApplicationComponent.Builder {
        @Override
        TestComponent build();
    }
}