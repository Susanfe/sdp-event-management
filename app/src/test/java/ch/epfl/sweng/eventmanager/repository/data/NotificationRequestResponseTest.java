package ch.epfl.sweng.eventmanager.repository.data;

import ch.epfl.sweng.eventmanager.notifications.NotificationRequestResponse;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class NotificationRequestResponseTest {

    private final NotificationRequestResponse notificationRequest1 = new NotificationRequestResponse("1", "body1", "1");

    @Test
    public void readTitleTest(){
        assertEquals(notificationRequest1.getTitle(), "1");
    }

    @Test
    public void readBodyTest(){
        assertEquals(notificationRequest1.getBody(), "body1");
    }

    @Test
    public void readGetFromTest(){
        assertEquals(notificationRequest1.getFrom(), "1");
    }

}
