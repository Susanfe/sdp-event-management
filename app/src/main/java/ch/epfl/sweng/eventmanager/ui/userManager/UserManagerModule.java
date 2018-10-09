package ch.epfl.sweng.eventmanager.ui.userManager;

import ch.epfl.sweng.eventmanager.ui.eventShowcase.MapActivity;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * @author Louis Vialar
 */
@Module
public abstract class UserManagerModule {
    @ContributesAndroidInjector
    abstract LoginActivity contributeLoginActivityInjector();

    @ContributesAndroidInjector
    abstract DisplayAccountActivity contributeDisplayAccountActivityInjector();

}
