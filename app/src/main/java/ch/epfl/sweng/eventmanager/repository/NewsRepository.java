package ch.epfl.sweng.eventmanager.repository;

import android.arch.lifecycle.LiveData;
import ch.epfl.sweng.eventmanager.repository.data.News;
import com.google.android.gms.tasks.Task;
import com.twitter.sdk.android.core.models.Tweet;

import java.util.List;

/**
 * @author Louis Vialar
 */
public interface NewsRepository {
    Task<Void> publishNews(int eventId, News news);

    LiveData<List<News>> getNews(int eventId);

    LiveData<List<Tweet>> getTweets(String screenName);
}
