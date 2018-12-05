package ch.epfl.sweng.eventmanager.ui.user;

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
    abstract SignUpActivity contributeSignUpActivityInjector();

    @ContributesAndroidInjector
    abstract DisplayAccountActivity contributeDisplayAccountActivityInjector();

}
