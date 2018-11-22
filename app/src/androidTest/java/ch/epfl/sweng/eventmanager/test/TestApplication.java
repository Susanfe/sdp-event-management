package ch.epfl.sweng.eventmanager.test;

import ch.epfl.sweng.eventmanager.EventManagerApplication;
import ch.epfl.sweng.eventmanager.repository.room.RoomModule;
import ch.epfl.sweng.eventmanager.test.inject.DaggerTestComponent;
import ch.epfl.sweng.eventmanager.test.inject.TestComponent;

/**
 * @author Louis Vialar
 */
public class TestApplication extends EventManagerApplication {
    public static TestComponent component;

    @Override
    public void initDaggerComponent() {
        component = (TestComponent) DaggerTestComponent
                .builder()
                .application(this)
                .room(new RoomModule(this))
                .build();

        component.inject(this);
    }
}
