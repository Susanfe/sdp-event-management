package ch.epfl.sweng.eventmanager.repository.data;

import android.support.annotation.NonNull;
import ch.epfl.sweng.eventmanager.ui.eventShowcase.fragments.NewsFragment;
import com.twitter.sdk.android.core.models.Tweet;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Louis Vialar
 */
public class NewsOrTweet implements Comparable<NewsOrTweet> {
    public static final int TYPE_TWEET = 1;
    public static final int TYPE_NEWS = 2;

    private final News news;
    private final Tweet tweet;
    private final long time;

    NewsOrTweet(News news) {
        this.news = news;
        this.tweet = null;
        this.time = news.getDate();
    }

    NewsOrTweet(Tweet tweet) {
        this.news = null;
        this.tweet = tweet;
        this.time = Date.parse(tweet.createdAt);
    }

    public News getNews() {
        return news;
    }

    public Tweet getTweet() {
        return tweet;
    }

    public long getTime() {
        return time;
    }

    @Override
    public int compareTo(@NonNull NewsOrTweet o) {
        return Long.compare(o.time, time);
    }

    public int getItemType() {
        return this.tweet == null ? TYPE_NEWS : TYPE_TWEET;
    }

    public static List<NewsOrTweet> mergeLists(List<News> news, List<Tweet> tweets) {
        List<NewsOrTweet> target = new ArrayList<>();
        for (News n : news) target.add(new NewsOrTweet(n));
        for (Tweet t : tweets) target.add(new NewsOrTweet(t));
        return target;
    }

    @Override
    public String toString() {
        return "NewsOrTweet{" +
                "news=" + news +
                ", tweet=" + tweet +
                ", time=" + time +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NewsOrTweet)) return false;

        NewsOrTweet that = (NewsOrTweet) o;

        if (time != that.time) return false;
        if (news != null ? !news.equals(that.news) : that.news != null) return false;
        return tweet != null ? tweet.equals(that.tweet) : that.tweet == null;
    }
}
