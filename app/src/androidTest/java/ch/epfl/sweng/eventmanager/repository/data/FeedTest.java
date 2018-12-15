package ch.epfl.sweng.eventmanager.repository.data;

import android.net.Uri;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class FeedTest {
    private Feed feed1;
    private Feed feed2;
    private Feed feed3;
    private Feed feed4;

    @Before
    public void setUp() throws JSONException {
        final String message = "event in blue !";
        final String createdTime = "2018-12-03T16:02:53+0000";
        JSONObject obj1 = new JSONObject();
        obj1.put("created_time", createdTime);
        obj1.put("message", message);
        obj1.put("id", "11");
        obj1.put("full_picture", "url");
        obj1.put("description", "Come with happiness");
        obj1.put("name", "title");
        feed1 = new Feed(obj1);

        JSONObject obj2 = new JSONObject();
        obj2.put("created_time", createdTime);
        obj2.put("message", message);
        obj2.put("id", "11");
        feed2 = new Feed(obj2);

        JSONObject obj3 = new JSONObject();
        obj3.put("created_time", createdTime);
        obj3.put("message", "event in blue");
        obj3.put("id", "11");
        feed3 = new Feed(obj3);

        JSONObject obj4 = new JSONObject();
        obj4.put("created_time", createdTime);
        obj4.put("message", message);
        obj4.put("id", "10");
        feed4 = new Feed(obj4);
    }

    @Test
    public void constructorMakeParsingCorrectly() {
        assertEquals("event in blue !", feed1.getContent());
        assertEquals("11", feed1.getId());
        assertEquals("title", feed1.getName());
        assertEquals("Come with happiness", feed1.getDescription());
        assertThat(feed1.getImageURL(), is(Uri.parse("url")));
    }

    @Test
    public void constructorWorkWithException() throws JSONException {
        String jStr= "{\"name\":\"hahaha\"}";
        JSONObject obj5 = new JSONObject(jStr);
        new Feed(obj5);
    }

    @Test
    public void dateAsStringWork() {
        assertTrue(feed1.dateAsString().contains("Dec 3,"));
    }

    @Test
    public void equalMethodWork() {
        assertTrue(feed1.equals(feed2));
        assertTrue(!feed1.equals(feed3));
        assertTrue(!feed1.equals(feed4));
    }

    @Test
    public void containsTypesWork() {
        assertTrue(feed1.hasContent());
        assertTrue(feed1.hasDescription());
        assertTrue(feed1.hasImage());
        assertTrue(feed1.hasName());

        assertFalse(feed2.hasImage());
        assertFalse(feed2.hasDescription());
        assertFalse(feed2.hasName());
    }

}
