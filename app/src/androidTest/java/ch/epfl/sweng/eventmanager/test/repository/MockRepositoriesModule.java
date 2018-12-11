package ch.epfl.sweng.eventmanager.test.repository;

import ch.epfl.sweng.eventmanager.repository.CloudFunction;
import ch.epfl.sweng.eventmanager.repository.EventRepository;
import ch.epfl.sweng.eventmanager.repository.FeedbackRepository;
import ch.epfl.sweng.eventmanager.repository.NewsRepository;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

/**
 * @author Louis Vialar
 */
@Module
public class MockRepositoriesModule {
    @Provides
    @Singleton
    public MockNewsRepository providesMockNewsRepository() {
        return new MockNewsRepository();
    }

    @Provides
    @Singleton
    public NewsRepository providesNewsRepository(MockNewsRepository repository) {
        return repository;
    }


    @Provides
    @Singleton
    public MockEventsRepository providesMockEventsRepository() {
        return new MockEventsRepository();
    }

    @Provides
    @Singleton
    public EventRepository providesEventRepository(MockEventsRepository repository) {
        return repository;
    }

    @Provides
    @Singleton
    public CloudFunction providesCloudFunction(MockEventsRepository repository) {
        return repository;
    }

    @Provides
    @Singleton
    public FeedbackRepository providesFeedbackRepository(MockFeedbackRepository repository) {return repository; }

    @Provides
    @Singleton
    public MockFeedbackRepository providesMockFeedbackRepository() {return new MockFeedbackRepository();}
}
