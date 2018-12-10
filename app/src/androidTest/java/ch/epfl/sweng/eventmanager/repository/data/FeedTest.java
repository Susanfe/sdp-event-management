package ch.epfl.sweng.eventmanager.repository.data;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class FeedTest {
    Feed feed1;
    Feed feed2;
    Feed feed3;
    Feed feed4;

    @Before
    public void setUp() throws JSONException {
        String message = "event in blue !";
        JSONObject obj1 = new JSONObject();
        obj1.put("created_time", "2018-12-03T16:02:53+0000");
        obj1.put("message", message);
        obj1.put("id", "11");
        feed1 = new Feed(obj1);

        JSONObject obj2 = new JSONObject();
        obj2.put("created_time", "2018-12-03T16:02:53+0000");
        obj2.put("message", message);
        obj2.put("id", "11");
        feed2 = new Feed(obj2);

        JSONObject obj3 = new JSONObject();
        obj3.put("created_time", "2018-12-03T16:02:53+0000");
        obj3.put("message", "event in blue");
        obj3.put("id", "11");
        feed3 = new Feed(obj3);

        JSONObject obj4 = new JSONObject();
        obj4.put("created_time", "2018-12-03T16:02:53+0000");
        obj4.put("message", message);
        obj4.put("id", "10");
        feed4 = new Feed(obj4);
    }

    @Test
    public void constructorMakeParsingCorrectly() {
        assertEquals("event in blue !", feed1.getContent());
        assertEquals("11", feed1.getId());
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

}
