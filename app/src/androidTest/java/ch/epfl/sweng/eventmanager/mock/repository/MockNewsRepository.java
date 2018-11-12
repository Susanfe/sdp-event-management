package ch.epfl.sweng.eventmanager.mock.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import ch.epfl.sweng.eventmanager.repository.NewsRepository;
import ch.epfl.sweng.eventmanager.repository.data.News;
import ch.epfl.sweng.eventmanager.test.ObservableList;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.twitter.sdk.android.core.models.Tweet;

import java.util.*;

/**
 * @author Louis Vialar
 */
public class MockNewsRepository implements NewsRepository {
    private Map<Integer, ObservableList<News>> news = new HashMap<>();

    private ObservableList<News> getOrCreateNews(int eventId) {
        if (!news.containsKey(eventId)) {
            news.put(eventId, new ObservableList<>());
        }

        System.err.println("Getting newslist for event " + eventId + ". Returning: " + Arrays.toString(news.get(eventId).getUnderlyingList().toArray()));

        return news.get(eventId);
    }

    @Override
    public Task<Void> publishNews(int eventId, News news) {
        getOrCreateNews(eventId).add(news);

        System.err.println("Created news " + news + " in event " + eventId);

        return Tasks.call(() -> null); // call the task
    }

    @Override
    public LiveData<List<News>> getNews(int eventId) {
        return getOrCreateNews(eventId);
    }

    @Override
    public LiveData<List<Tweet>> getTweets(String screenName) {
        return new MutableLiveData<>();
    }
}
