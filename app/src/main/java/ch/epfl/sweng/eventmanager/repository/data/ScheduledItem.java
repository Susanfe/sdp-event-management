package ch.epfl.sweng.eventmanager.repository.data;

import android.support.annotation.NonNull;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.UUID;

/**
 * Class describing a single scheduled item (concert, activity...) in an event. The class is for the moment only used by
 * the ScheduleFragment.
 */
public final class ScheduledItem {
    public static final double STANDARD_DURATION = 1;
    /**
     * Indicates the time of the concert, precision required is minutes.
     * <p>
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
    /**
     * An UUID identifying this scheduled item uniquely
     */
    private String id;
    /**
     * The type of the item<br>
     * It would make sense to have an enum, but a single string allows the organizer to defines its own types of items
     */
    private String itemType;
    /**
     * The place (room, usually) where the event takes place<br>
     * // FIXME Depending on how the map works, we might want to add more data here so that the user can click and go to the map.
     */
    private String itemLocation;

    public ScheduledItem(@NonNull Date date, @NonNull String artist, @NonNull String genre, @NonNull String description,
                         double duration, @NonNull UUID id, @NonNull String itemType, @NonNull String itemLocation) {
        this.date = date.getTime();
        this.artist = artist;
        this.genre = genre;
        this.description = description;
        this.id = id.toString();
        this.itemType = itemType;
        this.itemLocation = itemLocation;

        if (duration <= 0) this.duration = STANDARD_DURATION;
        else this.duration = duration;
    }

    public ScheduledItem() {
    }

    public Date getDate() {
        if (date <= 0) {
            return null;
        }
        return new Date(date);
    }

    public String getArtist() {
        return artist;
    }

    public String getDescription() {
        return description;
    }

    public String getGenre() {
        return genre;
    }

    public double getDuration() {
        return duration;
    }

    public UUID getId() {
        return UUID.fromString(id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ScheduledItem that = (ScheduledItem) o;

        return date == that.date
                && Double.compare(that.duration, duration) == 0
                && artist.equals(that.artist)
                && genre.equals(that.genre)
                && description.equals(that.description)
                && id.equals(that.id)
                && itemType.equals(that.itemType)
                && itemLocation.equals(that.itemLocation);
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = (int) (date ^ (date >>> 32));
        result = 31 * result + (artist != null ? artist.hashCode() : 0);
        result = 31 * result + (genre != null ? genre.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        temp = Double.doubleToLongBits(duration);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (id != null ? id.hashCode() : 0);
        result = 31 * result + (itemType != null ? itemType.hashCode() : 0);
        result = 31 * result + (itemLocation != null ? itemLocation.hashCode() : 0);
        return result;
    }

    public String dateAsString() {
        if (date <= 0) {
            return null;
        }
        SimpleDateFormat f = new SimpleDateFormat("dd MMMM yyyy 'at' kk'h'mm");
        return f.format(date);
    }

    public String getItemType() {
        return itemType;
    }

    public String getItemLocation() {
        return itemLocation;
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

    public ScheduledItemStatus getStatus() {
        // If we don't have a date, it's in the future
        if (getDate() == null) {
            return ScheduledItemStatus.NOT_STARTED;
        }

        Date currentDate = new Date();

        if (currentDate.after(getDate())) {
            if (currentDate.before(getEndOfConcert())) {
                return ScheduledItemStatus.IN_PROGRESS; // concert is taking place
            } else {
                return ScheduledItemStatus.PASSED; // concert is finished
            }
        } else {
            return ScheduledItemStatus.NOT_STARTED; // concert didn't start yet
        }
    }

    public enum ScheduledItemStatus {
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
