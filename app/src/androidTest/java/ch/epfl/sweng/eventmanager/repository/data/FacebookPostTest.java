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

public class FacebookPostTest {
    private FacebookPost facebookPost1;
    private FacebookPost facebookPost2;
    private FacebookPost facebookPost3;
    private FacebookPost facebookPost4;

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
        facebookPost1 = new FacebookPost(obj1);

        JSONObject obj2 = new JSONObject();
        obj2.put("created_time", createdTime);
        obj2.put("message", message);
        obj2.put("id", "11");
        facebookPost2 = new FacebookPost(obj2);

        JSONObject obj3 = new JSONObject();
        obj3.put("created_time", createdTime);
        obj3.put("message", "event in blue");
        obj3.put("id", "11");
        facebookPost3 = new FacebookPost(obj3);

        JSONObject obj4 = new JSONObject();
        obj4.put("created_time", createdTime);
        obj4.put("message", message);
        obj4.put("id", "10");
        facebookPost4 = new FacebookPost(obj4);
    }

    @Test
    public void constructorMakeParsingCorrectly() {
        assertEquals("event in blue !", facebookPost1.getContent());
        assertEquals("11", facebookPost1.getId());
        assertEquals("title", facebookPost1.getName());
        assertEquals("Come with happiness", facebookPost1.getDescription());
        assertThat(facebookPost1.getImageURL(), is(Uri.parse("url")));
    }

    @Test
    public void constructorWorkWithException() throws JSONException {
        String jStr= "{\"name\":\"hahaha\"}";
        JSONObject obj5 = new JSONObject(jStr);
        new FacebookPost(obj5);
    }

    @Test
    public void dateAsStringWork() {
        assertTrue(facebookPost1.dateAsString().contains("Dec 3,"));
    }

    @Test
    public void equalMethodWork() {
        assertTrue(facebookPost1.equals(facebookPost2));
        assertTrue(!facebookPost1.equals(facebookPost3));
        assertTrue(!facebookPost1.equals(facebookPost4));
    }

    @Test
    public void containsTypesWork() {
        assertTrue(facebookPost1.hasContent());
        assertTrue(facebookPost1.hasDescription());
        assertTrue(facebookPost1.hasImage());
        assertTrue(facebookPost1.hasName());

        assertFalse(facebookPost2.hasImage());
        assertFalse(facebookPost2.hasDescription());
        assertFalse(facebookPost2.hasName());
    }

}
