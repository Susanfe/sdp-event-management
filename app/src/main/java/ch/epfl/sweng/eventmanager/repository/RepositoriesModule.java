package ch.epfl.sweng.eventmanager.repository;

import dagger.Binds;
import dagger.Module;

import javax.inject.Singleton;

/**
 * @author Louis Vialar
 */
@Module
public abstract class RepositoriesModule {
    @Binds
    @Singleton
    abstract NewsRepository providesNewsRepository(FirebaseNewsRepository repository);

}
