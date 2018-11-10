package ch.epfl.sweng.eventmanager;

import android.app.Activity;
import android.app.Application;
import android.support.multidex.MultiDexApplication;
import ch.epfl.sweng.eventmanager.inject.DaggerApplicationComponent;
import ch.epfl.sweng.eventmanager.repository.room.RoomModule;
import com.twitter.sdk.android.core.Twitter;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;

import javax.inject.Inject;

/**
 * @author Louis Vialar
 */
public class EventManagerApplication extends MultiDexApplication implements HasActivityInjector {
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

        Twitter.initialize(this);
    }

    @Override
    public DispatchingAndroidInjector<Activity> activityInjector() {
        return activityDispatchingAndroidInjector;
    }
}
