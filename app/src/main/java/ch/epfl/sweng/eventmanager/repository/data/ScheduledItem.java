package ch.epfl.sweng.eventmanager.repository.data;


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
     *     // FIXME Depending on how the map works, we might want to add more data here so that the user can click and go to the map.
     */
    private String itemLocation;

    private static final double STANDARD_DURATION = 1;

    public ScheduledItem(Date date, String artist, String genre, String description, double duration, UUID id, String itemType, String itemLocation) {
        this.date = date.getTime();
        this.artist = artist;
        this.genre = genre;
        this.description = description;
        this.duration = duration;
        this.id = id.toString();
        this.itemType = itemType;
        this.itemLocation = itemLocation;
    }

    public ScheduledItem() {}

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
    public boolean equals(Object obj) {
        if (obj instanceof ScheduledItem) {
            ScheduledItem compared = (ScheduledItem) obj;
            return compared.date == date && compared.artist.equals(artist) &&
                    compared.genre.equals(genre) && compared.duration==duration;
        } else return super.equals(obj);
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
                return ScheduledItemStatus.IN_PROGRESS; //concert is taking place
            } else {
                return ScheduledItemStatus.NOT_STARTED; //concert is later
            }
        } else {
            return ScheduledItemStatus.PASSED; //concert happened
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
