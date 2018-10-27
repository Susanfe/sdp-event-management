package ch.epfl.sweng.eventmanager.ui.eventShowcase.models;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import ch.epfl.sweng.eventmanager.repository.JoinedScheduleItemRepository;
import ch.epfl.sweng.eventmanager.repository.NewsRepository;
import ch.epfl.sweng.eventmanager.repository.ScheduledItemRepository;
import ch.epfl.sweng.eventmanager.repository.data.JoinedScheduleItem;
import ch.epfl.sweng.eventmanager.repository.data.News;
import ch.epfl.sweng.eventmanager.repository.data.ScheduledItem;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
