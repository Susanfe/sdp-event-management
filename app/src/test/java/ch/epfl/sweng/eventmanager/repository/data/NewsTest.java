package ch.epfl.sweng.eventmanager.repository.data;

import org.junit.Test;

import java.text.DateFormat;
import java.util.Date;

import static org.junit.Assert.*;

/**
 * @author Louis Vialar
 */
public class NewsTest {
    // 24/10/2018 at 16:28:02
    private final News news1 = new News("News1", 1540391282000L, "News 1 Content");
    // 18/10/2018 at 16:28:02
    private final News news2 = new News("News2", 1539872882000L, "News 2 Content");

    @Test
    public void getTitle() {
        assertEquals("News1", news1.getTitle());
        assertEquals("News2", news2.getTitle());
    }

    @Test
    public void getDate() {
        assertEquals(1540391282000L, news1.getDate());
        assertEquals(1539872882000L, news2.getDate());
    }

    @Test
    public void getContent() {
        assertEquals("News 1 Content", news1.getContent());
        assertEquals("News 2 Content", news2.getContent());
    }

    @Test
    public void dateAsString() {
        DateFormat format = DateFormat.getDateTimeInstance();

        assertEquals(format.format(1540391282000L), news1.dateAsString());
        assertEquals(format.format(1539872882000L), news2.dateAsString());
    }

    @Test
    public void compareTo() {
        assertTrue(news1.compareTo(news2) < 0);
    }
}