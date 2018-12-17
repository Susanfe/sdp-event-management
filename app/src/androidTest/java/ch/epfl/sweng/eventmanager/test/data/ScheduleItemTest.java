package ch.epfl.sweng.eventmanager.test.data;

import android.os.Parcel;
import ch.epfl.sweng.eventmanager.repository.data.ScheduledItem;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import static org.junit.Assert.assertEquals;

public class ScheduleItemTest {
    private final Date when = new Calendar.Builder().setFields(Calendar.YEAR, 2018, Calendar.MONTH, Calendar.DECEMBER
            , Calendar.DAY_OF_MONTH, 25, Calendar.HOUR_OF_DAY, 12, Calendar.MINUTE, 0, Calendar.SECOND, 0).build().getTime();
    private final String mj = "Michael Jackson";
    private final String pop = "Pop";
    private final String des = "Amazing event as it is the resurrection of the King!";
    private final double dur = 3.5;
    private final UUID uuid = UUID.randomUUID();
    private final String location = "Polyv";

    private ScheduledItem c3 = new ScheduledItem(when, mj, pop, des, dur, uuid, location);

    @Test
    public void testParcelItem() {
        Parcel parcel = Parcel.obtain();
        c3.writeToParcel(parcel, 0);
        parcel.setDataPosition(0);
        ScheduledItem fromParcel = ScheduledItem.CREATOR.createFromParcel(parcel);
        assertEquals(c3, fromParcel);
    }
}
