package ch.epfl.sweng.eventmanager.repository.data;

import com.twitter.sdk.android.core.models.Tweet;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

/**
 * @author Louis Vialar
 */
public class NewsOrTweetTest {
    private Tweet t = new Tweet(null, "Wed Aug 27 13:08:45 +0000 2008", null, null, null, null, false, null, 0, null, null, 0, null, 0, null, null, null, false, null, 0, null, null, 0, false, null, null, null, null, false, null, false, null, null, null);
    private News n = new News("News1", 1540391282000L, "News 1 Content");
    private NewsOrTweet tweet = new NewsOrTweet(t);
    private NewsOrTweet news = new NewsOrTweet(n);


    @Test
    public void getNews() {
        assertNull(tweet.getNews());
        assertNotNull(news.getNews());
        assertEquals(n, news.getNews());
    }

    @Test
    public void getTweet() {
        assertNotNull(tweet.getTweet());
        assertEquals(t, tweet.getTweet());
        assertNull(news.getTweet());
    }

    @Test
    public void getTime() {
        assertEquals(1219842525000L, tweet.getTime());
        assertEquals(1540391282000L, news.getTime());
    }

    @Test
    public void compareTo() {
        assertTrue(news.compareTo(tweet) < 0);
    }

    @Test
    public void getItemType() {
        assertEquals(NewsOrTweet.TYPE_TWEET, tweet.getItemType());
        assertEquals(NewsOrTweet.TYPE_NEWS, news.getItemType());
    }

    @Test
    public void mergeLists() {
        List<NewsOrTweet> list = new ArrayList<>();
        list.add(news);
        list.add(tweet);

        List<NewsOrTweet> test = NewsOrTweet.mergeLists(Collections.singletonList(n), Collections.singletonList(t));

        // Sort both
        Collections.sort(test);
        Collections.sort(list);

        // Check
        for (int i = 0; i < list.size(); ++i) {
            assertEquals("inequality at item " + i, list.get(i), test.get(i));
        }

    }
}