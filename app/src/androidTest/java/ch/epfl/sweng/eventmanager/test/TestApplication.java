package ch.epfl.sweng.eventmanager.test;

import ch.epfl.sweng.eventmanager.EventManagerApplication;
import ch.epfl.sweng.eventmanager.inject.ApplicationComponent;
import ch.epfl.sweng.eventmanager.inject.DaggerApplicationComponent;
import ch.epfl.sweng.eventmanager.mock.repository.MockRepositoriesModule;
import ch.epfl.sweng.eventmanager.repository.room.RoomModule;

/**
 * @author Louis Vialar
 */
public class TestApplication extends EventManagerApplication {
    public static ApplicationComponent component;

    @Override
    public void initDaggerComponent() {
        component = DaggerApplicationComponent
                .builder()
                .application(this)
                .room(new RoomModule(this))
                .repositories(new MockRepositoriesModule())
                .build();

        component.inject(this);
    }
}
