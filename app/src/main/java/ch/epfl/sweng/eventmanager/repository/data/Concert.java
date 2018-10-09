package ch.epfl.sweng.eventmanager.repository.data;


import java.util.Date;

/**
 * Class describing a single concert in an event. The class is for the moment only used by the
 * ScheduleActivity.
 */
public final class Concert {
    /**
     * Indicates the time of the concert, precision required is minutes.
     */
    private Date date;

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


    public Concert(Date date, String artist) {
        new Concert(date, artist, null, null, STANDARD_DURATION);
    }

    public Concert(Date date, String artist, String genre, String description, double duration) {
        this.date = (Date) date.clone();
        this.artist = artist;
        this.genre = genre;
        this.description = description;
        this.duration = duration;
    }

    public Date getDate() {
        return date;
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

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Concert) {
            Concert compared = (Concert) obj;
            return compared.date.equals(date) && compared.artist.equals(artist) &&
                    compared.genre.equals(genre) && compared.duration==duration;
        } else return super.equals(obj);
    }
}
