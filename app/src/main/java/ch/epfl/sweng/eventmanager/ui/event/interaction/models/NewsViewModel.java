package ch.epfl.sweng.eventmanager.ui.event.interaction.models;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import ch.epfl.sweng.eventmanager.repository.NewsRepository;
import ch.epfl.sweng.eventmanager.repository.data.News;
import ch.epfl.sweng.eventmanager.repository.data.NewsOrTweet;
import com.twitter.sdk.android.core.models.Tweet;

import javax.inject.Inject;

import java.util.List;

public class NewsViewModel extends ViewModel {
    private static final String TAG = "NewsViewModel";

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

    private LiveData<List<Tweet>> nameToTweets(LiveData<String> twitterName) {
        return Transformations.switchMap(twitterName, name -> repository.getTweets(name));
    }

    private LiveData<List<NewsOrTweet>> combine(LiveData<List<News>> newsData, LiveData<List<Tweet>> tweetsData) {
        MediatorLiveData<List<NewsOrTweet>> data = new MediatorLiveData<>();
        data.addSource(newsData, news -> data.setValue(NewsOrTweet.mergeLists(news, tweetsData.getValue())));
        data.addSource(tweetsData, tweets -> data.setValue(NewsOrTweet.mergeLists(newsData.getValue(), tweets)));

        return data;
    }

    public LiveData<List<NewsOrTweet>> getNews(LiveData<String> twitterName) {
        return combine(news, nameToTweets(twitterName));
    }
}
