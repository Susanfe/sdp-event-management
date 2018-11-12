package ch.epfl.sweng.eventmanager.repository.impl;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;
import ch.epfl.sweng.eventmanager.repository.NewsRepository;
import ch.epfl.sweng.eventmanager.repository.data.News;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.tweetui.TimelineResult;
import com.twitter.sdk.android.tweetui.UserTimeline;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Collections;
import java.util.List;

/**
 * @author Louis Vialar
 */
@Singleton
public class FirebaseNewsRepository implements NewsRepository {
    private static final String TAG = "FirebaseNewsRepository";

    @Inject
    public FirebaseNewsRepository() {
    }

    @Override
    public Task<Void> publishNews(int eventId, News news) {
        DatabaseReference dbRef = FirebaseDatabase.getInstance()
                .getReference("news")
                .child("event_" + eventId)
                .push(); // Create a new key in the list

        return dbRef.setValue(news);
    }

    @Override
    public LiveData<List<News>> getNews(int eventId) {
        DatabaseReference dbRef = FirebaseDatabase.getInstance()
                .getReference("news")
                .child("event_" + eventId);

        return FirebaseHelper.getList(dbRef, News.class);
    }

    @Override
    public LiveData<List<Tweet>> getTweets(String screenName) {
        MutableLiveData<List<Tweet>> data = new MutableLiveData<>();

        if (screenName != null) {
            Log.i(TAG, "Loading tweets for " + screenName + "!");
            UserTimeline userTimeline = new UserTimeline.Builder().screenName(screenName).build();
            userTimeline.next(null, new Callback<TimelineResult<Tweet>>() {
                @Override
                public void success(Result<TimelineResult<Tweet>> result) {
                    data.setValue(result.data.items);
                }

                @Override
                public void failure(TwitterException exception) {
                    Log.e(TAG, "Twitter error.", exception);
                    data.setValue(Collections.emptyList());
                }
            });
        } else {
            data.setValue(Collections.emptyList());
        }

        return data;
    }
}
