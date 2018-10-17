package ch.epfl.sweng.eventmanager.repository.data;

import android.content.res.Resources;
import android.util.Log;

import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.UUID;

import static org.junit.Assert.*;

public class ScheduledItemTest {

    private final Date now = new Date();
    private final String mj = "Michael Jackson";
    private final String pop = "Pop";
    private final String des = "Amazing event as it is the resurrection of the King!";
    private final double dur = 3.5;
    private final UUID uuid = UUID.randomUUID();
    private final String type = "Concert";
    private final String location = "Polyv";

    private ScheduledItem c1 = new ScheduledItem(now, mj, pop, des, -12, uuid, type, location);
    private ScheduledItem c2 = new ScheduledItem(now, mj, pop, des, dur, uuid, type, location);

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
        double STANDARD_DURATION = ScheduledItem.STANDARD_DURATION;
        assertEquals(c1.getDuration(), STANDARD_DURATION, 0.0);
        assertEquals(c2.getDuration(), dur, 0.0);
    }

    @Test
    public void equals() {
        assertNotEquals(c1, c2);

        ScheduledItem copy2 = new ScheduledItem(now, mj, pop, des, dur, uuid, type, location);
        assertEquals(c2, copy2);

        ScheduledItem fakeName = new ScheduledItem(now, "Michael Fackson", pop, des, dur, uuid, type, location);
        ScheduledItem fakeGenre = new ScheduledItem(now, mj, "Funky", des, dur, uuid, type, location);
        ScheduledItem fakeDes = new ScheduledItem(now, mj, pop, "Wow!", dur, uuid, type, location);
        ScheduledItem fakeDur = new ScheduledItem(now, mj, pop, des, 5, uuid, type, location);
        ScheduledItem fakeUuid = new ScheduledItem(now, mj, pop, des, 5, UUID.randomUUID(), type, location);
        ScheduledItem fakeType = new ScheduledItem(now, mj, pop, des, 5, uuid, "Something", location);
        ScheduledItem fakeLocation = new ScheduledItem(now, mj, pop, des, 5, uuid, type, "Somewhere");


        assertNotEquals(c2, fakeName);
        assertNotEquals(c2, fakeGenre);
        assertNotEquals(c2, fakeDes);
        assertNotEquals(c2, fakeDur);
        assertNotEquals(c2, fakeUuid);
        assertNotEquals(c2, fakeType);
        assertNotEquals(c2, fakeLocation);
    }

    @Test
    public void dateAsString() {
        SimpleDateFormat f = new SimpleDateFormat("dd MMMM yyyy 'at' kk'h'mm");
        String now_s = f.format(now);
        assertEquals(now_s, c2.dateAsString());
    }

    @Test
    public void getEndOfScheduledItem() {
         Date end = new Date(now.getYear(), now.getMonth(), now.getDate(), now.getHours()+3, now.getMinutes() +30);
         assertEquals(setPrecisionToMinutes(end), setPrecisionToMinutes(c2.getEndOfConcert()));
    }

    @Test
    public void getStatus() {
        Calendar c = new GregorianCalendar();
        c.set(Calendar.YEAR, 2999);
        ScheduledItem fake1 = new ScheduledItem(c.getTime(), mj, pop, des, dur, uuid, type, location);
        c.set(Calendar.YEAR, 1970);
        ScheduledItem fake2 = new ScheduledItem(c.getTime(), mj, pop, des, dur, uuid, type, location);

        System.out.print(fake1.dateAsString());
        assertEquals(ScheduledItem.ScheduledItemStatus.NOT_STARTED, fake1.getStatus());
        assertEquals(ScheduledItem.ScheduledItemStatus.PASSED, fake2.getStatus());
        assertEquals(ScheduledItem.ScheduledItemStatus.IN_PROGRESS, c2.getStatus());
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