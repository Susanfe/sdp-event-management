package ch.epfl.sweng.eventmanager.test.inject;

import ch.epfl.sweng.eventmanager.inject.ActivityBuilder;
import ch.epfl.sweng.eventmanager.inject.ApplicationComponent;
import ch.epfl.sweng.eventmanager.inject.ApplicationModule;
import ch.epfl.sweng.eventmanager.repository.room.RoomModule;
import ch.epfl.sweng.eventmanager.test.repository.MockRepositoriesModule;
import ch.epfl.sweng.eventmanager.test.ticketing.MockTicketingModule;
import ch.epfl.sweng.eventmanager.test.users.MockUsersModule;
import ch.epfl.sweng.eventmanager.ui.event.interaction.EventCreateActivityTest;
import ch.epfl.sweng.eventmanager.ui.event.interaction.EventUpdateActivityTest;
import ch.epfl.sweng.eventmanager.ui.event.interaction.fragments.EventUserManagementFragmentTest;
import ch.epfl.sweng.eventmanager.ui.event.interaction.fragments.EventFeedbackFragmentTest;
import ch.epfl.sweng.eventmanager.ui.event.interaction.fragments.NewsFragmentTest;
import ch.epfl.sweng.eventmanager.ui.event.interaction.fragments.NotificationsFragmentTest;
import ch.epfl.sweng.eventmanager.ui.event.interaction.fragments.map.EventMapEditionFragmentTest;
import ch.epfl.sweng.eventmanager.ui.event.interaction.fragments.schedule.ScheduleCreateTest;
import ch.epfl.sweng.eventmanager.ui.event.interaction.fragments.schedule.ScheduleEditTest;
import ch.epfl.sweng.eventmanager.ui.event.selection.EventPickingActivityTest;
import ch.epfl.sweng.eventmanager.ui.ticketing.ScanningTest;
import ch.epfl.sweng.eventmanager.ui.ticketing.activities.NoOpBarcodeViewManagerModule;
import ch.epfl.sweng.eventmanager.ui.tools.ImageLoaderModule;
import ch.epfl.sweng.eventmanager.ui.user.LoginActivityTest;
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
        NoOpBarcodeViewManagerModule.class,
        ImageLoaderModule.class,
        MockUsersModule.class,
        MockTicketingModule.class})
@Singleton
public interface TestComponent extends ApplicationComponent {
    void inject(NewsFragmentTest test);
    void inject(ScheduleCreateTest test);
    void inject(ScheduleEditTest test);

    void inject(EventUpdateActivityTest test);

    void inject(EventCreateActivityTest test);

    void inject(ScanningTest test);

    void inject(EventFeedbackFragmentTest test);
    void inject(EventUserManagementFragmentTest test);

    void inject(LoginActivityTest test);

    void inject(EventPickingActivityTest test);

    void inject(NotificationsFragmentTest test);

    void inject(EventMapEditionFragmentTest test);

    @Component.Builder
    interface Builder extends ApplicationComponent.Builder {
        @Override
        TestComponent build();
    }
}
