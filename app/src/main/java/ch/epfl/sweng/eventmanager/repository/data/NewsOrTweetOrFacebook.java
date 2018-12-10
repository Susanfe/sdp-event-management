package ch.epfl.sweng.eventmanager.repository.data;

import androidx.annotation.NonNull;
import com.twitter.sdk.android.core.models.Tweet;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Louis Vialar
 */
public class NewsOrTweetOrFacebook implements Comparable<NewsOrTweetOrFacebook> {
    public static final int TYPE_TWEET = 1;
    public static final int TYPE_NEWS = 2;
    public static final int TYPE_FACEBOOK = 3;

    private final News news;
    private final Tweet tweet;
    private final FacebookPost facebook;
    private final long time;

    NewsOrTweetOrFacebook(News news) {
        this.news = news;
        this.tweet = null;
        this.facebook = null;
        this.time = news.getDate();
    }

    NewsOrTweetOrFacebook(Tweet tweet) {
        this.news = null;
        this.tweet = tweet;
        this.facebook = null;
        this.time = Date.parse(tweet.createdAt);
    }

    NewsOrTweetOrFacebook(FacebookPost facebook) {
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

    public FacebookPost getFacebook() {return facebook; }

    public long getTime() {
        return time;
    }

    @Override
    public int compareTo(@NonNull NewsOrTweetOrFacebook o) {
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

    public static List<NewsOrTweetOrFacebook> mergeLists(List<News> news, List<Tweet> tweets, List<FacebookPost> facebookNews) {
        List<NewsOrTweetOrFacebook> target = new ArrayList<>();
        if (news != null)
            for (News n : news)
                target.add(new NewsOrTweetOrFacebook(n));

        if (tweets != null)
            for (Tweet t : tweets)
                target.add(new NewsOrTweetOrFacebook(t));

        if (facebookNews != null)
            for (FacebookPost f : facebookNews)
                target.add(new NewsOrTweetOrFacebook(f));

        return target;
    }

    @Override
    @NonNull
    public String toString() {
        return "NewsOrTweetOrFacebook{" +
                "news=" + news +
                ", tweet=" + tweet +
                ", facebook=" +facebook +
                ", time=" + time +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NewsOrTweetOrFacebook)) return false;

        NewsOrTweetOrFacebook that = (NewsOrTweetOrFacebook) o;

        if (time != that.time) return false;
        if (news != null ? !news.equals(that.news) : that.news != null) return false;
        if (facebook != null ? !facebook.equals(that.facebook) : that.facebook != null) return false;
        return tweet != null ? tweet.equals(that.tweet) : that.tweet == null;
    }
}
