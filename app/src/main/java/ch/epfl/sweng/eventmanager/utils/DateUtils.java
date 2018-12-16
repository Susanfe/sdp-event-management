package ch.epfl.sweng.eventmanager.utils;

import android.util.Log;
import android.widget.EditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @author Louis Vialar
 */
public class DateUtils {
    public static String formatDate(Date date) {
        if (date != null) {
            String format = "dd/MM/yyyy";
            SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.getDefault());
            return dateFormat.format(date);
        } else {
            return null;
        }
    }

    public static String formatTime(int hours, int minutes, String separator) {
        StringBuilder sb = new StringBuilder();

        if (hours < 10)
            sb.append('0');
        sb.append(hours);
        sb.append(separator);
        if (minutes < 10)
            sb.append('0');
        sb.append(minutes);
        return sb.toString();
    }

    public static String formatTime(Date date) {
        if (date != null) {
            String format = "HH:mm";
            SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.getDefault());
            return dateFormat.format(date);
        } else {
            return null;
        }
    }

    public static String formatDuration(double duration) {
        int hours = (int) duration;
        int minutes = (int) ((duration - hours) * 60D);

        return formatTime(hours, minutes, " h ");
    }


    private static long getStringValue(EditText editText, String format) {
        long date = 0L;
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.getDefault());
        try {
            date = dateFormat.parse(editText.getText().toString()).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            Log.i("DateUtils", "unable to parse date");
        }
        return date;
    }

    public static long getDateValue(EditText editText) {
        String format = "dd/MM/yyyy";
        return getStringValue(editText, format);
    }

    public static long getTimeValue(EditText editText) {
        String format = "HH:mm";
        return getStringValue(editText, format);
    }

    public static double getDurationValue(EditText duration) {
        String[] text = duration.getText().toString().split(" h ");

        if (text.length != 2) {
            return 0D;
        }

        try {
            int hours = Integer.parseInt(text[0]);
            double minutes = (Integer.parseInt(text[1])) / 60D;

            return hours + minutes;
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
            return 0D;
        }
    }
}
