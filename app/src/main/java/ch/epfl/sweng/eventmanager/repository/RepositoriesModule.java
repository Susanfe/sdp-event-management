package ch.epfl.sweng.eventmanager.repository;

import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

/**
 * @author Louis Vialar
 */
@Module
public class RepositoriesModule {
    @Provides @Singleton
    public NewsRepository providesNewsRepository() {
        return new FirebaseNewsRepository();
    }
}
