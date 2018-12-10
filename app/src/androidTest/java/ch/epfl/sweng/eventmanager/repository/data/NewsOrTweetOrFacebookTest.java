package ch.epfl.sweng.eventmanager.repository.data;

import com.twitter.sdk.android.core.models.Tweet;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author Louis Vialar
 */
public class NewsOrTweetOrFacebookTest {
    private Tweet t = new Tweet(null, "Wed Aug 27 13:08:45 +0000 2008", null, null, null, null, false, null, 0, null, null, 0, null, 0, null, null, null, false, null, 0, null, null, 0, false, null, null, null, null, false, null, false, null, null, null);
    private News n = new News("News1", 1540391282000L, "News 1 Content");
    private Feed f;
    private NewsOrTweetOrFacebook tweet = new NewsOrTweetOrFacebook(t);
    private NewsOrTweetOrFacebook news = new NewsOrTweetOrFacebook(n);
    private NewsOrTweetOrFacebook facebookNews;

    @Before
    public void setUpFeed() throws JSONException {
        JSONObject obj = new JSONObject();
        obj.put("message", "event in blue !");
        obj.put("created_time", "2018-12-03T16:02:53+0000");
        obj.put("id", "11");
        f = new Feed(obj);
        facebookNews = new NewsOrTweetOrFacebook(f);
    }


    @Test
    public void getNews() {
        assertNull(tweet.getNews());
        assertNotNull(news.getNews());
        assertEquals(n, news.getNews());
        assertNull(facebookNews.getNews());
    }

    @Test
    public void getTweet() {
        assertNotNull(tweet.getTweet());
        assertEquals(t, tweet.getTweet());
        assertNull(news.getTweet());
        assertNull(facebookNews.getTweet());
    }

    @Test
    public void getFacebookNews() {
        assertNotNull(facebookNews.getFacebook());
        assertEquals(f, facebookNews.getFacebook());
        assertNull(news.getFacebook());
        assertNull(news.getFacebook());
    }

    @Test
    public void getTime() {
        assertEquals(1219842525000L, tweet.getTime());
        assertEquals(1540391282000L, news.getTime());
        assertEquals(1543852973000L, facebookNews.getTime());
    }

    @Test
    public void compareTo() {
        assertTrue(news.compareTo(tweet) < 0);
        assertTrue(news.compareTo(facebookNews) > 0);
        assertTrue(tweet.compareTo(facebookNews) > 0);
    }

    @Test
    public void getItemType() {
        assertEquals(NewsOrTweetOrFacebook.TYPE_TWEET, tweet.getItemType());
        assertEquals(NewsOrTweetOrFacebook.TYPE_NEWS, news.getItemType());
        assertEquals(NewsOrTweetOrFacebook.TYPE_FACEBOOK, facebookNews.getItemType());
    }

    @Test
    public void mergeLists() {
        List<NewsOrTweetOrFacebook> list = new ArrayList<>();
        list.add(news);
        list.add(tweet);
        list.add(facebookNews);

        List<NewsOrTweetOrFacebook> test = NewsOrTweetOrFacebook.mergeLists(Collections.singletonList(n), Collections.singletonList(t), Collections.singletonList(f));

        // Sort both
        Collections.sort(test);
        Collections.sort(list);

        // Check
        for (int i = 0; i < list.size(); ++i) {
            assertEquals("inequality at item " + i, list.get(i), test.get(i));
        }
    }

    @Test
    public void mergeNullLists() {
        List<NewsOrTweetOrFacebook> test = NewsOrTweetOrFacebook.mergeLists(null, null, null);
        assertTrue(test.isEmpty());
    }
}