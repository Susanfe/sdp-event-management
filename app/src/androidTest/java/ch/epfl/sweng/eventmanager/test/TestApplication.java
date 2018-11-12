package ch.epfl.sweng.eventmanager.test;

import ch.epfl.sweng.eventmanager.EventManagerApplication;
import ch.epfl.sweng.eventmanager.inject.DaggerApplicationComponent;
import ch.epfl.sweng.eventmanager.mock.repository.MockRepositoriesModule;
import ch.epfl.sweng.eventmanager.repository.room.RoomModule;

/**
 * @author Louis Vialar
 */
public class TestApplication extends EventManagerApplication {
    @Override
    public void initDaggerComponent() {
        DaggerApplicationComponent
                .builder()
                .application(this)
                .room(new RoomModule(this))
                .repositories(new MockRepositoriesModule())
                .build()
                .inject(this);
    }
}
