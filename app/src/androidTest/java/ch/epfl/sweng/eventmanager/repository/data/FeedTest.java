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
        JSONObject obj1 = new JSONObject();
        obj1.put("created_time", "2018-12-03T16:02:53+0000");
        obj1.put("message", "event in blue !");
        obj1.put("id", "11");
        feed1 = new Feed(obj1);

        JSONObject obj2 = new JSONObject();
        obj2.put("created_time", "2018-12-03T16:02:53+0000");
        obj2.put("message", "event in blue !");
        obj2.put("id", "11");
        feed2 = new Feed(obj2);

        JSONObject obj3 = new JSONObject();
        obj3.put("created_time", "2018-12-03T16:02:53+0000");
        obj3.put("message", "event in blue");
        obj3.put("id", "11");
        feed3 = new Feed(obj3);

        JSONObject obj4 = new JSONObject();
        obj4.put("created_time", "2018-12-03T16:02:53+0000");
        obj4.put("message", "event in blue !");
        obj4.put("id", "10");
        feed4 = new Feed(obj4);
    }

    @Test
    public void constructorMakeParsingCorrectly() {
        assertEquals("event in blue !", feed1.getContent());
        assertEquals("Mon Dec 03 17:02:53 GMT+01:00 2018", feed1.getTime().toString());
        assertEquals("11", feed1.getId());
    }

    @Test
    public void dateAsStringWork() {
        assertEquals("Dec 3, 2018 5:02:53 PM", feed1.dateAsString());
    }

    @Test
    public void equalMethodWork() {
        assertTrue(feed1.equals(feed2));
        assertTrue(!feed1.equals(feed3));
        assertTrue(!feed1.equals(feed4));
    }
}