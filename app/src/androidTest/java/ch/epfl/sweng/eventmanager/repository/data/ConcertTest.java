package ch.epfl.sweng.eventmanager.repository.data;

import android.content.res.Resources;

import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

import ch.epfl.sweng.eventmanager.R;

import static org.junit.Assert.*;

public class ConcertTest {

    private Date now = new Date();
    private String mj = "Michael Jackson";
    private String pop = "Pop";
    private String des = "Amazing event as it is the resurrection of the King!";
    double dur = 3.5;

    private Concert c1 = new Concert();
    private Concert c2 = new Concert(now, mj, pop,
            des, dur);

    @Test
    public void getDate() {
        String no_date = Resources.getSystem().getString(R.string.concert_no_date);
        //assertEquals(c1.getDate(), -----); impossible to test as the time is get and set in constructor
        assertEquals(c2.getDate(), now);
    }

    @Test
    public void getArtist() {
        String no_artist = Resources.getSystem().getString(R.string.concert_no_artist);
        assertEquals(c1.getArtist(), no_artist);
        assertEquals(c2.getArtist(), mj);
    }

    @Test
    public void getDescription() {
        String no_description = Resources.getSystem().getString(R.string.concert_no_description);
        assertEquals(c1.getDescription(), no_description);
        assertEquals(c2.getDescription(), des);
    }

    @Test
    public void getGenre() {
        String no_genre = Resources.getSystem().getString(R.string.concert_no_genre);
        assertEquals(c1.getGenre(), no_genre);
        assertEquals(c2.getGenre(), pop);
    }

    @Test
    public void getDuration() {
        int STANDARD_DURATION = 1;
        assertTrue(c1.getDuration() == STANDARD_DURATION);
        assertTrue(c2.getDuration() == 3);
    }

    @Test
    public void equals() {
        assertFalse(c1.equals(c2));
        Concert copy1 = new Concert();
        assertTrue(c1.equals(copy1));

        Concert copy2 = new Concert(now, mj, pop, des, dur);
        assertTrue(c2.equals(copy2));

        Concert fake_name = new Concert(now, "Michael Fackson", pop, des, dur);
        Concert fake_genre = new Concert(now, mj, "Funky", des, dur);
        Concert fake_des = new Concert(now, mj, pop, "Wow!", dur);
        Concert fake_dur = new Concert(now, mj, pop, des, 5);


        assertFalse(c2.equals(fake_name));
        assertFalse(c2.equals(fake_genre));
        assertFalse(c2.equals(fake_des));
        assertFalse(c2.equals(fake_dur));
    }

    @Test
    public void dateAsString() {
        SimpleDateFormat f = new SimpleDateFormat("dd MMMM yyyy 'at' kk'h'mm");
        String now_s = f.format(now);
        assertEquals(now_s, c2.dateAsString());

        String no_date_string = Resources.getSystem().getString(R.string.concert_no_date);
        assertEquals(no_date_string, c1.dateAsString());

    }

    @Test
    public void getEndOfConcert() {
         assertNull(c1.getEndOfConcert());

         Date end = new Date(now.getYear(), now.getMonth(), now.getDate(), now.getHours()+3, now.getMinutes() +30);
         assertEquals(end, c2.getEndOfConcert());
    }

    @Test
    public void getStatus() {
        assertEquals(Concert.ConcertStatus.PASSED, c1.getStatus());
        assertEquals(Concert.ConcertStatus.IN_PROGRESS, c2.getStatus());
    }
}