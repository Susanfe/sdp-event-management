package ch.epfl.sweng.eventmanager.repository;

import ch.epfl.sweng.eventmanager.repository.impl.FirebaseNewsRepository;
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
