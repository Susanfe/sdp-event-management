package ch.epfl.sweng.eventmanager.inject;

import android.app.Application;
import android.content.Context;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

/**
 * This is the main module of the app. This is used by Dagger2 to build the dependency injection tree. This should not
 * be touched. For more information:
 * @link https://google.github.io/dagger/android
 * @link https://medium.com/@iammert/new-android-injector-with-dagger-2-part-1-8baa60152abe
 * @author Louis Vialar
 */
@Module
public class ApplicationModule {
    @Provides
    @Singleton
    Context provideContext(Application application) {
        return application;
    }
}
