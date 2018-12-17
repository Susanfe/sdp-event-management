package ch.epfl.sweng.eventmanager.repository.data;

import ch.epfl.sweng.eventmanager.notifications.NotificationRequest;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class NotificationRequestTest {

    private final NotificationRequest notificationRequest1 = new NotificationRequest("1", "body1", 1);

    @Test
    public void writeAndReadTitleTest(){
        assertEquals(notificationRequest1.getTitle(), "1");
        notificationRequest1.setTitle("1m");
        assertEquals(notificationRequest1.getTitle(), "1m");
    }

    @Test
    public void writeAndReadBodyTest(){
        assertEquals(notificationRequest1.getBody(), "body1");
        notificationRequest1.setBody("body1m");
        assertEquals(notificationRequest1.getBody(), "body1m");
    }

    @Test
    public void writeAndReadEventIdTest(){
        assertEquals(notificationRequest1.getEventId(), 1);
        notificationRequest1.setEventId(2);
        assertEquals(notificationRequest1.getEventId(), 2);
    }

}
