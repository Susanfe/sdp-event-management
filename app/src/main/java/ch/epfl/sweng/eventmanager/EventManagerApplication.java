package ch.epfl.sweng.eventmanager;

import android.app.Activity;
import android.app.Application;
import ch.epfl.sweng.eventmanager.inject.DaggerApplicationComponent;
import ch.epfl.sweng.eventmanager.room.RoomModule;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;

import javax.inject.Inject;

/**
 * @author Louis Vialar
 */
public class EventManagerApplication extends Application implements HasActivityInjector {
    @Inject
    DispatchingAndroidInjector<Activity> activityDispatchingAndroidInjector;

    @Override
    public void onCreate() {
        super.onCreate();
        DaggerApplicationComponent
                .builder()
                .application(this)
                .room(new RoomModule(this))
                .build()
                .inject(this);

        System.out.println(this.activityDispatchingAndroidInjector);

    }

    @Override
    public DispatchingAndroidInjector<Activity> activityInjector() {
        return activityDispatchingAndroidInjector;
    }
}
