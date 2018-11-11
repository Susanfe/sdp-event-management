package ch.epfl.sweng.eventmanager.ui.event.interaction.fragments.schedule;

import ch.epfl.sweng.eventmanager.repository.data.ScheduledItem;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.UUID;

import static org.junit.Assert.*;

/**
 * @author Louis Vialar
 */
public class MyScheduleFragmentTest {


    private final Date when = new Calendar.Builder()
            .setFields(Calendar.YEAR, 2018,
                    Calendar.MONTH, Calendar.DECEMBER,
                    Calendar.DAY_OF_MONTH, 25,
                    Calendar.HOUR_OF_DAY, 12,
                    Calendar.MINUTE, 0,
                    Calendar.SECOND, 0).build().getTime();
    private final String mj = "Michael Jackson";
    private final String pop = "Pop";
    private final String des = "Amazing event as it is the resurrection of the King!";
    private final double dur = 3.5;
    private final UUID uuid = UUID.randomUUID();
    private final String type = "Concert";
    private final String location = "Polyv";

    private ScheduledItem c3 = new ScheduledItem(when, mj, pop, des, dur, uuid, type, location);

    @Test
    public void writeCalendarTest() throws Exception {
        MyScheduleFragment fragment = new MyScheduleFragment();

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();

        fragment.writeCalendar(Collections.singletonList(c3), outContent);
        String expected =
                "BEGIN:VCALENDAR\n" +
                        "VERSION:2.0\n" +
                        "PRODID:-//EventManager/MySchedule//Event Schedule//EN\n" +
                        "BEGIN:VEVENT\n" +
                        "SUMMARY:" + mj + "\n" +
                        "DTSTART;VALUE=DATE-TIME:20181225T120000\n" +
                        "DTEND;VALUE=DATE-TIME:20181225T153000\n" +
                        "LOCATION:" + location + "\n" +
                        "END:VEVENT\n" +
                        "END:VCALENDAR\n";

        assertEquals(expected, outContent.toString());

    }
}