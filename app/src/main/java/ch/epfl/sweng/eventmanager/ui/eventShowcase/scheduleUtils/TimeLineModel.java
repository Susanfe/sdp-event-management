package ch.epfl.sweng.eventmanager.ui.eventShowcase.scheduleUtils;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class TimeLineModel implements Parcelable {

    private String artist;
    private Date date;
    private String stringDate;
    private String genre;
    private String description;
    private double duration;
    private int isNow;

    public TimeLineModel(String artist, Date date, String genre, String description, double duration) {
        this.artist = artist;
        this.date = date;
        this.stringDate = date != null ? date.toString() : null;
        this.genre = genre;
        this.description = description;
        this.duration = duration;
        if (!setIsNow()) isNow = 1;
    }

    public String getArtist() {
        return artist;
    }

    public Date getDate() {
        return date;
    }

    public String getGenre() {
        return genre;
    }

    public String getDescription() {
        return description;
    }

    public double getDuration() {
        return duration;
    }

    public String getStringDate() {
        return stringDate;
    }

    public int getIsNow() {
        return isNow;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.artist);
        dest.writeString(date.toString());
        dest.writeString(genre);
        dest.writeString(description);
        dest.writeDouble(duration);
        if(!setIsNow()) isNow = 1;
        dest.writeInt(isNow);
    }

    private TimeLineModel(Parcel in) {
        this.artist = in.readString();
        this.stringDate = in.readString();
        this.genre = in.readString();
        this.description = in.readString();
        this.duration = in.readDouble();
        this.isNow = in.readInt();
        date = null;
    }

    public static final Parcelable.Creator<TimeLineModel> CREATOR = new Creator<TimeLineModel>() {
        @Override
        public TimeLineModel createFromParcel(Parcel source) {
            return new TimeLineModel(source);
        }

        @Override
        public TimeLineModel[] newArray(int size) {
            return new TimeLineModel[size];
        }
    };

    /**
     * Sets isNow, the int is 0 if the concert is happening at the time of creation of the timeline,
     * 1 if the concert will happen and -1 if it already happened.
     *
     * @return true if the operation was successful
     */
    private boolean setIsNow() {
        Date actualDate = new Date();
        if (getDate() == null) {
            return false;
        }

        Date endOfConcert = (Date) getDate().clone();

        int duration_hours = (int)Math.abs(getDuration());

        endOfConcert.setHours(endOfConcert.getHours() + duration_hours);
        endOfConcert.setMinutes(endOfConcert.getMinutes() + (int)Math.abs(
                (getDuration() - duration_hours)*10));

        if (actualDate.after(getDate())) {
            if (actualDate.before(endOfConcert)) isNow = 0; //concert is taking place
            else isNow = 1; //concert is later
        }
        else isNow = -1; //concert happened

        return true;
    }
}