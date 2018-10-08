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

    public static final String NO_ARTIST = "Unknown artist";
    public static final String NO_GENRE = "Unknown genre";
    public static final String NO_DESCRIPTION = "This event does not have a description.";


    public Concert(Date date, String artist) {
        new Concert(date, artist, NO_GENRE, NO_DESCRIPTION);
    }

    public Concert(Date date, String artist, String genre, String description) {
        this.date = (Date) date.clone();
        this.artist = (artist == null) ? NO_ARTIST : artist;

        this.genre = (genre == null) ? NO_GENRE : genre;
        this.description = (description == null) ? NO_DESCRIPTION : description;
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

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Concert) {
            Concert compared = (Concert) obj;
            return compared.date.equals(date) && compared.artist.equals(artist) &&
                    compared.genre.equals(genre);
        } else return super.equals(obj);
    }
}
