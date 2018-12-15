package ch.epfl.sweng.eventmanager.ui.event.interaction.models;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import ch.epfl.sweng.eventmanager.repository.NewsRepository;
import ch.epfl.sweng.eventmanager.repository.data.*;
import com.twitter.sdk.android.core.models.Tweet;

import javax.inject.Inject;
import java.util.ArrayList;
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

    private LiveData<List<Feed>> nameToFacebookNews(LiveData<String> facebookName) {
        return Transformations.switchMap(facebookName, name -> repository.getFacebookNews(name));
    }

    private LiveData<List<SocialNetworkPost>> combine(LiveData<List<News>> newsData, LiveData<List<Tweet>> tweetsData, LiveData<List<Feed>> facebookData) {
        MediatorLiveData<List<SocialNetworkPost>> data = new MediatorLiveData<>();
        data.addSource(newsData, news -> data.setValue(mergeLists(news, tweetsData.getValue(), facebookData.getValue())));
        data.addSource(tweetsData, tweets -> data.setValue(mergeLists(newsData.getValue(), tweets, facebookData.getValue())));
        data.addSource(facebookData, facebook -> data.setValue(mergeLists(newsData.getValue(), tweetsData.getValue(), facebook)));

        return data;
    }

    private List<SocialNetworkPost> mergeLists(List<News> news, List<Tweet> tweets, List<Feed> fb) {
        List<SocialNetworkPost> list = new ArrayList<>();
        if (news != null)
            for (News n : news)
                list.add(new NewsWrapper(n));

        if (tweets != null)
            for (Tweet t : tweets)
                list.add(new TweetWrapper(t));

        if (fb != null)
            for (Feed f : fb)
                list.add(new FacebookPostWrapper(f));

        return list;
    }

    public LiveData<List<SocialNetworkPost>> getNews(LiveData<String> twitterName, LiveData<String> facebookName) {
        return combine(news, nameToTweets(twitterName), nameToFacebookNews(facebookName));
    }

}
