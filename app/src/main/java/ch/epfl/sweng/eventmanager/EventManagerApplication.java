package ch.epfl.sweng.eventmanager;

import android.app.Activity;
import android.support.multidex.MultiDexApplication;
import android.support.v4.app.Fragment;
import ch.epfl.sweng.eventmanager.inject.DaggerApplicationComponent;
import ch.epfl.sweng.eventmanager.repository.room.RoomModule;
import com.twitter.sdk.android.core.Twitter;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import dagger.android.support.HasSupportFragmentInjector;

import javax.inject.Inject;

/**
 * @author Louis Vialar
 */
public class EventManagerApplication extends MultiDexApplication implements HasActivityInjector, HasSupportFragmentInjector {
    @Inject
    DispatchingAndroidInjector<Activity> activityDispatchingAndroidInjector;
    @Inject
    DispatchingAndroidInjector<Fragment> fragmentDispatchingAndroidInjector;

    public void initDaggerComponent() {
        DaggerApplicationComponent
                .builder()
                .application(this)
                .room(new RoomModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        initDaggerComponent();

        Twitter.initialize(this);
    }

    @Override
    public DispatchingAndroidInjector<Activity> activityInjector() {
        return activityDispatchingAndroidInjector;
    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return fragmentDispatchingAndroidInjector;
    }
}
