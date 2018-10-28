package ch.epfl.sweng.eventmanager.ui.eventShowcase.models;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import ch.epfl.sweng.eventmanager.repository.NewsRepository;
import ch.epfl.sweng.eventmanager.repository.data.News;

import javax.inject.Inject;
import java.util.List;

public class NewsViewModel extends ViewModel {
    private LiveData<List<News>> news;
    private NewsRepository repository;

    @Inject
    public NewsViewModel(NewsRepository repository) {
        this.repository = repository;
    }

    public void init(int eventId) {
        if (this.news != null) {
            return;
        }

        this.news = repository.getNews(eventId);
    }

    public LiveData<List<News>> getNews() {
        return news;
    }
}
