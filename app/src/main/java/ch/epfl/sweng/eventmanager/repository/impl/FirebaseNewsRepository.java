package ch.epfl.sweng.eventmanager.repository.impl;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import android.os.Bundle;
import android.util.Log;
import ch.epfl.sweng.eventmanager.repository.NewsRepository;
import ch.epfl.sweng.eventmanager.repository.data.Feed;
import ch.epfl.sweng.eventmanager.repository.data.News;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.tweetui.TimelineResult;
import com.twitter.sdk.android.tweetui.UserTimeline;

import org.json.JSONArray;
import org.json.JSONObject;

import javax.inject.Inject;
import javax.inject.Singleton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Louis Vialar
 */
@Singleton
public class FirebaseNewsRepository implements NewsRepository {
    private static final String TAG = "FirebaseNewsRepository";
    private static final String FIREBASE_REF = "news";

    @Inject
    public FirebaseNewsRepository() {
    }

    @Override
    public Task<Void> publishNews(int eventId, News news) {
        return FirebaseHelper.publishElement(eventId, FIREBASE_REF, news);
    }

    @Override
    public LiveData<List<News>> getNews(int eventId) {
        DatabaseReference dbRef = FirebaseDatabase.getInstance()
                .getReference(FIREBASE_REF)
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

    @Override
    public LiveData<List<Feed>> getFacebookNews(String screenName) {
        MutableLiveData<List<Feed>> data = new MutableLiveData<>();
        List<Feed> feedList = new ArrayList<>();

        new GraphRequest(AccessToken.getCurrentAccessToken(), "/793504527660961/feed", null, HttpMethod.GET,
                response -> {
                    /* handle the result */
                    try {
                        Log.v("facebook request", "start");
                        JSONObject jObjResponse = new JSONObject(String.valueOf(response.getJSONObject()));
                        JSONArray jArray = jObjResponse.getJSONArray("data");

                        for (int i = 0; i < jArray.length(); i++) {
                            JSONObject jObject = jArray.getJSONObject(i);
                            Feed feed = new Feed(jObject);
                            feedList.add(feed);
                        }
                        data.setValue(feedList);
                    } catch (Exception e) {
                        e.printStackTrace();
                        data.setValue(Collections.EMPTY_LIST);
                    }
                }
        ).executeAsync();
        return data;
    }
}
