package ch.epfl.sweng.eventmanager.test.repository;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.twitter.sdk.android.core.models.Tweet;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import ch.epfl.sweng.eventmanager.repository.NewsRepository;
import ch.epfl.sweng.eventmanager.repository.data.FacebookPost;
import ch.epfl.sweng.eventmanager.repository.data.News;
import ch.epfl.sweng.eventmanager.test.ObservableList;

/**
 * @author Louis Vialar
 */
public class MockNewsRepository implements NewsRepository {
    private Map<Integer, ObservableList<News>> news = new HashMap<>();
    private boolean nextWillFail = false;
    private MutableLiveData<List<FacebookPost>> feedFacebook = new MutableLiveData<>();

    private void addFacebookPost() throws JSONException {
        JSONObject obj1 = new JSONObject();
        obj1.put("created_time", "2018-12-03T16:02:53+0000");
        obj1.put("message", "event in blue !");
        obj1.put("id", "11");
        obj1.put("full_picture", "https://www.google.com/search?q=photo&source=lnms&tbm=isch&sa=X&ved=0ahUKEwjMoc-EtZXfAhU5VBUIHXqWBrgQ_AUIDigB&biw=1439&bih=674&dpr=2#imgrc=ZcP5MOOiyNu2iM:");
        obj1.put("description", "black or white tie");
        obj1.put("name", "fake event");
        FacebookPost facebookPost = new FacebookPost(obj1);
        List<FacebookPost> facebookPostList = new ArrayList<>();
        facebookPostList.add(facebookPost);
        feedFacebook.setValue(facebookPostList);
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
    public LiveData<List<FacebookPost>> getFacebookNews(String screenName) {
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
