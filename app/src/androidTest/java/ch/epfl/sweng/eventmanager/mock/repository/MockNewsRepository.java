package ch.epfl.sweng.eventmanager.mock.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import ch.epfl.sweng.eventmanager.repository.NewsRepository;
import ch.epfl.sweng.eventmanager.repository.data.News;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Louis Vialar
 */
public class MockNewsRepository implements NewsRepository {
    private Map<Integer, List<News>> news = new HashMap<>();

    private List<News> getOrCreateNews(int eventId) {
        if (!news.containsKey(eventId)) {
            news.put(eventId, new ArrayList<>());
        }

        return news.get(eventId);
    }

    @Override
    public Task<Void> publishNews(int eventId, News news) {
        getOrCreateNews(eventId).add(news);
        return Tasks.call(() -> null); // call the task
    }

    @Override
    public LiveData<List<News>> getNews(int eventId) {
        MutableLiveData<List<News>> data = new MutableLiveData<>();
        data.setValue(getOrCreateNews(eventId));
        return data;
    }
}
