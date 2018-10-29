package ch.epfl.sweng.eventmanager.repository.data;

import android.support.annotation.NonNull;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * @author Louis Vialar
 */
public final class News implements Comparable<News> {
    private String title;
    private long date;
    private String content;

    public News(String title, long date, String content) {
        this.title = title;
        this.date = date;
        this.content = content;
    }

    public News() {
    }

    public String getTitle() {
        return title;
    }

    public long getDate() {
        return date;
    }

    public String getContent() {
        return content;
    }

    public String dateAsString() {
        if (date <= 0) {
            return null;
        }
        DateFormat f = DateFormat.getDateTimeInstance();
        return f.format(date);
    }

    @Override
    public int compareTo(@NonNull News o) {
        return Long.compare(o.date, date);
    }
}
