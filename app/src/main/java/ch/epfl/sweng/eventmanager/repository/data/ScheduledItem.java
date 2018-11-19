package ch.epfl.sweng.eventmanager.repository.data;

import androidx.annotation.NonNull;

import java.io.PrintStream;
import java.text.DateFormat;
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

    public Date getEnd() {
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
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd'T'HHmmss");

        out.println("BEGIN:VEVENT");
        out.println("SUMMARY:" + getArtist());
        out.println("DTSTART;VALUE=DATE-TIME:" + dateFormat.format(getDate()));
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
}
