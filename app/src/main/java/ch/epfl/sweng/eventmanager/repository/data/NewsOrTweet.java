package ch.epfl.sweng.eventmanager.repository.data;

import androidx.annotation.NonNull;

import com.twitter.sdk.android.core.models.Tweet;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Louis Vialar
 */
public class NewsOrTweet implements Comparable<NewsOrTweet> {
    public static final int TYPE_TWEET = 1;
    public static final int TYPE_NEWS = 2;
    public static final int TYPE_FACEBOOK = 3;

    private final News news;
    private final Tweet tweet;
    private final Feed facebook;
    private final long time;

    NewsOrTweet(News news) {
        this.news = news;
        this.tweet = null;
        this.facebook = null;
        this.time = news.getDate();
    }

    NewsOrTweet(Tweet tweet) {
        this.news = null;
        this.tweet = tweet;
        this.facebook = null;
        this.time = Date.parse(tweet.createdAt);
    }

    NewsOrTweet(Feed facebook) {
        this.news = null;
        this.tweet = null;
        this.facebook = facebook;
        this.time = facebook.getTime().getTime();
    }

    public News getNews() {
        return news;
    }

    public Tweet getTweet() {
        return tweet;
    }

    public Feed getFacebook() {return facebook; }

    public long getTime() {
        return time;
    }

    @Override
    public int compareTo(@NonNull NewsOrTweet o) {
        return Long.compare(o.time, time);
    }

    public int getItemType() {
        if(this.tweet != null) {
            return TYPE_TWEET;
        }
        else if(this.facebook != null) {
            return TYPE_FACEBOOK;
        }
        return TYPE_NEWS;
    }

    public static List<NewsOrTweet> mergeLists(List<News> news, List<Tweet> tweets, List<Feed> facebookNews) {
        List<NewsOrTweet> target = new ArrayList<>();
        if (news != null)
            for (News n : news)
                target.add(new NewsOrTweet(n));

        if (tweets != null)
            for (Tweet t : tweets)
                target.add(new NewsOrTweet(t));

        if (facebookNews != null)
            for (Feed f : facebookNews)
                target.add(new NewsOrTweet(f));

        return target;
    }

    @Override
    @NonNull
    public String toString() {
        return "NewsOrTweet{" +
                "news=" + news +
                ", tweet=" + tweet +
                ", facebook news=" +
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
        if (facebook != null ? !facebook.equals(that.facebook) : that.facebook != null) return false;
        return tweet != null ? tweet.equals(that.tweet) : that.tweet == null;
    }
}
