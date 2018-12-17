package ch.epfl.sweng.eventmanager.repository.data;

import androidx.annotation.NonNull;
import com.google.firebase.database.Exclude;

import java.io.PrintStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Class describing a single scheduled item (concert, activity...) in an event. The class is for the moment only used by
 * the ScheduleFragment.
 */
public final class ScheduledItem {
    public static final double STANDARD_DURATION = 1;
    /**
     * Indicates the time of the item, precision required is minutes.
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
     * Describes the item
     */
    private String description;
    /**
     * Duration of the item in hours
     */
    private double duration;
    /**
     * An UUID identifying this scheduled item uniquely
     */
    private String id;
    /**
     * The place (room, usually) where the event takes place<br>
     * // FIXME Depending on how the map works, we might want to add more data here so that the user can click and go to the map.
     */
    private String itemLocation;

    @Deprecated
    public ScheduledItem(@NonNull Date date, @NonNull String artist, @NonNull String genre, @NonNull String description,
                         double duration, @NonNull UUID uuid, @NonNull String itemLocation) {
        this(date, artist, genre, description, duration, uuid.toString(), itemLocation);
    }

    public ScheduledItem(@NonNull Date date, @NonNull String artist, @NonNull String genre, @NonNull String description,
                         double duration, @NonNull String id, @NonNull String itemLocation) {
        this.date = date.getTime();
        this.artist = artist;
        this.genre = genre;
        this.description = description;
        this.id = id;
        this.itemLocation = itemLocation;

        if (duration <= 0) this.duration = STANDARD_DURATION;
        else this.duration = duration;
    }

    public ScheduledItem() {
    }

    public long getDate() {
        return date;
    }

    @Exclude
    public Date getJavaDate() {
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public void setItemLocation(String itemLocation) {
        this.itemLocation = itemLocation;
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
                && itemLocation.equals(that.itemLocation);
    }

    public String dateAsString() {
        if (date <= 0) {
            return null;
        }
        SimpleDateFormat f = new SimpleDateFormat("dd MMMM yyyy 'at' kk'h'mm", Locale.getDefault());
        return f.format(date);
    }

    public String getItemLocation() {
        return itemLocation;
    }

    @Exclude
    public Date getEnd() {
        if (getJavaDate() == null) {
            return null;
        }

        int durationMinutes = (int) Math.abs(getDuration() * 60);

        Calendar calendar = new GregorianCalendar();
        calendar.setTime(getJavaDate());
        calendar.add(Calendar.MINUTE, durationMinutes);

        return calendar.getTime();
    }

    @Exclude
    public ScheduledItemStatus getStatus() {
        // If we don't have a date, it's in the future
        if (getJavaDate() == null) {
            return ScheduledItemStatus.NOT_STARTED;
        }

        Date currentDate = new Date();

        if (currentDate.after(getJavaDate())) {
            if (currentDate.before(getEnd())) {
                return ScheduledItemStatus.IN_PROGRESS; // concert is taking place
            } else {
                return ScheduledItemStatus.PASSED; // concert is finished
            }
        } else {
            return ScheduledItemStatus.NOT_STARTED; // concert didn't start yet
        }
    }

    /**
     * Print this event as an iCalendar event to the given output stream
     * @param out the stream to print this event in
     */
    public void printAsIcalendar(PrintStream out) {
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd'T'HHmmss",Locale.US);

        out.println("BEGIN:VEVENT");
        out.println("SUMMARY:" + getArtist());
        out.println("DTSTART;VALUE=DATE-TIME:" + dateFormat.format(getJavaDate()));
        out.println("DTEND;VALUE=DATE-TIME:" + dateFormat.format(getEnd()));
        if (getItemLocation() != null) {
            out.println("LOCATION:" + getItemLocation());
        }
        out.println("END:VEVENT");
    }

    public enum ScheduledItemStatus {
        /**
         * The item already happened
         */
        PASSED,
        /**
         * The item is in progress
         */
        IN_PROGRESS,
        /**
         * The item will happen in the future
         */
        NOT_STARTED
    }

    @Override
    public String toString() {
        return "ScheduledItem{" +
                "date=" + date +
                ", artist='" + artist + '\'' +
                ", genre='" + genre + '\'' +
                ", description='" + description + '\'' +
                ", duration=" + duration +
                ", id='" + id + '\'' +
                ", itemLocation='" + itemLocation + '\'' +
                '}';
    }
}
