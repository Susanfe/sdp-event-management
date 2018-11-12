package ch.epfl.sweng.eventmanager.ui.eventShowcase.models;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.util.Log;
import ch.epfl.sweng.eventmanager.repository.NewsRepository;
import ch.epfl.sweng.eventmanager.repository.data.News;
import ch.epfl.sweng.eventmanager.repository.data.NewsOrTweet;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.tweetui.TimelineResult;
import com.twitter.sdk.android.tweetui.UserTimeline;

import javax.inject.Inject;
import java.util.Collections;
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

    public LiveData<List<NewsOrTweet>> getNews(LiveData<String> twitterName) {
        return Transformations.switchMap(news, list ->
                Transformations.switchMap(twitterName, screenName -> {
                    MutableLiveData<List<NewsOrTweet>> data = new MutableLiveData<>();
                    data.setValue(NewsOrTweet.mergeLists(list, Collections.emptyList()));

                    if (screenName != null) {
                        Log.i(TAG, "Loading tweets for " + screenName + "!");
                        UserTimeline userTimeline = new UserTimeline.Builder().screenName(screenName).build();
                        userTimeline.next(null, new Callback<TimelineResult<Tweet>>() {
                            @Override
                            public void success(Result<TimelineResult<Tweet>> result) {
                                Log.i(TAG, "Twitter result. " + result.data.items.toString());

                                data.setValue(NewsOrTweet.mergeLists(list, result.data.items));
                            }

                            @Override
                            public void failure(TwitterException exception) {
                                Log.e(TAG, "Twitter error.", exception);
                            }
                        });

                    }
                    return data;

                })
        );
    }
}
