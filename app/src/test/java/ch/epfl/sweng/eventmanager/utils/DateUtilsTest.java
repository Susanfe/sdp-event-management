package ch.epfl.sweng.eventmanager.utils;

import android.test.mock.MockContext;
import android.text.Editable;
import android.text.InputFilter;
import android.widget.EditText;
import android.widget.TextView;
import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Louis Vialar
 */
public class DateUtilsTest {
    private final Date testDate;
    private EditText text;

    public DateUtilsTest() {
        Calendar calendar = new GregorianCalendar();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.MONTH, 0);
        calendar.set(Calendar.YEAR, 2018);
        calendar.set(Calendar.HOUR_OF_DAY, 10);
        calendar.set(Calendar.MINUTE, 25);

        testDate = calendar.getTime();
    }

    @Before
    public void before() {
        text = mock(EditText.class);
    }


    @Test
    public void formatDate() {
        assertEquals("01/01/2018", DateUtils.formatDate(testDate));
    }

    @Test
    public void formatTime() {
        assertEquals("10:25", DateUtils.formatTime(testDate));
        assertEquals("10:25", DateUtils.formatTime(10, 25, ":"));
    }

    @Test
    public void formatDuration() {
        assertEquals("01 h 30", DateUtils.formatDuration(1.5));
    }

    @Test
    public void getDateValue() {
        Editable e = editableForText("01/01/2018");
        when(text.getText()).thenReturn(e);

        Date d = new Date(DateUtils.getDateValue(this.text));
        Calendar c = new GregorianCalendar();
        c.setTime(d);

        assertEquals(1, c.get(Calendar.DAY_OF_MONTH));
        assertEquals(0, c.get(Calendar.MONTH));
        assertEquals(2018, c.get(Calendar.YEAR));
    }

    @Test
    public void getTimeValue() {
        Editable e = editableForText("10:25");
        when(text.getText()).thenReturn(e);

        Date d = new Date(DateUtils.getTimeValue(this.text));
        Calendar c = new GregorianCalendar();
        c.setTime(d);

        assertEquals(10, c.get(Calendar.HOUR_OF_DAY));
        assertEquals(25, c.get(Calendar.MINUTE));
    }

    @Test
    public void getDurationValue() {
        Editable e = editableForText("2 h 42");
        when(text.getText()).thenReturn(e);

        double duration = DateUtils.getDurationValue(this.text);
        assertEquals(2.7, duration, 0.01);
    }

    private Editable editableForText(String text) {
        Editable editable = mock(Editable.class);
        when(editable.toString()).thenReturn(text);
        return editable;
    }
}