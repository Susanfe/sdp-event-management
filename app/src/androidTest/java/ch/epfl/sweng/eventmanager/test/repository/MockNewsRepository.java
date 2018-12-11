package ch.epfl.sweng.eventmanager.test.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import ch.epfl.sweng.eventmanager.repository.NewsRepository;
import ch.epfl.sweng.eventmanager.repository.data.Feed;
import ch.epfl.sweng.eventmanager.repository.data.News;
import ch.epfl.sweng.eventmanager.test.ObservableList;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.twitter.sdk.android.core.models.Tweet;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;

/**
 * @author Louis Vialar
 */
public class MockNewsRepository implements NewsRepository {
    private Map<Integer, ObservableList<News>> news = new HashMap<>();
    private boolean nextWillFail = false;
    private MutableLiveData<List<Feed>> feedFacebook = new MutableLiveData<>();

    private void addFacebookPost() throws JSONException {
        JSONObject obj1 = new JSONObject();
        obj1.put("created_time", "2018-12-03T16:02:53+0000");
        obj1.put("message", "event in blue !");
        obj1.put("id", "11");
        Feed feed = new Feed(obj1);
        List<Feed> feedList = new ArrayList<>();
        feedList.add(feed);
        feedFacebook.setValue(feedList);
    }

    private ObservableList<News> getOrCreateNews(int eventId) {
        if (!news.containsKey(eventId)) {
            news.put(eventId, new ObservableList<>());
        }

        // TODO handle null exception
        System.err.println("Getting newslist for event " + eventId + ". Returning: " +
                Arrays.toString(news.get(eventId).getUnderlyingList().toArray()));

        return news.get(eventId);
    }

    @Override
    public Task<Void> publishNews(int eventId, News news) {
        if (nextWillFail) {
            nextWillFail = false;
            return Tasks.forException(new Exception());
        }

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

    @Override
    public LiveData<List<Feed>> getFacebookNews(String screenName) {
        try {
            addFacebookPost();
            return feedFacebook;
        }
        catch (JSONException e) {
            e.printStackTrace();
            return new MutableLiveData<>();
        }
    }

    public void setNextInsertToFail() {
        nextWillFail = true;
    }

    public void clearNews() {
        for (ObservableList<News> news : this.news.values()) {
            news.clear();
        }
    }
}
