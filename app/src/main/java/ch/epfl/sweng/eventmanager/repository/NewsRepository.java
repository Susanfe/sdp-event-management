package ch.epfl.sweng.eventmanager.repository;

import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import ch.epfl.sweng.eventmanager.repository.data.News;
import ch.epfl.sweng.eventmanager.repository.impl.FirebaseHelper;
import ch.epfl.sweng.eventmanager.repository.internal.DatabaseReferenceWrapper;
import ch.epfl.sweng.eventmanager.repository.internal.FirebaseDatabaseWrapper;
import ch.epfl.sweng.eventmanager.repository.internal.TwitterWrapper;
import ch.epfl.sweng.eventmanager.repository.internal.UserTimelineWrapper;
import com.google.android.gms.tasks.Task;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.tweetui.TimelineResult;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Collections;
import java.util.List;

/**
 * @author Louis Vialar
 */
@Singleton
public final class NewsRepository {
    private static final String TAG = "NewsRepository";

    private FirebaseDatabaseWrapper db;
    private TwitterWrapper twitter;

    @Inject
    public NewsRepository(FirebaseDatabaseWrapper db, TwitterWrapper twitter) {
        this.db = db;
        this.twitter = twitter;
    }

    public Task<Void> publishNews(int eventId, News news) {
        DatabaseReferenceWrapper dbRef = db
                .getReference("news")
                .child("event_" + eventId)
                .push(); // Create a new key in the list

        return dbRef.setValue(news);
    }

    public LiveData<List<News>> getNews(int eventId) {
        DatabaseReferenceWrapper dbRef = db
                .getReference("news")
                .child("event_" + eventId);

        return FirebaseHelper.getList(dbRef, News.class);
    }

    public LiveData<List<Tweet>> getTweets(String screenName) {
        MutableLiveData<List<Tweet>> data = new MutableLiveData<>();

        if (screenName != null) {
            Log.i(TAG, "Loading tweets for " + screenName + "!");
            UserTimelineWrapper userTimeline = twitter.getUserTimeline(screenName);
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
