package ch.epfl.sweng.eventmanager.mock.repository;

import ch.epfl.sweng.eventmanager.repository.NewsRepository;
import ch.epfl.sweng.eventmanager.repository.RepositoriesModule;
import dagger.Module;
import dagger.Provides;

/**
 * @author Louis Vialar
 */
@Module
public class MockRepositoriesModule extends RepositoriesModule {
    @Override @Provides
    public NewsRepository providesNewsRepository() {
        return new MockNewsRepository();
    }
}
