package ch.epfl.sweng.eventmanager.repository.data;

import android.content.res.Resources;
import android.util.Log;

import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import ch.epfl.sweng.eventmanager.R;

import static org.junit.Assert.*;

public class ConcertTest {

    private final Date now = new Date();
    private final String mj = "Michael Jackson";
    private final String pop = "Pop";
    private final String des = "Amazing event as it is the resurrection of the King!";
    private final double dur = 3.5;

    private Concert c1 = new Concert(now, mj, pop, des, -12);
    private Concert c2 = new Concert(now, mj, pop, des, dur);

    @Test
    public void getDate() {
        assertEquals(c2.getDate(), now);
    }

    @Test
    public void getArtist() {
        assertEquals(c2.getArtist(), mj);
    }

    @Test
    public void getDescription() {
        assertEquals(c2.getDescription(), des);
    }

    @Test
    public void getGenre() {
        assertEquals(c2.getGenre(), pop);
    }

    @Test
    public void getDuration() {
        double STANDARD_DURATION = Concert.STANDARD_DURATION;
        assertTrue(c1.getDuration() == STANDARD_DURATION);
        assertTrue(c2.getDuration() == dur);
    }

    @Test
    public void equals() {
        assertFalse(c1.equals(c2));

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
    }

    @Test
    public void getEndOfConcert() {
         Date end = new Date(now.getYear(), now.getMonth(), now.getDate(), now.getHours()+3, now.getMinutes() +30);
         assertEquals(setPrecisionToMinutes(end), setPrecisionToMinutes(c2.getEndOfConcert()));
    }

    @Test
    public void getStatus() {
        Calendar c = new GregorianCalendar();
        c.set(Calendar.YEAR, 2999);
        Concert fake1 = new Concert(c.getTime(), mj, pop, des, dur);
        c.set(Calendar.YEAR, 1970);
        Concert fake2 = new Concert(c.getTime(), mj, pop, des, dur);

        System.out.print(fake1.dateAsString());
        assertEquals(Concert.ConcertStatus.NOT_STARTED, fake1.getStatus());
        assertEquals(Concert.ConcertStatus.PASSED, fake2.getStatus());
        assertEquals(Concert.ConcertStatus.IN_PROGRESS, c2.getStatus());
    }

    /**
     * Erases seconds precision from the String
     * @param date String with format DDDD MMMM dd HH:mm:ss GMT yyyy
     * @return the broader precision string
     */
    public String setPrecisionToMinutes(Date date) {
        //String regex = "[a-zA-Z]{3,} [a-zA-Z]{3,} [0-9]{2} [0-9]{2}:[0-9]{2}:[0-9]{2} GMT[\\+\\-][0-9]{2}:[0-9]{2}[0-9]{4}";
        String dateS =date.toString();
        String[] splitted = dateS.split(":");
        dateS = splitted[0] + ':' +
                splitted[1] + ' ' +
                dateS.split(" ")[1] + ':' +
                splitted[3];

        /* "Mon Oct 15 22:11:00 GMT+02:00 2018" is first split into
         * [Mon Oct 15 22] [11] [00 GMT+02] [00 2018]
         *
         * And then date is reconstituted as follows
         * date = [Mon Oct 15 22] : [11]  [GMT+02] : [00 2018]
         */

        return dateS;
    }


}