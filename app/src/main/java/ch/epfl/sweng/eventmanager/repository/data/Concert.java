package ch.epfl.sweng.eventmanager.repository.data;


import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.test.mock.MockContext;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import ch.epfl.sweng.eventmanager.R;

/**
 * Class describing a single concert in an event. The class is for the moment only used by the
 * ScheduleActivity.
 */
public final class Concert {
    /**
     * Indicates the time of the concert, precision required is minutes.
     *
     * This is a long because firebase doesn't understand Date objects.
     */
    private long date;

    /**
     * Indicates the artist or band performing.
     */
    private String artist;

    /**
     * Indicates the music genre (for the moment, it is a string, could become an enum)
     */
    private String genre;

    /**
     * Describes the concert
     */
    private String description;

    /**
     * Duration of the concert in hours
     */
    private double duration;

    private static final double STANDARD_DURATION = 1;

    public Concert(@NonNull Date date,@NonNull String artist,@NonNull String genre,
                   @NonNull String description, double duration) {
        this.date = date.getTime();
        this.artist = artist;
        this.genre = genre;
        this.description = description;
        if (duration <= 0) this.duration = STANDARD_DURATION;
        else this.duration = duration;
    }

    public Date getDate() {
        if (date <= 0) {
            return null;
        }
        return new Date(date);
    }

    public String getArtist() {return artist; }

    public String getDescription() {return description; }

    public String getGenre() {return genre; }

    public double getDuration() {
        return duration;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Concert) {
            Concert compared = (Concert) obj;
            return compared.getDate() == getDate() && compared.getArtist().equals(getArtist()) &&
                    compared.getGenre().equals(getGenre()) && compared.getDuration()==getDuration();
        } else return super.equals(obj);
    }

    public String dateAsString() {
        if (date <= 0) {
            return null;
        }
        SimpleDateFormat f = new SimpleDateFormat("dd MMMM yyyy 'at' kk'h'mm");
        return f.format(date);
    }

    public Date getEndOfConcert() {
        if (getDate() == null) {
            return null;
        }

        int durationMinutes = (int) Math.abs(getDuration() * 60);

        Calendar calendar = new GregorianCalendar();
        calendar.setTime(getDate());
        calendar.add(Calendar.MINUTE, durationMinutes);

        return calendar.getTime();
    }

    public ConcertStatus getStatus() {
        // If we don't have a date, it's in the future
        if (getDate() == null) {
            return ConcertStatus.NOT_STARTED;
        }

        Date currentDate = new Date();

        if (currentDate.after(getDate())) {
            if (currentDate.before(getEndOfConcert())) {
                return ConcertStatus.IN_PROGRESS; //concert is taking place
            } else {
                return ConcertStatus.NOT_STARTED; //concert is later
            }
        } else {
            return ConcertStatus.PASSED; //concert happened
        }
    }

    public enum ConcertStatus {
        /**
         * The concert already happened
         */
        PASSED,
        /**
         * The concert is in progress
         */
        IN_PROGRESS,
        /**
         * The concert will happen in the future
         */
        NOT_STARTED
    }
}
